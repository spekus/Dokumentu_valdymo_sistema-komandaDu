package it.akademija;

import com.github.javafaker.Faker;
import it.akademija.auth.AppRoleEnum;
import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentTypeService;

import it.akademija.exceptions.NoApproverAvailableException;
import it.akademija.users.controller.CreateUserGroupCommand;

import it.akademija.files.service.FileService;

import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import it.akademija.users.service.UserGroupService;
import it.akademija.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DemoData implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserGroupRepository userGroupRepository;

    @Autowired
    private final UserGroupService userGroupService;

    @Autowired
    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    private final DocumentTypeService documentTypeService;

    @Autowired
    private  DocumentService documentService;
    @Autowired
    private FileService fileService;

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public DemoData(UserRepository userRepository, UserService userService, UserGroupRepository userGroupRepository, UserGroupService userGroupService, DocumentTypeRepository documentTypeRepository, DocumentTypeService documentTypeService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userGroupRepository = userGroupRepository;
        this.userGroupService = userGroupService;
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeService = documentTypeService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        createUserGroupIfNotExists("Administratoriai",AppRoleEnum.ROLE_ADMIN);
        createUserGroupIfNotExists("Buhalteriai",AppRoleEnum.ROLE_USER);
        createUserGroupIfNotExists("Vadybininkai",AppRoleEnum.ROLE_USER);
        createUserGroupIfNotExists("Vadovai",AppRoleEnum.ROLE_USER);
        createUserGroupIfNotExists("Suspenduoti vartotojai", AppRoleEnum.ROLE_SUSPENDED);

        createDocumentTypeIfNotExists("Paraiška");
        createDocumentTypeIfNotExists("Paraiška2");
        createDocumentTypeIfNotExists("Paraiška3");
        createDocumentTypeIfNotExists("Paraiška4");
        createDocumentTypeIfNotExists("Paraiška5");
        createDocumentTypeIfNotExists("Darbo sutartis");
        createDocumentTypeIfNotExists("Registruotas laiškas");
        createDocumentTypeIfNotExists("Receptas");

        createUserIfNotExists("Administrator", "IT", "admin", "admin");
        createUserIfNotExists("Augustas", "Dirzys", "id123", "id123");
        createUserIfNotExists( "Anna", "Paidem", "annpai", "annpai");
        createUserIfNotExists( "User", "Vienas", "user1", "user1");
        createUserIfNotExists( "User", "Du", "user2", "user2");


        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Darbo sutartis","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Registruotas laiškas","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Receptas","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška2","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška3","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška4","");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška5","");


        userGroupService.addGroupToUser("Administratoriai","admin","");
        userGroupService.addGroupToUser("Administratoriai","id123","");
        userGroupService.addGroupToUser("Vadovai","id123","");
        userGroupService.addGroupToUser("Vadybininkai","annpai","");
        userGroupService.addGroupToUser("Vadybininkai","user1","");
        userGroupService.addGroupToUser("Vadybininkai","user2","");


        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška","");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška2","");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška3","");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška4","");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška5","");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Darbo sutartis","");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Registruotas laiškas","");



        userGroupService.addDocumentTypeToApprove("Administratoriai", "Paraiška","");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Paraiška2","");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Paraiška3","");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Paraiška4","");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Paraiška5","");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Darbo sutartis","");
        userGroupService.addDocumentTypeToApprove( "Vadovai","Registruotas laiškas","");
//        addDummydata2();
        createUserIfNotExists( "dummy", "dummy", "dummy", "dummy");


    }

    private void createUserIfNotExists(String fn, String ln, String un, String pswd) {
        UserEntity u = userRepository.findUserByUsername(un);

        if (u == null) {
            userService.createNewUser(fn, ln, un, pswd,"");
        }
    }

    private void createUserGroupIfNotExists(String title, AppRoleEnum role){
        UserGroupEntity uge = userGroupRepository.findGroupByTitle(title);

        if (uge == null) {
            userGroupService.addNewUserGroup(new CreateUserGroupCommand(title,role),"");
        }
    }
    private void createDocumentTypeIfNotExists(String title) {
        DocumentTypeEntity dte = documentTypeRepository.findDocumentTypeByTitle(title);

        if (dte == null) {
            documentTypeService.createNewDocumentType(title, "");
        }
    }

    private DocumentEntity addDocumentToUser(String userName, int number){
        //    DocumentEntity documentEntity =
//            documentService.createDocument(authentication.getName(), p.getTitle(), p.getType(), p.getDescription());
        DocumentEntity documentEntity =
                documentService.createDocument(userName, "title" + number, "Paraiška",
                        "description" + number);
//                fileService.addFileToDocument("fileID14-Konsultacija.pdf4.908534818622955E81"
//                , documentEntity.getDocumentIdentifier());
        return documentEntity;

    }


