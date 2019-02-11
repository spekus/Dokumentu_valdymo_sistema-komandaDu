package it.akademija;

import it.akademija.auth.AppRoleEnum;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentTypeService;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import it.akademija.users.service.UserGroupService;
import it.akademija.users.service.UserGroupServiceObject;
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

        createDocumentTypeIfNotExists("Paraiška");
        createDocumentTypeIfNotExists("Darbo sutartis");
        createDocumentTypeIfNotExists("Registruotas laiškas");

        createUserIfNotExists("admin", "Administrator", "IT", "admin", "admin");
        createUserIfNotExists("id123", "Augustas", "Dirzys", "id123", "id123");
        createUserIfNotExists("annpai", "Anna", "Paidem", "annpai", "annpai");
        createUserIfNotExists("user1", "User", "Vienas", "user1", "user1");
        createUserIfNotExists("user2", "User", "Du", "user2", "user2");

        userGroupService.addGroupToUser("Administratoriai","admin");
        userGroupService.addGroupToUser("Administratoriai","id123");
        userGroupService.addGroupToUser("Vadybininkai","annpai");
        userGroupService.addGroupToUser("Vadybininkai","user1");
        userGroupService.addGroupToUser("Vadybininkai","user2");


        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Darbo sutartis");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Registruotas laiškas");

        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Darbo sutartis");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Registruotas laiškas");

    }

    private void createUserIfNotExists(String id, String fn, String ln, String un, String pswd) {
        UserEntity u = userRepository.findUserByUserIdentifier(id);

        if (u == null) {
            userService.addNewUser(id, fn, ln, un, pswd);
        }
    }

    private void createUserGroupIfNotExists(String title, AppRoleEnum role){
        UserGroupEntity uge = userGroupRepository.findGroupByTitle(title);

        if (uge == null) {
            userGroupService.addNewUserGroup(new UserGroupServiceObject(title,role));
        }
    }
    private void createDocumentTypeIfNotExists(String title) {
        DocumentTypeEntity dte = documentTypeRepository.findDocumentTypeByTitle(title);

        if (dte == null) {
            documentTypeService.createNewDocumentType(title);
        }
    }


}