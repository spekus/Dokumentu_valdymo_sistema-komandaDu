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
        //makes new users and documents for them
        for(int counter =10000; counter > 0; counter--){
            String uniqueString = "";
            uniqueString = userInput +uniqueNumber;
            uniqueNumber++;

            //documentTypeRepository.putDummyDocumentTypes(userInput+counter);
            makeUser(uniqueString);
            //make documents for user which is just created
            makeDocuments(uniqueString);

        }
        //makes documents for specific user
        for(int counter =10; counter > 0; counter--) {
            makeDocuments(author);
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

        for(int counter =500; counter > 0; counter--){
            String documentTypeTitle = title+counter;
            documentTypeRepository.putDummyDocumentTypes(documentTypeTitle);

        }

    }

    @Transactional
    private void makeDocuments(String author){
//        counter2 = counter2 *10000;
        System.out.println("runnung makeDocuments");
        Faker faker = new Faker();
        for(int counter =20; counter > 0; counter--){
            String title = faker.ancient().primordial();
            String description = faker.chuckNorris().fact();
            System.out.println("runnung makeDocuments loop");
            String DocumentIdentifier = "";
            DocumentIdentifier = "docID" + uniqueNumber;
            //String modifiedDocumentName= documentName + uniqueNumber;
            uniqueNumber++;

            //keeps it unique
            //increment();
            if(counter%2 == 0){
                documentRepository.putDummyDocumentTypes(DocumentIdentifier, title, description, author, "CREATED");
            }
            else{
                documentRepository.putDummyDocumentTypes(DocumentIdentifier, title, description, author, "SUBMITTED");
            }

        }



    }


}
