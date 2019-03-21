package it.akademija.documents.service;


import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import it.akademija.audit.service.AuditService;
import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.exceptions.NoApproverAvailableException;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.service.FileServiceObject;
import it.akademija.helpers.DocumentHelper;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private DocumentHelper document;

    Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);

    @Transactional
    public Set<DocumentServiceObject> getDocumentsByState(DocumentState state) {
        LOGGER.debug("getDocumentsByState");
        return documentRepository.findByDocumentState(state)
                .stream()
                .map(documentEntity -> SOfromEntity(documentEntity))
                .collect(Collectors.toSet());
    }

    @Transactional
    public DocumentServiceObject getDocument(String documentIdentifier, String username) {
        LOGGER.debug("getDocument");
        DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentFromDatabase == null) {
            throw new NullPointerException("Dokumentas su id '" + documentIdentifier + "'nerastas");
        }

        LOGGER.debug("document - " + documentIdentifier + " is being returned ");

        List<String> documentTypesUserCanApprove =
                document.getDocumentTypesUserCanAprooveBy(username);

        boolean isAuthor = documentFromDatabase.getAuthor().equals(username);
        boolean isApprover = (documentFromDatabase.getDocumentState() == DocumentState.SUBMITTED
                || documentFromDatabase.getDocumentState() == DocumentState.APPROVED
                || documentFromDatabase.getDocumentState() == DocumentState.REJECTED)
                && (documentTypesUserCanApprove.contains(documentFromDatabase.getType()));

        if (isAuthor || isApprover) {
            LOGGER.debug("document - " + documentIdentifier + " is being returned ");
            return SOfromEntity(documentFromDatabase);

        } else {
            throw new SecurityException("Šio dokumento negalite peržiūrėti dėl prieigos teisių");
        }
    }

    @Transactional
    public DocumentEntity createDocument(String username, String title, String type, String description)
            throws IllegalArgumentException, SecurityException {
        LOGGER.debug("createDocument");

        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new SecurityException("Naudotojas " + username + " nerastas");
        }

        DocumentTypeEntity typeEntity = documentTypeRepository.findDocumentTypeByTitle(type);
        if (typeEntity == null) {
            throw new IllegalArgumentException("Tipas '" + type + "' nerastas");
        }

        Set<DocumentTypeEntity> typesUserAllowedToUpload = new HashSet<DocumentTypeEntity>();

        for (UserGroupEntity userGroupEntity : user.getUserGroups()) {
            typesUserAllowedToUpload.addAll(userGroupEntity.getAvailableDocumentTypesToUpload());
        }

        if (!typesUserAllowedToUpload.contains(typeEntity)) {
            throw new SecurityException("Jums negalima kurti '" + type + "' tipo dokumento");
        }

        DocumentEntity newDocument = new DocumentEntity(title, description, type);
        newDocument.setAuthor(user.getUsername());
        user.addDocument(newDocument);
        documentRepository.save(newDocument);

        auditService.addNewAuditEntry(user, AuditActionEnum.CREATE_NEW_DOCUMENT,
                ObjectTypeEnum.DOCUMENT, newDocument.getDocumentIdentifier());
        return newDocument;
    }

    @Transactional
    public void submitDocument(String documentIdentifier, String username)
            throws IllegalArgumentException, NoApproverAvailableException {
        LOGGER.debug("submitDocument");
        DocumentEntity document = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);

        if (document == null) {
            throw new IllegalArgumentException("Dokumentas su id '" + documentIdentifier + "' nerastas");
        }

        DocumentTypeEntity type = documentTypeRepository.findDocumentTypeByTitle(document.getType());
        if (type == null) {
            throw new IllegalArgumentException("Dokumentas su id '" + documentIdentifier + "' turi klaidingą tipą");
        }

        List<UserGroupEntity> allUserGroups = userGroupRepository.findAll();

        boolean groupWhichCanApproveDocumentTypeFound = false;
        boolean groupWhichCanApproveDocumentTypeAndHasUsersFound = false;

        for (UserGroupEntity group : allUserGroups) {
            if (!groupWhichCanApproveDocumentTypeAndHasUsersFound)
                if (group.getAvailableDocumentTypesToApprove().contains(type)) {
                    groupWhichCanApproveDocumentTypeFound = true;

                    if (group.getGroupUsers().size() > 0) {
                        groupWhichCanApproveDocumentTypeAndHasUsersFound = true;
                    }
                }

        }

        if (!groupWhichCanApproveDocumentTypeFound) {
            throw new NoApproverAvailableException("Nėra grupės, kuri galėtų tvirtinti šį dokumentą");
        }

        if (!groupWhichCanApproveDocumentTypeAndHasUsersFound) {
            throw new NoApproverAvailableException("Yra grupė(-s), bet nėra naudotojų, kurie galėtų tvirtinti šį dokumentą");
        }

        document.setDocumentState(DocumentState.SUBMITTED);
        document.setPostedDate(LocalDateTime.now());
        documentRepository.save(document);

        UserEntity user = userRepository.findUserByUsername(username);
        if (user != null) {
            auditService.addNewAuditEntry(user, AuditActionEnum.SUBMIT_DOCUMENT,
                    ObjectTypeEnum.DOCUMENT, document.getDocumentIdentifier());
        }

    }

    @Transactional
    public void approveOrRejectDocument(String documentIdentifier,
                                        String username,
                                        DocumentState newState,
                                        String rejectedReason) throws IllegalArgumentException, SecurityException {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            LOGGER.debug("approveOrRejectDocument");
            DocumentEntity document = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            if (document == null) {
                throw new IllegalArgumentException("Dokumentas, kurio ID '" + documentIdentifier + "', nerastas");
            }

            UserEntity user = userRepository.findUserByUsername(username);
            if (user == null) {
                throw new SecurityException("Naudotojas " + username + " nerastas");
            }

            Set<DocumentTypeEntity> typesUserAllowedToApprove = new HashSet<DocumentTypeEntity>();

            for (UserGroupEntity userGroupEntity : user.getUserGroups()) {
                typesUserAllowedToApprove.addAll(userGroupEntity.getAvailableDocumentTypesToApprove());
            }

            DocumentTypeEntity type = documentTypeRepository.findDocumentTypeByTitle(document.getType());
            if (type == null) {
                throw new IllegalArgumentException("Dokumentas, kurio id '" + documentIdentifier + "', turi klaidingą tipą");
            }

            if (!typesUserAllowedToApprove.contains(type)) {
                throw new IllegalArgumentException("Jums negalima tvirtinti šio dokumento");
            }

            switch (newState) {
                case REJECTED:
                    document.setDocumentState(newState);
                    document.setRejectedDate(LocalDateTime.now());
                    document.setApprover(user.getUsername());
                    document.setRejectionReason(rejectedReason);
                    documentRepository.save(document);
                    auditService.addNewAuditEntry(user, AuditActionEnum.REJECT_DOCUMENT,
                            ObjectTypeEnum.DOCUMENT, document.getDocumentIdentifier());
                    break;
                case APPROVED:
                    document.setDocumentState(newState);
                    document.setApprovalDate(LocalDateTime.now());
                    document.setApprover(user.getUsername());
                    documentRepository.save(document);
                    auditService.addNewAuditEntry(user, AuditActionEnum.APPROVE_DOCUMENT,
                            ObjectTypeEnum.DOCUMENT, document.getDocumentIdentifier());
                    break;
                default:
                    throw new IllegalArgumentException("Netinkamas tipas");
            }


        }

    }

    public DocumentRepository getDocumentRepository() {
        return documentRepository;
    }

    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addFileToDocument(String documentIdentifier, FileEntity fileEntity) {
        LOGGER.debug("addFileToDocument");
        getDocumentEntityByDocumentIdentifier(documentIdentifier).addFileToDocument(fileEntity);
    }

    @Transactional
    public DocumentServiceObject getDocumentByDocumentIdentifier(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            LOGGER.debug("getDocumentByDocumentIdentifier");

            DocumentServiceObject documentServiceObject = SOfromEntity
                    (documentRepository.findDocumentByDocumentIdentifier(documentIdentifier));
            return documentServiceObject;
        } else {
            throw new IllegalArgumentException("Nepateiktas teisingas dokumento identifikacinis numeris");
        }
    }

    @Transactional
    public DocumentEntity getDocumentEntityByDocumentIdentifier(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            LOGGER.debug("getDocumentEntityByDocumentIdentifier");

            DocumentEntity documentEntity =
                    documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            return documentEntity;
        } else {
            throw new IllegalArgumentException("Nepateiktas teisingas dokumento identifikacinis numeris");
        }
    }

    @Transactional
    public void deleteDocument(String documentIdentifier) {
        LOGGER.debug("deleteDocument");
        DocumentEntity documentEntity = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentEntity.getDocumentState().equals(DocumentState.CREATED) ||
                documentEntity.getDocumentState().equals(DocumentState.REJECTED)) {
            documentRepository.deleteDocumentByDocumentIdentifier(documentIdentifier);

        }
    }

    public long getDocumentCount(String username) {
        return documentRepository.getDocumentCountByUsername(username);
    }

    private DocumentServiceObject SOfromEntity(DocumentEntity entity) {
        LOGGER.debug("SOfromEntity");
        DocumentServiceObject so = new DocumentServiceObject();

        so.setApprovalDate(entity.getApprovalDate());
        so.setApprover(entity.getApprover());
        so.setAuthor(entity.getAuthor());
        so.setDescription(entity.getDescription());
        so.setDocumentIdentifier(entity.getDocumentIdentifier());
        so.setDocumentState(entity.getDocumentState());
        so.setPostedDate(entity.getPostedDate());
        so.setRejectedDate(entity.getRejectedDate());
        so.setRejectedReason(entity.getRejectionReason());
        so.setTitle(entity.getTitle());
        so.setType(entity.getType());

        so.setFilesAttachedToDocument(entity.getFileSet()
                .stream()
                .map(file -> new FileServiceObject(file.getFileName(), file.getContentType(),
                        file.getSize(), file.getIdentifier()))
                .collect(Collectors.toSet()));
        return so;
    }

}