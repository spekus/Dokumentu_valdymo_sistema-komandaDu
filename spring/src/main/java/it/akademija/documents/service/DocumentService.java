package it.akademija.documents.service;


import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.service.FileServiceObject;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;

import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.HashSet;

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

    @Transactional
    public Set<DocumentServiceObject> getDocumentsByState(DocumentState state) {
        return documentRepository.findByDocumentState(state)
                .stream()
                .map(documentEntity -> SOfromEntity(documentEntity))
                .collect(Collectors.toSet());
    }

    @Transactional
    public DocumentServiceObject getDocument(String documentIdentifier) {
        DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentFromDatabase == null) {
            throw new IllegalArgumentException("Dokuments su id '" + documentIdentifier + "'nerastas");
        }
        return SOfromEntity(documentFromDatabase);
    }


    @Transactional
    public DocumentEntity createDocument(String username, String title, String type, String description)
            throws IllegalArgumentException, SecurityException{
        //Pasiimam useri is DB
        UserEntity user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new SecurityException("Naudotojas " + username + " nerastas");
        }
        // susirinkime visus tipus, kuriuos sis naudotojas gali ikelti
        Set<DocumentTypeEntity> typesUserAllowedToUpload = new HashSet<DocumentTypeEntity>();

        // surenkame sarasa dokumentu tipu, kuriuos siam naudotojui leidziama tvirtinti
        for (UserGroupEntity userGroupEntity : user.getUserGroups()) {
            typesUserAllowedToUpload.addAll(userGroupEntity.getAvailableDocumentTypesToUpload());
        }

        // patikriname ar sis tipas yra tarp leidziamu kurti
        if (!typesUserAllowedToUpload.stream()
                .map(t -> t.getTitle()).collect(Collectors.toSet())
                .contains(type)) {
            throw new SecurityException("Jums negalima kurtio '" + type +  "' tipo dokumento !");
        }


        DocumentEntity newDocument = new DocumentEntity(title, description, type);
        newDocument.setAuthor(user.getUsername());
        user.addDocument(newDocument);
        documentRepository.save(newDocument);
        return newDocument;
    }

    @Transactional
    public void submitDocument(String documentIdentifier) throws IllegalArgumentException {
        DocumentEntity document = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);

        if (document == null) {
            throw new IllegalArgumentException("Documentas su id '" + documentIdentifier + "' nerastas");
        }

        document.setDocumentState(DocumentState.SUBMITTED);
        document.setPostedDate(LocalDateTime.now());
        documentRepository.save(document);

    }

    @Transactional
    public void approveOrRejectDocument(String documentIdentifier,
                                        String username,
                                        DocumentState newState,
                                        String rejectedReason) throws IllegalArgumentException, SecurityException {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            //surandamas dokumentas
            DocumentEntity document = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            if (document == null) {
                throw new IllegalArgumentException("Dokumentas su ID '" + documentIdentifier + "' nerastas");
            }

            //surandame prisiloginusi useri
            UserEntity user = userRepository.findUserByUsername(username);
            if (user == null) {
                throw new SecurityException("Naudotojas " + username + " nerastas");
            }

            // susirinkime visus tipus, kuriuos sis naudotojas gali tvirtinti
            Set<DocumentTypeEntity> typesUserAllowedToApprove = new HashSet<DocumentTypeEntity>();

            // surenkame sarasa dokumentu tipu, kuriuos siam naudotojui leidziama tvirtinti
            for (UserGroupEntity userGroupEntity : user.getUserGroups()) {
                typesUserAllowedToApprove.addAll(userGroupEntity.getAvailableDocumentTypesToApprove());
            }

            // jeigu tvirtinamo dokumento tipas yra leistinu tvirtinti sarase - tvirtima.
            // reikalinga tokia stream-map-collect konstrukcija nes mes susirinkome "DocumentTypeEntity"
            // o lyginam ir tikrinam ar ten yra Stringas, nes document.getType()
            if (!typesUserAllowedToApprove.stream()
                    .map(type -> type.getTitle()).collect(Collectors.toSet())
                    .contains(document.getType())) {
                throw new IllegalArgumentException("Jums negalima tvirtintio šio dokumento !");
            }

            switch (newState) {
                case REJECTED:
                    document.setDocumentState(newState);
                    document.setRejectedDate(LocalDateTime.now());
                    document.setApprover(user.getFirstname() + " " + user.getLastname());
                    document.setRejectionReason(rejectedReason);
                    documentRepository.save(document);
                    break;
                case APPROVED:
                    document.setDocumentState(newState);
                    document.setApprovalDate(LocalDateTime.now());
                    document.setApprover(user.getFirstname() + " " + user.getLastname());
                    documentRepository.save(document);
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
        // adds file to document. the search was done by unique identifiers
        getDocumentEntityByDocumentIdentifier(documentIdentifier).addFileToDocument(fileEntity);
    }

    @Transactional
    public DocumentServiceObject getDocumentByDocumentIdentifier(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
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
        DocumentEntity documentEntity = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentEntity.getDocumentState().equals(DocumentState.CREATED) ||
                documentEntity.getDocumentState().equals(DocumentState.REJECTED)) {
            documentRepository.deleteDocumentByDocumentIdentifier(documentIdentifier);

        }
    }

    private DocumentServiceObject SOfromEntity(DocumentEntity entity) {
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




