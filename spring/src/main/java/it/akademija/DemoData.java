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

        createUserGroupIfNotExists("Administratoriai",AppRoleEnum.ADMIN_USER);
        createUserGroupIfNotExists("Buhalteriai",AppRoleEnum.STANDARD_USER);
        createUserGroupIfNotExists("Vadybininkai",AppRoleEnum.STANDARD_USER);
        createUserGroupIfNotExists("Vadovai",AppRoleEnum.STANDARD_USER);

        createDocumentTypeIfNotExists("Paraiška");
        createDocumentTypeIfNotExists("Darbo sutartis");
        createDocumentTypeIfNotExists("Registruotas laiškas");

        createUserIfNotExists("admin", "Administrator", "IT", "admin", "admin");
        createUserIfNotExists("annpai", "Anna", "Paidem", "annpai", "annpai");

        userGroupService.addGroupToUser("Administratoriai","admin");
        userGroupService.addGroupToUser("Vadybininkai","annpai");

        userGroupService.addDocumentTypeToUpload("Administratoriai","Paraiška");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Darbo sutartis");
        userGroupService.addDocumentTypeToUpload("Administratoriai","Registruotas laiškas");

        userGroupService.addDocumentTypeToUpload("Vadybininkai","Paraiška");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Darbo sutartis");
        userGroupService.addDocumentTypeToUpload("Vadybininkai","Registruotas laiškas");

        userGroupService.addDocumentTypeToApprove("Administratoriai","Paraiška");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Darbo sutartis");
        userGroupService.addDocumentTypeToApprove("Administratoriai","Registruotas laiškas");
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