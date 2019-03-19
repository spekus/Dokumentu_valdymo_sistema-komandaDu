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
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import org.slf4j.Logger;
import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
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

//    @Autowired
//    private AuditRepository auditRepository;
//
//    @Autowired
//    private AuditEntryEntity auditEntryEntity;

//    @Autowired
//    private Logger LOGGER;

    //    private static Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);
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
    public DocumentServiceObject getDocument(String documentIdentifier) {
        LOGGER.debug("getDocument");
        DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentFromDatabase == null) {
            throw new NullPointerException("Dokumentas su id '" + documentIdentifier + "'nerastas");
        }
        LOGGER.debug("document - " + documentIdentifier + " is being returned ");
        return SOfromEntity(documentFromDatabase);
    }


    @Transactional
    public DocumentEntity createDocument(String username, String title, String type, String description)
            throws IllegalArgumentException, SecurityException {
        LOGGER.debug("createDocument");
        //Pasiimam useri is DB
        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new SecurityException("Naudotojas " + username + " nerastas");
        }

        // surandame tipa, kuri prasoma sukurti. Patikrinam, kad egzistuoja toks.
        DocumentTypeEntity typeEntity = documentTypeRepository.findDocumentTypeByTitle(type);
        if (typeEntity == null) {
            throw new IllegalArgumentException("Tipas '" + type + "' nerastas");
        }

        // susirinkime visus tipus, kuriuos sis naudotojas gali ikelti
        Set<DocumentTypeEntity> typesUserAllowedToUpload = new HashSet<DocumentTypeEntity>();

        // surenkame sarasa dokumentu tipu, kuriuos siam naudotojui leidziama kurti
        for (UserGroupEntity userGroupEntity : user.getUserGroups()) {
            typesUserAllowedToUpload.addAll(userGroupEntity.getAvailableDocumentTypesToUpload());
        }

        // patikriname, ar sis tipas yra tarp leidziamu kurti
        if (!typesUserAllowedToUpload.contains(typeEntity)) {
            throw new SecurityException("Jums negalima kurti '" + type + "' tipo dokumento");
        }

        DocumentEntity newDocument = new DocumentEntity(title, description, type);
        newDocument.setAuthor(user.getUsername());
        user.addDocument(newDocument);
        documentRepository.save(newDocument);

        auditService.addNewAuditEntry(user, AuditActionEnum.CREATE_NEW_DOCUMENT, ObjectTypeEnum.DOCUMENT, newDocument.getDocumentIdentifier());

        return newDocument;


    }

    @Transactional
    public void submitDocument(String documentIdentifier, String username)
            throws IllegalArgumentException, NoApproverAvailableException {
        LOGGER.debug("submitDocument");
        DocumentEntity document = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);

        if (document == null) {
            throw new IllegalArgumentException("Documentas su id '" + documentIdentifier + "' nerastas");
        }

        DocumentTypeEntity type = documentTypeRepository.findDocumentTypeByTitle(document.getType());
        if (type == null) {
            throw new IllegalArgumentException("Documentas su id '" + documentIdentifier + "' turi klaidingą tipą");
        }

        List<UserGroupEntity> allUserGroups = userGroupRepository.findAll();

        boolean groupWhichCanApproveDocumentTypeFound = false;
        boolean groupWhichCanApproveDocumentTypeAndHasUsersFound = false;

        //Check if to be submitted document type is already available to approve by some group. Otherwise, throw exception.
        for (UserGroupEntity group : allUserGroups) {
            if (!groupWhichCanApproveDocumentTypeAndHasUsersFound) // patikriname ar jau nebuvome surade reikiamos grupes
                if (group.getAvailableDocumentTypesToApprove().contains(type)) { // grupe turi tureti teise tvirtinti toki tipa
                    groupWhichCanApproveDocumentTypeFound = true;

                    if (group.getGroupUsers().size() > 0) // na ir butu gerai kad joje butu naudotoju
                    {
                        // radome grupe, kuri gali tvirtinti sio tipo dokumentus
                        // ir kurioje yra naudotoju
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
            auditService.addNewAuditEntry(user, AuditActionEnum.SUBMIT_DOCUMENT, ObjectTypeEnum.DOCUMENT, document.getDocumentIdentifier());
        }


    }


    @Transactional
    public void approveOrRejectDocument(String documentIdentifier,
                                        String username,
                                        DocumentState newState,
                                        String rejectedReason) throws IllegalArgumentException, SecurityException {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            LOGGER.debug("approveOrRejectDocument");
            //surandamas dokumentas
            DocumentEntity document = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            if (document == null) {
                throw new IllegalArgumentException("Dokumentas, kurio ID '" + documentIdentifier + "', nerastas");
            }

            //surandame prisiloginusi useri
            UserEntity user = userRepository.findUserByUsername(username);
            if (user == null) {
                throw new SecurityException("Naudotojas " + username + " nerastas");
            }

            // susirinkime visus tipus, kuriuos sis naudotojas gali tvirtinti:
            Set<DocumentTypeEntity> typesUserAllowedToApprove = new HashSet<DocumentTypeEntity>();


            // surenkame sarasa dokumentu tipu, kuriuos siam naudotojui leidziama tvirtinti
            for (UserGroupEntity userGroupEntity : user.getUserGroups()) {
                typesUserAllowedToApprove.addAll(userGroupEntity.getAvailableDocumentTypesToApprove());
            }


            // gaunam dokumento tipa kaip objekta
            DocumentTypeEntity type = documentTypeRepository.findDocumentTypeByTitle(document.getType());
            if (type == null) {
                throw new IllegalArgumentException("Documentas, kurio id '" + documentIdentifier + "', turi klaidingą tipą");
            }

            // jeigu tvirtinamo dokumento tipas yra leistinu tvirtinti sarase - tvirtinama.
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
                    auditService.addNewAuditEntry(user, AuditActionEnum.REJECT_DOCUMENT, ObjectTypeEnum.DOCUMENT, document.getDocumentIdentifier());
                    break;
                case APPROVED:
                    document.setDocumentState(newState);
                    document.setApprovalDate(LocalDateTime.now());
                    document.setApprover(user.getUsername());
                    documentRepository.save(document);
                    auditService.addNewAuditEntry(user, AuditActionEnum.APPROVE_DOCUMENT, ObjectTypeEnum.DOCUMENT, document.getDocumentIdentifier());
                    break;
                default:
                    throw new IllegalArgumentException("WRONG TYPE");
            }


        }

    }

