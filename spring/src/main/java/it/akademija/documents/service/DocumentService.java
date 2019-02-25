package it.akademija.documents.service;


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

import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import it.akademija.users.service.UserServiceObject;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentService{

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
    public DocumentEntity createDocument(String username, String title, String type, String description) {
        //Pasiimam useri is DB
        UserEntity userFromDatabase = userRepository.findUserByUsername(username);
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
    public void submitDocument(String documentIdentifier) throws NoApproverAvailableException {

        boolean alreadyExecuted=false;

        List<UserGroupEntity> allUserGroups = userGroupRepository.findAll();
        DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
        DocumentTypeEntity type = documentTypeRepository.findDocumentTypeByTitle(documentEntityFromDatabase.getType());
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {

            //Check if to be submitted document type is already available to approve by some group. Otherwise, throw exception.
            for (UserGroupEntity group : allUserGroups) {
                if (group.getAvailableDocumentTypesToApprove().contains(type) && !alreadyExecuted) {
                    sendSubmittedDocumentToApprove(documentEntityFromDatabase);
                    documentEntityFromDatabase.setDocumentState(DocumentState.SUBMITTED);
                    LocalDateTime datePosted = LocalDateTime.now();
                    documentEntityFromDatabase.setPostedDate(datePosted);
                    documentRepository.save(documentEntityFromDatabase);
                    alreadyExecuted=true;

                }
            }

            if (!alreadyExecuted) {
                throw new NoApproverAvailableException("Document cannot be submitted until there's a group that" +
                        " can approve " + documentEntityFromDatabase.getType());
            }

        }
    }

    @Transactional
    public void approveDocument(String documentIdentifier, String username) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            //surandamas dokumentas
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            //surandame prisiloginusi useri
            UserEntity userEntityFromDataBase = userRepository.findUserByUsername(username);
            boolean atisismt = false;
            for (UserGroupEntity userGroupEntity : userEntityFromDataBase.getUserGroups()) {
                if (userGroupEntity.getDocumentsToApprove().contains(documentEntityFromDatabase)) {
                    LocalDateTime dateApproved = LocalDateTime.now();
                    documentEntityFromDatabase.setDocumentState(DocumentState.APPROVED);
                    documentEntityFromDatabase.setApprovalDate(dateApproved);
                    documentEntityFromDatabase.setApprover(userEntityFromDataBase.getFirstname() + " " + userEntityFromDataBase.getLastname());
                    documentRepository.save(documentEntityFromDatabase);
                     atisismt = true;


                }
            }
            if(atisismt == true){
                removeDocFromApproverList(documentIdentifier);
            }
        }
    }

    @Transactional
    public void rejectDocument(String documentIdentifier, String username, String rejectedReason) {
        if (documentIdentifier != null && !documentIdentifier.isEmpty()) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            UserEntity userEntityFromDataBase = userRepository.findUserByUsername(username);

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




/*dokumentas su jo pasirasusiu specialistu issaugotas, dabar patvirtinta dokumenta reiktu pasalinti is
specialisto Dokumento saraso*/

    @Transactional
    private void removeDocFromApproverList(String documentIdentifier) {
        boolean remove = false;
        DocumentEntity documentEntityToRemove = null;
        List<UserGroupEntity> availableUserGroups = userGroupRepository.findAll();
        for (UserGroupEntity userGroupEntity : availableUserGroups) {
            Set<DocumentEntity> allDocsToApproveFromGroup = userGroupEntity.getDocumentsToApprove();
            for (DocumentEntity documentEntity : allDocsToApproveFromGroup) {
                if (documentEntity.getDocumentIdentifier().equals(documentIdentifier)) {
                    remove =true;
                    documentEntityToRemove = documentEntity;

                }


            }
            if(remove == true){
                allDocsToApproveFromGroup.remove(documentEntityToRemove);
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




