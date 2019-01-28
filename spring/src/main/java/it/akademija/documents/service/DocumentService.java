package it.akademija.documents.service;


import it.akademija.documents.repository.Document;
import it.akademija.documents.repository.DocumentRepository;
//import it.akademija.users.repository.User;
//import it.akademija.users.repository.UserRepository;
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
        List<Document> documentsFromDatabase = userRepository.findDocumentsByUserIdentifier(userIdentifier);
        List<Document> documentsFromDatabaseWithState = new ArrayList<>();
        for (Document document : documentsFromDatabase) {
            if (document.getState().equals(state)) {
                documentsFromDatabaseWithState.add(document);

            }
        }

        if (state.equals("created")) {
            return documentsFromDatabaseWithState.stream().map((document) ->
                    new DocumentServiceObject(document.getTitle(), document.getType(), document.getDescription()))
                    .collect(Collectors.toList());

        } else if (state.equals("submitted")) {
            return documentsFromDatabaseWithState.stream().map((document) ->
                    new DocumentServiceObject(document.getTitle(), document.getType(), document.getDescription(),
                            document.getPostedDate())).collect(Collectors.toList());
        } else if (state.equals("approved")) {
            return documentsFromDatabaseWithState.stream().map((document) ->
                    new DocumentServiceObject(document.getTitle(), document.getType(), document.getDescription(),
                            document.getPostedDate(), document.getApprovalDate(), document.getApprover()))
                    .collect(Collectors.toList());
        } else {
            return documentsFromDatabaseWithState.stream().map((document) ->
                    new DocumentServiceObject(document.getTitle(), document.getType(), document.getDescription(),
                            document.getPostedDate(), document.getApprover(), document.getRejectedDate(),
                            document.getRejectionReason())).collect(Collectors.toList());
        }
    }

    @Transactional
    public void addDocument(String userIdentifier, String title, String type, String description) {

        User userFromDatabase = userRepository.findUserByUserIdentifier(userIdentifier);

        if (userFromDatabase.getUsername() != null) {
            Document document = new Document(title, description, type);
            document.setAuthor(userFromDatabase.getUsername());

            documentRepository.save(document);
            userFromDatabase.addDocument(document);
        }

        //patikriniam ar pagal userid surasto autoriaus name nera null ir kuriant nauja dokumenta, prisetinam name

    }

    @Transactional
    public void updateDocument(String documentIdentifier, String title, String description, String type) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            Document documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentFromDatabase.setTitle(title);
            documentFromDatabase.setDescription(description);
            documentFromDatabase.setType(type);

            documentRepository.save(documentFromDatabase);
        }
    }

    @Transactional
    public void submitDocument (String documentIdentifier) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            Document documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentFromDatabase.setState("submitted");
            LocalDateTime datePosted= LocalDateTime.now();
            documentFromDatabase.setPostedDate(datePosted);
            documentRepository.save(documentFromDatabase);
        }
    }

    @Transactional
    public void approveDocument (String documentIdentifier) {
        if (!documentIdentifier.isEmpty() && documentIdentifier!=null) {
            Document documentFromDatabase = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
            documentFromDatabase.setState("approved");
            LocalDateTime dateApproved= LocalDateTime.now();
            documentFromDatabase.setPostedDate(dateApproved);

            }
            }
        }


