package it.akademija.documents.service;


import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.service.FileServiceObject;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;

import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import it.akademija.users.service.UserServiceObject;
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


    @Transactional

    public Set<DocumentServiceObject> getDocumentsByState(String userIdentifier, DocumentState state) throws IllegalArgumentException {
        try {
            UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
            //kai ekspermentavau, nemeta exceptiono tol kol user ententyje nepabandai kazko ieskot, net
            // jei tokio userio nera, jokio crasho/error nebus tol kol su useriu kazko nepadarai
            userEntity.getDocumentEntities();
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("When trying to get document using" +
                    "UserIdentifier  database returns null," +
                    "either user identifier is not recognised or user has no documents attached");

        }
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        Set<DocumentEntity> documentsFromDatabase = userEntity.getDocumentEntities();
        Set<DocumentEntity> documentsFromDatabaseWithState = new HashSet<>();
        for (DocumentEntity documentEntity : documentsFromDatabase) {
            if (documentEntity.getDocumentState() == state) {
                documentsFromDatabaseWithState.add(documentEntity);
            }
        }

        if (state.equals(DocumentState.CREATED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getDocumentIdentifier(), documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription()))
                    .collect(Collectors.toSet());

        } else if (state.equals(DocumentState.SUBMITTED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getDocumentIdentifier(), documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate())).collect(Collectors.toSet());
        } else if (state.equals(DocumentState.APPROVED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getDocumentIdentifier(), documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate(), documentEntity.getApprovalDate(), documentEntity.getApprover()))
                    .collect(Collectors.toSet());

        } else if (state.equals(DocumentState.REJECTED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getDocumentIdentifier(), documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate(), documentEntity.getApprover(), documentEntity.getRejectedDate(),
                            documentEntity.getRejectionReason())).collect(Collectors.toSet());

        }
        // does not actually work, as it crashes on controller if wrong state is used
        throw new IllegalArgumentException("Most likely you used wrong document state is used" +
                ", we can only handle CREATED, SUBMITTED, APPROVED and REJECTED");
    }

    @Transactional
    public Set<DocumentServiceObject> getAllUserDocuments(String userIdentifier) {

        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        Set<DocumentEntity> documentsFromDatabase = userEntity.getDocumentEntities();

        return documentsFromDatabase.stream().map((documentEntity) ->
                new DocumentServiceObject(documentEntity.getDocumentIdentifier(),
                        documentEntity.getAuthor(),
                        documentEntity.getTitle(),
                        documentEntity.getType(),
                        documentEntity.getDocumentState(),
                        documentEntity.getDescription(),
                        documentEntity.getPostedDate(),
                        documentEntity.getApprovalDate(),
                        documentEntity.getRejectedDate(),
                        documentEntity.getRejectionReason(),
                        documentEntity.getApprover())).collect(Collectors.toSet());
    }

    @Transactional
    public DocumentEntity addDocument(String userIdentifier, String title, String type, String description) {
        //Pasiimam useri is DB
        UserEntity userFromDatabase = userRepository.findUserByUserIdentifier(userIdentifier);
        //Pasiimam grupes, kurioms jis yra priskirtas
        Set<UserGroupEntity> availableUserGroups = userFromDatabase.getUserGroups();

        //Pereinam per tas grupes ir tikrinam
        for (UserGroupEntity userGroup : availableUserGroups) {
            //Pasiimam kiekvienos grupes galimus kurti failus
            Set<DocumentTypeEntity> groupAvailableDocumentTypes = userGroup.getAvailableDocumentTypesToUpload();
            //Einam per kiekviena is tu tipu ir tikrinam, ar jis atitinka norimo sukurti dokumento
            for (DocumentTypeEntity documentTypeEntity : groupAvailableDocumentTypes) {
                if (documentTypeEntity.getTitle().equals(type)) {
//                    if (userFromDatabase.getUsername() != null) {
                    DocumentEntity documentEntity = new DocumentEntity(title, description, type);
                    documentEntity.setAuthor(userFromDatabase.getUsername());
                    userFromDatabase.addDocument(documentEntity);
                    documentRepository.save(documentEntity);
                    return documentEntity;


                } else {
                    System.out.println("User doesn't belong to a group that can create that type's documents");
                }

            }

        }
        return null;

    }

    @Transactional
    public void updateDocument(String documentIdentifier, String title, String description, String type) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {

            DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentFromDatabase.setTitle(title);
            documentFromDatabase.setDescription(description);
            documentFromDatabase.setType(type);
            documentRepository.save(documentFromDatabase);
        }
    }

    @Transactional
    public void submitDocument(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            sendSubmittedDocumentToApprove(documentEntityFromDatabase);
            documentEntityFromDatabase.setDocumentState(DocumentState.SUBMITTED);
            LocalDateTime datePosted = LocalDateTime.now();
            documentEntityFromDatabase.setPostedDate(datePosted);
            documentRepository.save(documentEntityFromDatabase);
        }
    }

    @Transactional
    private void sendSubmittedDocumentToApprove(DocumentEntity documentEntity) {
        List<UserGroupEntity> availableUserGroupsToGetSubmittedDoc = userGroupRepository.findAll();
        for (UserGroupEntity userGroupEntity : availableUserGroupsToGetSubmittedDoc) {
            for (DocumentTypeEntity documentTypeEntity : userGroupEntity.getAvailableDocumentTypesToApprove()) {
                if (documentEntity.getType().equals(documentTypeEntity.getTitle())) {
                    userGroupEntity.getDocumentsToApprove().add(documentEntity);
                }
            }
        }
    }

    @Transactional
    public void approveDocument(String documentIdentifier, String userIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            //surandamas dokumentas
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            //surandame prisiloginusi useri
            UserEntity userEntityFromDataBase = userRepository.findUserByUserIdentifier(userIdentifier);

            for (UserGroupEntity userGroupEntity : userEntityFromDataBase.getUserGroups()) {
                if (userGroupEntity.getDocumentsToApprove().contains(documentEntityFromDatabase)) {
                    LocalDateTime dateApproved = LocalDateTime.now();
                    documentEntityFromDatabase.setDocumentState(DocumentState.APPROVED);
                    documentEntityFromDatabase.setApprovalDate(dateApproved);
                    documentEntityFromDatabase.setApprover(userEntityFromDataBase.getFirstname() + " " + userEntityFromDataBase.getLastname());
                    documentRepository.save(documentEntityFromDatabase);
                    removeDocFromApproverList(documentIdentifier);

                }
            }
        }
    }

    @Transactional
    public void rejectDocument(String documentIdentifier, String userIdentifier, String rejectedReason) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            UserEntity userEntityFromDataBase = userRepository.findUserByUserIdentifier(userIdentifier);

            for (UserGroupEntity userGroupEntity : userEntityFromDataBase.getUserGroups()) {
                if (userGroupEntity.getDocumentsToApprove().contains(documentEntityFromDatabase)) {

                    LocalDateTime dateRejected = LocalDateTime.now();
                    documentEntityFromDatabase.setDocumentState(DocumentState.REJECTED);
                    documentEntityFromDatabase.setRejectedDate(dateRejected);
                    documentEntityFromDatabase.setApprover(userEntityFromDataBase.getFirstname() + " " + userEntityFromDataBase.getLastname());
                    documentEntityFromDatabase.setRejectionReason(rejectedReason);
                    documentRepository.save(documentEntityFromDatabase);
                    removeDocFromApproverList(documentIdentifier);
                }
            }

        }
    }
