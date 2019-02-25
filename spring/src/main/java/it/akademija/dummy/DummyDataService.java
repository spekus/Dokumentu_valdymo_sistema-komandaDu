package it.akademija.dummy;

import com.github.javafaker.Faker;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentTypeService;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DummyDataService {

    private static int uniqueNumber = 22340;
    public String author = "id123";
//    public void increment()
//    {
//        skaicius++;
//    }

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void augisTestuoja(String userInput) {
        for(int counter =10; counter > 0; counter--){
            String uniqueString = "";
            uniqueString = userInput +uniqueNumber;
            uniqueNumber++;
            makeDocuments("testas1000", userInput, counter, author );
            documentTypeRepository.putDummyDocumentTypes(userInput+counter);
            makeUser(uniqueString);

        }
        //documentTypeRepository.putTestData(title);
    }

    private void makeUser(String uniqueString) {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String surename = faker.name().lastName();
        userRepository.makeDummyUsers(uniqueString,name, surename);
    }

    @Transactional
    private void makeDocumentTypes(String title){

        for(int counter =50000; counter > 0; counter--){
            String documentTypeTitle = title+counter;
            documentTypeRepository.putDummyDocumentTypes(documentTypeTitle);

        }

    }

    @Transactional
    private void makeDocuments(String DocumentType, String documentName, Integer counter2, String author){
//        counter2 = counter2 *10000;
        System.out.println("runnung makeDocuments");
        for(int counter =50; counter > 0; counter--){
            System.out.println("runnung makeDocuments loop");
            String DocumentIdentifier = "";
            DocumentIdentifier = "docID" + uniqueNumber;
            String modifiedDocumentName= documentName + uniqueNumber;
            uniqueNumber++;

            //keeps it unique
            //increment();

            documentRepository.putDummyDocumentTypes(DocumentIdentifier, modifiedDocumentName, author, "CREATED");
        }
        for(int counter =50; counter > 0; counter--){
            System.out.println("runnung makeDocuments loop");
            String DocumentIdentifier = "";
            DocumentIdentifier = "docID" + uniqueNumber;
            String modifiedDocumentName= documentName + uniqueNumber;
            uniqueNumber++;

            //keeps it unique
            //increment();

            documentRepository.putDummyDocumentTypes(DocumentIdentifier, modifiedDocumentName, author, "SUBMITTED");
        }


    }


}
