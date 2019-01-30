package it.akademija.documents.service;



import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;

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

    @Transactional

    public Set<DocumentServiceObject> getDocumentsByState(String userIdentifier, String state) {
        Set<DocumentEntity> documentsFromDatabase = userRepository.findDocumentsByUserIdentifier(userIdentifier);
        Set<DocumentEntity> documentsFromDatabaseWithState = new HashSet<>();
        for (DocumentEntity documentEntity : documentsFromDatabase) {
            if (documentEntity.getDocumentState().equals(state)) {
                documentsFromDatabaseWithState.add(documentEntity);
            }
        }


        if (state.equals(DocumentState.CREATED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription()))
                    .collect(Collectors.toSet());

        } else if (state.equals(DocumentState.SUBMITTED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate())).collect(Collectors.toSet());
        } else if (state.equals(DocumentState.APPROVED)) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate(), documentEntity.getApprovalDate(), documentEntity.getApprover()))
                    .collect(Collectors.toSet());

        } else {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate(), documentEntity.getApprover(), documentEntity.getRejectedDate(),

                            documentEntity.getRejectionReason())).collect(Collectors.toSet());

        }
    }

    @Transactional
    public Set<DocumentServiceObject> getAllUserDocuments(String userIdentifier) {
        Set<DocumentEntity> documentsFromDatabase = userRepository.findDocumentsByUserIdentifier(userIdentifier);
        return documentsFromDatabase.stream().map((documentEntity) ->
                new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                        documentEntity.getPostedDate(), documentEntity.getApprover(), documentEntity.getRejectedDate(),
                        documentEntity.getRejectionReason())).collect(Collectors.toSet());



    }

    @Transactional
    public void addDocument(String userIdentifier, String title, String type, String description) {
        //Pasiimam useri is DB
        UserEntity userFromDatabase = userRepository.findUserByUserIdentifier(userIdentifier);
        //Pasiimam grupes, kurioms jis yra priskirtas
        Set<UserGroupEntity> availableUserGroups=userFromDatabase.getUserGroups();

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

                    } else {
                        System.out.println("User doesn't belong to a group that can create that type's documents");
                    }
                }



            }
        }

    @Transactional
    public void updateDocument(String documentIdentifier, String title, String description, String type) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {

            DocumentEntity documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentFromDatabase.setTitle(title);
            documentFromDatabase.setDescription(description);
            documentFromDatabase.setType(type);
            documentRepository.save(documentFromDatabase);
        }
    }

    @Transactional
    public void submitDocument (String documentIdentifier) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);

            documentEntityFromDatabase.setDocumentState(DocumentState.SUBMITTED);

            LocalDateTime datePosted= LocalDateTime.now();
            documentEntityFromDatabase.setPostedDate(datePosted);
            documentRepository.save(documentEntityFromDatabase);
        }
    }

    @Transactional
    public void approveDocument (String documentIdentifier) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);

            documentEntityFromDatabase.setDocumentState(DocumentState.APPROVED);
            LocalDateTime dateApproved= LocalDateTime.now();
            documentEntityFromDatabase.setApprovalDate(dateApproved);


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
}