//    private void addDummydata() throws NoApproverAvailableException {
//        //patikrina ar jau buvo prideta data
//        ArrayList documentsForAprooval = new ArrayList();
//        if(userRepository.findUserByUsername("testuser1") ==  null) {
//
//            // jei dar neiko nebuvo prideta  x skaiciu useriu
//            for (int userNumber = 0; userNumber < 10; userNumber++) {
//                createUserIfNotExists("name" + userNumber, "surename" + userNumber,
//                        "testuser" + userNumber, "testuser" + userNumber);
//                userGroupService.addGroupToUser("Vadybininkai", "testuser" + userNumber);
//
//                //kiekvienam useriui pridada po tusciu dokumentu
//                for (int documentNumber = 0; documentNumber < 25; documentNumber++) {
//                    DocumentEntity documentEntity =addDocumentToUser("testuser" + userNumber, documentNumber);
//                    //submits part of documents
//                    if(documentNumber>5 && documentNumber <20){
//                        documentService.submitDocument(documentEntity.getDocumentIdentifier());
////                        documentEntity.setDocumentState(DocumentState.SUBMITTED);
////                        documentEntity.setPostedDate(LocalDateTime.now());
////                        documentRepository.save(documents);
//                    }
//                    //document aprooval
//                    if(documentNumber>5 && documentNumber <8) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "admin",
//                                DocumentState.APPROVED, "");
//                    }
//                    if(documentNumber>8 && documentNumber <10) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "id123",
//                                DocumentState.APPROVED, "");
//                    }
//                    if(documentNumber>10 && documentNumber <12) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "id123",
//                                DocumentState.REJECTED, "not valid");
//                    }
//                    if(documentNumber>12 && documentNumber <14) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "admin",
//                                DocumentState.REJECTED, "not in a mood approvals");
//                    }
//
//                }
//            }
//        }



//    }
    private void addDummydata2() throws NoApproverAvailableException {
        //for generating random realistic fiels
        Faker faker = new Faker();
        //setting user group
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle("Vadybininkai");
        // document types to be used for generation
        List<String> documentTypes = new ArrayList<>( Arrays.asList(
                "Paraiška", "Paraiška2", "Paraiška3", "Paraiška4", "Paraiška5"));
        Set<UserGroupEntity> allUserGroups = new HashSet<>();
        allUserGroups.add(userGroupEntity);
        //for random integer generations
        Random randomGenerator = new Random();
        if(userRepository.findUserByUsername("dummy") ==  null) {

            // jei dar neiko nebuvo prideta  x skaiciu useriu
            for (int userNumber = 0; userNumber < 10000; userNumber++) {
                Set<DocumentEntity> documentSet = new HashSet<>();
//                String userName =  "testuser" + userNumber;
                String firstname = faker.name().firstName();
                String lastname = faker.name().lastName();
                String userName = firstname + userNumber;
                //creating user
                UserEntity userEntity = new UserEntity(firstname, lastname, userName, passwordEncoder.encode(userName));
                //adding user group vadybininkas
                userEntity.setUserGroups(allUserGroups);
                userRepository.save(userEntity);

//                createUserIfNotExists("name" + userNumber, "surename" + userNumber,
//                        userName, userName);
//                userGroupService.addGroupToUser("Vadybininkai", userName);

                //kiekvienam useriui pridada po tusciu dokumentu
                for (int documentNumber = 0; documentNumber < 30; documentNumber++) {

//                    DocumentEntity documentEntity =addDocumentToUser("testuser" + userNumber, documentNumber);
                    //submits part of documents
                    DocumentEntity documentEntity =  new DocumentEntity();
                    String title = faker.ancient().primordial();
                    String description = faker.chuckNorris().fact();
                    documentEntity.setAuthor(userName);
                    documentEntity.setDescription(description);
                    documentEntity.setTitle(title);
                    //documentEntity.setType("Paraiška"); // static for now
                    // randomly chooses one of 5 document types
                    documentEntity.setType(documentTypes.get(randomGenerator.nextInt(5)));
                    if(documentNumber>0 && documentNumber <26){
                        //documentService.submitDocument(documentEntity.getDocumentIdentifier());
                        documentEntity.setDocumentState(DocumentState.SUBMITTED);
                        documentEntity.setPostedDate(LocalDateTime.now());
                      //  documentRepository.saveAll(documents);
                    }
                    //document aprooval

                    if(documentNumber>5 && documentNumber <8) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "admin",
//                                DocumentState.APPROVED, "");

                        //surandame prisiloginusi useri
                        UserEntity user = userRepository.findUserByUsername("admin");
                        documentEntity.setDocumentState(DocumentState.APPROVED);
                        documentEntity.setApprovalDate(LocalDateTime.now());

                        documentEntity.setApprover(user.getFirstname() + " " + user.getLastname());
//                        documentRepository.save(document);
                    }

                    if(documentNumber>8 && documentNumber <10) {
                        //documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "id123",
                         //       DocumentState.APPROVED, "");
                        UserEntity user = userRepository.findUserByUsername("id123");
                        documentEntity.setDocumentState(DocumentState.APPROVED);
                        documentEntity.setApprovalDate(LocalDateTime.now());

                        documentEntity.setApprover(user.getFirstname() + " " + user.getLastname());
                    }

                    String rejectionReason = faker.shakespeare().hamletQuote();
                    if(documentNumber>10 && documentNumber <12) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "id123",
//                                DocumentState.REJECTED, "not valid");
                        UserEntity user = userRepository.findUserByUsername("id123");
                        documentEntity.setDocumentState(DocumentState.REJECTED);
                        documentEntity.setRejectedDate(LocalDateTime.now());
                        documentEntity.setApprover(user.getFirstname() + " " + user.getLastname());
                        documentEntity.setRejectionReason(rejectionReason);
//                        documentRepository.save(document);


                    }
                    if(documentNumber>12 && documentNumber <14) {
//                        documentService.approveOrRejectDocument(documentEntity.getDocumentIdentifier(), "admin",
//       Gerardo2                         DocumentState.REJECTED, "not in a mood approvals");
                        UserEntity user = userRepository.findUserByUsername("admin");
                        documentEntity.setDocumentState(DocumentState.REJECTED);
                        documentEntity.setRejectedDate(LocalDateTime.now());
                        documentEntity.setApprover(user.getFirstname() + " " + user.getLastname());
                        documentEntity.setRejectionReason(rejectionReason);
                    }
                    documentSet.add(documentEntity);
                }
                userEntity.setDocuments(documentSet);
                documentRepository.saveAll(documentSet);
                userRepository.save(userEntity);
            }
        }



    }


}