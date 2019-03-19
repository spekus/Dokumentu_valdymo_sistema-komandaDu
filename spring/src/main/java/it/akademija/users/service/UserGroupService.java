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

    public UserGroupService(UserGroupRepository userGroupRepository, DocumentTypeRepository documentTypeRepository,
                            DocumentRepository documentRepository, AuditService auditService) {
        this.userGroupRepository = userGroupRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentRepository = documentRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Collection<UserGroupServiceObject> getAllGroups() {
        return userGroupRepository.
                findAll()
                .stream()
                .map(userGroupEntity -> SOfromEntity(userGroupEntity))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addNewUserGroup(CreateUserGroupCommand createUserGroupCommand, String username) {
        UserGroupEntity userGroupEntityFromDataBase = userGroupRepository.findGroupByTitle(createUserGroupCommand.getTitle());
        if (userGroupEntityFromDataBase == null) {
            UserGroupEntity userGroupEntity = new UserGroupEntity(createUserGroupCommand.getTitle(), createUserGroupCommand.getRole());
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
        UserGroupEntity savedUserGroupEntity = userGroupRepository.findGroupByTitle(title);
        savedUserGroupEntity.setTitle(newTitle);
        UserGroupEntity updateUserGroupEntit = userGroupRepository.save(savedUserGroupEntity);

        if (!username.isEmpty()) {
            UserEntity user = userRepository.findUserByUsername(username);
            if (user != null) {
                auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USERGROUP, ObjectTypeEnum.USERGROUP, title + " -> " + newTitle);
            }
        }
    }


    @Transactional
    public void addGroupToUser(String userGroupTitle, String username, String myusername) {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        Set<UserGroupEntity> allUserGroups = userEntity.getUserGroups();
        if (!allUserGroups.contains(userGroupEntity)) {
            allUserGroups.add(userGroupEntity);
            userRepository.save(userEntity);

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
        UserEntity userEntity = userRepository.findUserByUsername(username);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);

        if (userEntity != null && userGroupEntity != null) {
            userEntity.getUserGroups().remove(userGroupEntity);

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
        UserEntity userEntity = userRepository.findUserByUsername(username);
//        userEntity.getUserGroups().clear();
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByRole(AppRoleEnum.ROLE_SUSPENDED);
        if (userGroupEntity != null) {
            addGroupToUser(userGroupEntity.getTitle(), username, myusername);
            userRepository.save(userEntity);

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
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.addAvailableDocumentTypeToUpload(documentTypeEntity);

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
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.addAvailableDocumentTypeToApprove(documentTypeEntity);

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
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.removeAvailableDocumentTypeToUpload(documentTypeEntity);

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
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
        DocumentTypeEntity documentTypeEntity = documentTypeRepository.findDocumentTypeByTitle(documentTypeTitle);
        if (userGroupEntity != null && documentTypeEntity != null) {
            userGroupEntity.removeAvailableDocumentTypeToApprove(documentTypeEntity);

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
        userGroupRepository.deleteGroupByTitle(title);

        if (!username.isEmpty()) {
            UserEntity user = userRepository.findUserByUsername(username);
            if (user != null) {
                auditService.addNewAuditEntry(user, AuditActionEnum.DELETE_USERGROUP, ObjectTypeEnum.USERGROUP, title);
            }
        }
    }


    @Transactional
    public UserGroupServiceObject getGroupByTitle(String title) {
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(title);
        if (userGroupEntity != null) {
            UserGroupServiceObject userGroupServiceObject = SOfromEntity(userGroupEntity);
            return userGroupServiceObject;
        }
        return null;
    }

//    @Transactional
//    public void addDocumentsToApprove(String userGroupTitle, String documentIdentifier) {
//        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(userGroupTitle);
//        DocumentEntity documentEntity = documentRepository.findDocumentByDocumentIdentifier(documentIdentifier);
//        if (userGroupEntity != null && documentEntity != null) {
//            userGroupEntity.addDocumentsToApprove(documentEntity);
//        }
//
//
//    }

    public UserGroupRepository getUserGroupRepository() {
        return userGroupRepository;
    }

    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
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



