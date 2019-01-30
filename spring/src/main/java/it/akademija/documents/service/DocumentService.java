package it.akademija.documents.service;


import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
//import it.akademija.users.repository.UserEntity;
//import it.akademija.users.repository.UserRepository;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<DocumentServiceObject> getDocuments(String userIdentifier, String state) {
        List<DocumentEntity> documentsFromDatabase = userRepository.findDocumentsByUserIdentifier(userIdentifier);
        List<DocumentEntity> documentsFromDatabaseWithState = new ArrayList<>();
        for (DocumentEntity documentEntity : documentsFromDatabase) {
            if (documentEntity.getState().equals(state)) {
                documentsFromDatabaseWithState.add(documentEntity);

            }
        }

        if (state.equals("created")) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription()))
                    .collect(Collectors.toList());

        } else if (state.equals("submitted")) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate())).collect(Collectors.toList());
        } else if (state.equals("approved")) {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate(), documentEntity.getApprovalDate(), documentEntity.getApprover()))
                    .collect(Collectors.toList());
        } else {
            return documentsFromDatabaseWithState.stream().map((documentEntity) ->
                    new DocumentServiceObject(documentEntity.getTitle(), documentEntity.getType(), documentEntity.getDescription(),
                            documentEntity.getPostedDate(), documentEntity.getApprover(), documentEntity.getRejectedDate(),
                            documentEntity.getRejectionReason())).collect(Collectors.toList());
        }
    }

    @Transactional
    public void addDocument(String userIdentifier, String title, String type, String description) {

        UserEntity userEntityFromDatabase = userRepository.findUserByUserIdentifier(userIdentifier);

        if (userEntityFromDatabase.getUsername() != null) {
            DocumentEntity documentEntity = new DocumentEntity(title, description, type);
            documentEntity.setAuthor(userEntityFromDatabase.getUsername());

            documentRepository.save(documentEntity);
            userEntityFromDatabase.addDocument(documentEntity);
        }

        //patikriniam ar pagal userid surasto autoriaus name nera null ir kuriant nauja dokumenta, prisetinam name

    }

    @Transactional
    public void updateDocument(String documentIdentifier, String title, String description, String type) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentEntityFromDatabase.setTitle(title);
            documentEntityFromDatabase.setDescription(description);
            documentEntityFromDatabase.setType(type);

            documentRepository.save(documentEntityFromDatabase);
        }
    }

    @Transactional
    public void submitDocument (String documentIdentifier) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentEntityFromDatabase.setState("submitted");
            LocalDateTime datePosted= LocalDateTime.now();
            documentEntityFromDatabase.setPostedDate(datePosted);
            documentRepository.save(documentEntityFromDatabase);
        }
    }

    @Transactional
    public void approveDocument (String documentIdentifier) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            DocumentEntity documentEntityFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentEntityFromDatabase.setState("approved");
            LocalDateTime dateApproved= LocalDateTime.now();
            documentEntityFromDatabase.setPostedDate(dateApproved);

            }
            }
        }