/*dokumentas su jo pasirasusiu specialistu issaugotas, dabar patvirtinta dokumenta reiktu pasalinti is
specialisto Dokumento saraso*/

    @Transactional
    private void removeDocFromApproverList(String documentIdentifier) {
        List<UserGroupEntity> availableUserGroups = userGroupRepository.findAll();
        for (UserGroupEntity userGroupEntity : availableUserGroups) {
            Set<DocumentEntity> allDocsToApproveFromGroup = userGroupEntity.getDocumentsToApprove();
            for (DocumentEntity documentEntity : allDocsToApproveFromGroup) {
                if (documentEntity.getDocumentIdentifier().equals(documentIdentifier)) {
                    allDocsToApproveFromGroup.remove(documentEntity);
                }
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
        // adds file to document. the search was done by unique identifiers
        getDocumentEntityByDocumentIdentifier(documentIdentifier).addFileToDocument(fileEntity);
    }


    @Transactional
    public DocumentServiceObject getDocumentByDocumentIdentifier(String documentIdentifier) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            //converting from database object to normal one
            DocumentServiceObject documentServiceObject = convertDocumentEntityToObject
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

    // you can delete or move this. I was just thinking it might be cool to have one method for conversion
    // less code to maintain
    @Transactional
    private DocumentServiceObject convertDocumentEntityToObject(DocumentEntity documentFromDatabase) {
        DocumentServiceObject documentServiceObject = new DocumentServiceObject();
        documentServiceObject.setTitle(documentFromDatabase.getTitle());
        documentServiceObject.setDescription(documentFromDatabase.getDescription());
        documentServiceObject.setType(documentFromDatabase.getType());
        documentServiceObject.setFilesAttachedToDocument(documentFromDatabase.getFileSet());
        return documentServiceObject;
    }

    @Transactional
    public DocumentServiceObject getDocument(String documentIdentifier) {
        DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentFromDatabase.getDocumentState().equals(DocumentState.CREATED)) {
            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription());

        } else if (documentFromDatabase.getDocumentState().equals(DocumentState.SUBMITTED)) {
            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription(),
                    documentFromDatabase.getPostedDate());
        } else if (documentFromDatabase.getDocumentState().equals(DocumentState.APPROVED)) {
            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription(),
                    documentFromDatabase.getPostedDate(), documentFromDatabase.getApprovalDate(), documentFromDatabase.getApprover());

        } else {
            return new DocumentServiceObject(documentFromDatabase.getDocumentIdentifier(), documentFromDatabase.getTitle(), documentFromDatabase.getType(), documentFromDatabase.getDescription(),
                    documentFromDatabase.getPostedDate(), documentFromDatabase.getApprover(), documentFromDatabase.getRejectedDate(),
                    documentFromDatabase.getRejectionReason());

        }

    }

    @Transactional
    public void deleteDocument(String documentIdentifier) {
        DocumentEntity documentEntity = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        if (documentEntity.getDocumentState().equals(DocumentState.CREATED) ||
                documentEntity.getDocumentState().equals(DocumentState.REJECTED)) {
            documentRepository.deleteDocumentByDocumentIdentifier(documentIdentifier);
        }
    }
}




