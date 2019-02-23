package it.akademija;

import it.akademija.auth.AppRoleEnum;
import it.akademija.documents.repository.DocumentEntity;
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
import org.springframework.stereotype.Component;

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
        createDocumentTypeIfNotExists("Darbo sutartis");
        createDocumentTypeIfNotExists("Registruotas laiškas");
        createDocumentTypeIfNotExists("Receptas");

        createUserIfNotExists("Administrator", "IT", "admin", "admin");
        createUserIfNotExists("Augustas", "Dirzys", "id123", "id123");
        createUserIfNotExists( "Anna", "Paidem", "annpai", "annpai");
        createUserIfNotExists( "User", "Vienas", "user1", "user1");
        createUserIfNotExists( "User", "Du", "user2", "user2");


        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Darbo sutartis");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Registruotas laiškas");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Receptas");


        userGroupService.addGroupToUser("Administratoriai","admin");
        userGroupService.addGroupToUser("Administratoriai","id123");
        userGroupService.addGroupToUser("Vadovai","id123");
        userGroupService.addGroupToUser("Vadybininkai","annpai");
        userGroupService.addGroupToUser("Vadybininkai","user1");
        userGroupService.addGroupToUser("Vadybininkai","user2");


        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Darbo sutartis");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Registruotas laiškas");



        userGroupService.addDocumentTypeToApprove("Administratoriai", "Paraiška");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Darbo sutartis");
        userGroupService.addDocumentTypeToApprove( "Vadovai","Registruotas laiškas");


//        addDummydata();
    }

    private void createUserIfNotExists(String fn, String ln, String un, String pswd) {
        UserEntity u = userRepository.findUserByUsername(un);

        if (u == null) {
            userService.addNewUser(fn, ln, un, pswd);
        }
    }

    private void createUserGroupIfNotExists(String title, AppRoleEnum role){
        UserGroupEntity uge = userGroupRepository.findGroupByTitle(title);

        if (uge == null) {
            userGroupService.addNewUserGroup(new CreateUserGroupCommand(title,role));
        }
    }
    private void createDocumentTypeIfNotExists(String title) {
        DocumentTypeEntity dte = documentTypeRepository.findDocumentTypeByTitle(title);

        if (dte == null) {
            documentTypeService.createNewDocumentType(title);
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

    private void addDummydata() throws NoApproverAvailableException {
        //patikrina ar jau buvo prideta data
        if(userRepository.findUserByUsername("testuser1") ==  null) {

            // jei dar neiko nebuvo prideta prideda 10 useriu
            for (int userNumber = 0; userNumber < 5; userNumber++) {
                createUserIfNotExists("name" + userNumber, "surename" + userNumber,
                        "testuser" + userNumber, "testuser" + userNumber);
                userGroupService.addGroupToUser("Vadybininkai", "testuser" + userNumber);

                //kiekvienam useriui pridada po tusciu dokumentu
                for (int documentNumber = 0; documentNumber < 25; documentNumber++) {
                    DocumentEntity documentEntity =addDocumentToUser("testuser" + userNumber, documentNumber);
                    //submits part of documents
                    if(documentNumber>5 && documentNumber <20){
                        documentService.submitDocument(documentEntity.getDocumentIdentifier());
                    }

                }
            }
        }



    }


}