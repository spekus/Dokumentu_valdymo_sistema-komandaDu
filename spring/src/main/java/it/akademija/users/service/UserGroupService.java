package it.akademija.users.service;

import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import it.akademija.audit.service.AuditService;
import it.akademija.auth.AppRoleEnum;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.controller.CreateUserGroupCommand;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;
import it.akademija.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    private Logger LOGGER = LoggerFactory.getLogger(UserGroupService.class);

    public UserGroupService(UserGroupRepository userGroupRepository, DocumentTypeRepository documentTypeRepository,
                            DocumentRepository documentRepository, AuditService auditService) {
        this.userGroupRepository = userGroupRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentRepository = documentRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Collection<UserGroupServiceObject> getAllGroups() {
        LOGGER.info("getAllGroups is being run");
        return userGroupRepository.
                findAll()
                .stream()
                .map(userGroupEntity -> SOfromEntity(userGroupEntity))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addNewUserGroup(CreateUserGroupCommand createUserGroupCommand, String username) {
        LOGGER.info("addNewUserGroup is being run by user - " + username);
        UserGroupEntity userGroupEntityFromDataBase = userGroupRepository.findGroupByTitle(createUserGroupCommand.getTitle());
        if (userGroupEntityFromDataBase == null) {
            UserGroupEntity userGroupEntity = new UserGroupEntity(createUserGroupCommand.getTitle(), createUserGroupCommand.getRole());
            LOGGER.info("User group by name - " + createUserGroupCommand.getTitle());
            userGroupRepository.save(userGroupEntity);

            if (!username.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(username);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.CREATE_NEW_USERGROUP, ObjectTypeEnum.USERGROUP, createUserGroupCommand.getTitle());
                }
            }
        }
    }

    @Transactional
    public void updateGroupByTitle(String title, String newTitle, String username) {
        LOGGER.info("updateGroupByTitle is being run by user - " + username);
        UserGroupEntity savedUserGroupEntity = userGroupRepository.findGroupByTitle(title);
        savedUserGroupEntity.setTitle(newTitle);
        UserGroupEntity updateUserGroupEntit = userGroupRepository.save(savedUserGroupEntity);

        if (!username.isEmpty()) {
            LOGGER.info("Old user group title - " + title + " ,new title - " + newTitle);
            UserEntity user = userRepository.findUserByUsername(username);
            if (user != null) {
                auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USERGROUP, ObjectTypeEnum.USERGROUP, title + " -> " + newTitle);
            }
        }
    }


    @Transactional
    public void addGroupToUser(String userGroupTitle, String username, String myusername) {
        LOGGER.info("addGroupToUser is being run by user - " + myusername);
        UserEntity userEntity = userRepository.findUserByUsername(username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        Set<UserGroupEntity> allUserGroups = userEntity.getUserGroups();
        if (!allUserGroups.contains(userGroupEntity)) {
            allUserGroups.add(userGroupEntity);
            userRepository.save(userEntity);
            LOGGER.info("person who is added to group - " + username + " ,person who is adding to the group- " + myusername
                    + " usergroup title - " +userGroupTitle);
            if (!myusername.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(myusername);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.ADD_USER_TO_GROUP, ObjectTypeEnum.USER, username + " + group: " + userGroupTitle);
                }
            }
        }
    }

    @Transactional
    public void removeGroupFromUser(String userGroupTitle, String username, String myusername) {
        LOGGER.info("addGroupToUser is being run by user - " + myusername);
        UserEntity userEntity = userRepository.findUserByUsername(username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);

        if (userEntity != null && userGroupEntity != null) {
            userEntity.getUserGroups().remove(userGroupEntity);
            LOGGER.info("person who is removed from group - " + username + " ,person who is removed from the group- " + myusername
                    + " usergroup title - " +userGroupTitle);

            if (!myusername.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(myusername);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.REMOVE_USER_FROM_GROUP, ObjectTypeEnum.USER, username + " - group: " + userGroupTitle);
                }
            }
        }
    }

    @Transactional
    public void suspendUser(String username, String myusername) {
        LOGGER.info("suspendUser is being run by user - " + myusername);
        UserEntity userEntity = userRepository.findUserByUsername(username);
//        userEntity.getUserGroups().clear();
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByRole(AppRoleEnum.ROLE_SUSPENDED);
        if (userGroupEntity != null) {
            addGroupToUser(userGroupEntity.getTitle(), username, myusername);
            userRepository.save(userEntity);
            LOGGER.info("person who is being suspended - " + username);
            if (!myusername.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(myusername);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.SUSPEND_USER, ObjectTypeEnum.USER, username);
                }
            }
        }
    }

    @Transactional
    public void addDocumentTypeToUpload(String userGroupTitle, String documentTypeTitle, String username) {
        LOGGER.info("addDocumentTypeToUpload is being run by user - " + username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.addAvailableDocumentTypeToUpload(documentTypeEntity);
            LOGGER.info("Document type - " + documentTypeTitle + " is being added to group - " + userGroupTitle +
                    " , now users from the group should be able to upload these document types");
            if (!username.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(username);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USERGROUP, ObjectTypeEnum.USERGROUP, userGroupTitle + " + upload: " + documentTypeTitle);
                }
            }
        }



    }

    @Transactional
    public void addDocumentTypeToApprove(String userGroupTitle, String documentTypeTitle, String username) {
        LOGGER.info("addDocumentTypeToApprove is being run by user - " + username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.addAvailableDocumentTypeToApprove(documentTypeEntity);
            LOGGER.info("Document type - " + documentTypeTitle + " is being added to group - " + userGroupTitle +
                    " , now users from the group should be able to above these document types");
            if (!username.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(username);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USERGROUP, ObjectTypeEnum.USERGROUP, userGroupTitle + " + approve: " + documentTypeTitle);
                }
            }
        }
    }

    @Transactional
    public void removeDocumentTypeToUpload(String userGroupTitle, String documentTypeTitle, String username) {
        LOGGER.info("removeDocumentTypeToUpload is being run by user - " + username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.removeAvailableDocumentTypeToUpload(documentTypeEntity);
            LOGGER.info("Document type - " + documentTypeTitle + " is being removed from group - " + userGroupTitle +
                    " , now users from the group should no longer be able to upload these document types");
            if (!username.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(username);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USERGROUP, ObjectTypeEnum.USERGROUP, userGroupTitle + " - upload: " + documentTypeTitle);
                }
            }
        }
    }

    @Transactional
    public void removeDocumentTypeToApprove(String userGroupTitle, String documentTypeTitle, String username) {
        LOGGER.info("removeDocumentTypeToUpload is being run by user - " + username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.removeAvailableDocumentTypeToApprove(documentTypeEntity);
            LOGGER.info("Document type - " + documentTypeTitle + " is being removed from group - " + userGroupTitle +
                    " , now this group should loose privilege to approve these document types");

            if (!username.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(username);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USERGROUP, ObjectTypeEnum.USERGROUP, userGroupTitle + " - approve: " + documentTypeTitle);
                }
            }
        }
    }

    @Transactional
    @Modifying
    public void deleteGroupByTitle(String title, String username) {
        LOGGER.info("deleteGroupByTitle is being run by user - " + username);
        userGroupRepository.deleteGroupByTitle(title);

        if (!username.isEmpty()) {
            UserEntity user = userRepository.findUserByUsername(username);
            if (user != null) {
                LOGGER.info(username + " is deleting group - " + title);
                auditService.addNewAuditEntry(user, AuditActionEnum.DELETE_USERGROUP, ObjectTypeEnum.USERGROUP, title);
            }
        }
    }



    static UserGroupServiceObject SOfromEntity(UserGroupEntity entity) {
        UserGroupServiceObject so = new UserGroupServiceObject();
        so.setTitle(entity.getTitle());
        so.setRole(entity.getRole());
        so.setTypesToUpload(entity.getAvailableDocumentTypesToUpload()
                .stream().map(dte -> new DocumentTypeServiceObject(dte.getTitle())).collect(Collectors.toSet())
        );
        so.setTypesToApprove(entity.getAvailableDocumentTypesToApprove()
                .stream().map(dte -> new DocumentTypeServiceObject(dte.getTitle())).collect(Collectors.toSet())
        );
        return so;
    }

}