//
//    @Transactional
//    public void updateDocument(String documentIdentifier, String title, String description, String type) {
//        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
//
//            DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
//            documentFromDatabase.setTitle(title);
//            documentFromDatabase.setDescription(description);
//            documentFromDatabase.setType(type);
//            documentRepository.save(documentFromDatabase);
//        }
//    }

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
        // adds file to document. the search was done by unique identifiers
        getDocumentEntityByDocumentIdentifier(documentIdentifier).addFileToDocument(fileEntity);
    }

    @Transactional
    public DocumentServiceObject getDocumentByDocumentIdentifier(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            LOGGER.debug("getDocumentByDocumentIdentifier");
            //converting from database object to normal one
            DocumentServiceObject documentServiceObject = SOfromEntity
                    (documentRepository.findDocumentByDocumentIdentifier(documentIdentifier));
            return documentServiceObject;
        } else {
            throw new IllegalArgumentException("no valid document identifier provided");
        }
    }

    @Transactional
    public DocumentEntity getDocumentEntityByDocumentIdentifier(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            LOGGER.debug("getDocumentEntityByDocumentIdentifier");
            //converting from database object to normal one
            DocumentEntity documentEntity =
                    documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            return documentEntity;
        } else {
            throw new IllegalArgumentException("no valid document identifier provided");
        }
    }

//        if (documentFromDatabase.getDocumentState().equals(DocumentState.CREATED)) {
//            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription());
//
//        } else if (documentFromDatabase.getDocumentState().equals(DocumentState.SUBMITTED)) {
//            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription(),
//                    documentFromDatabase.getPostedDate());
//        } else if (documentFromDatabase.getDocumentState().equals(DocumentState.APPROVED)) {
//            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription(),
//                    documentFromDatabase.getPostedDate(), documentFromDatabase.getApprovalDate(), documentFromDatabase.getApprover());
//
//        } else {
//            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription(),
//                    documentFromDatabase.getPostedDate(), documentFromDatabase.getApprover(), documentFromDatabase.getRejectedDate(),
//                    documentFromDatabase.getRejectionReason());
//
//        }

//    }

    @Transactional
    public void deleteDocument(String documentIdentifier) {
        LOGGER.debug("deleteDocument");
        DocumentEntity documentEntity = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentEntity.getDocumentState().equals(DocumentState.CREATED) ||
                documentEntity.getDocumentState().equals(DocumentState.REJECTED)) {
            documentRepository.deleteDocumentByDocumentIdentifier(documentIdentifier);

        }
    }

    public long getDocumentCount(String username)
    {
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
                .map(file -> new FileServiceObject(file.getFileName(), file.getContentType(), file.getSize(), file.getIdentifier()))
                .collect(Collectors.toSet()));
        return so;
    }

}