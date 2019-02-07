package it.akademija.users.service;

import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.repository.UserEntity;

import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;

import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

import java.util.stream.Collectors;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    UserGroupRepository userGroupRepository;

    public UserService() {
    }


    public UserService(UserRepository userRepository, UserGroupRepository userGroupRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;

    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserGroupRepository getUserGroupRepository() {
        return userGroupRepository;
    }

    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }


    @Transactional
    public void addNewUser(String userIdentifier, String firstname, String lastname, String username, String password) {
        UserEntity userEntity = new UserEntity(userIdentifier, firstname, lastname, username, password);
        UserEntity userEntityFromDataBase1 = userRepository.findUserByUserIdentifier(userIdentifier);
        UserEntity userEntityFromDataBase2 = userRepository.findUserByUsername(username);
        if (userEntityFromDataBase1 == null && userEntityFromDataBase2 == null) {
            userRepository.save(userEntity);
        }

    }

    @Transactional
    public UserServiceObject getUserByUserId(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        if (userEntity != null) {
            UserServiceObject userServiceObject = new UserServiceObject();
            userServiceObject.setUserIdentifier(userEntity.getUserIdentifier());
            userServiceObject.setFirstname(userEntity.getFirstname());
            userServiceObject.setLastname(userEntity.getLastname());
            userServiceObject.setUsername(userEntity.getUsername());
            userServiceObject.setPassword(userEntity.getPassword());
            userServiceObject.setUserGroups(userEntity.getUserGroups());
            return userServiceObject;
        }
        return null;
    }

    @Transactional
    public List<UserServiceObject> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserServiceObject> userDto = new ArrayList<>();
        for (UserEntity userEntity : users) {
            UserServiceObject userServiceObject = new UserServiceObject();
            userServiceObject.setUserIdentifier(userEntity.getUserIdentifier());
            userServiceObject.setFirstname(userEntity.getFirstname());
            userServiceObject.setLastname(userEntity.getLastname());
            userServiceObject.setUsername(userEntity.getUsername());
            userServiceObject.setPassword(userEntity.getPassword());
            userServiceObject.setUserGroups(userEntity.getUserGroups());
            userDto.add(userServiceObject);
        }
        return userDto;
    }


    @Transactional
    public List<UserServiceObject> getAllUsersWithPasswords() {
        return userRepository.findAll().stream().map(userEntity -> new UserServiceObject(userEntity.getUserIdentifier(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getUsername(),
                userEntity.getPassword()))
                .collect(Collectors.toList());
    }


    @Transactional
    @Modifying
    public void deleteUserByIdentifier(String userIdentifier) {
        userRepository.deleteByUserIdentifier(userIdentifier);


    }

    @Transactional
    public void updateUserPassword(String userIdentifier, String password) {
        UserEntity savedUserEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        savedUserEntity.setPassword(password);
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
    }

    @Transactional
    public UserServiceObject userLogin(String username, String password) {
        UserEntity userEntity = userRepository.findUserByUsernameAndPassword(username, password);
        if (userEntity != null) {

            UserServiceObject userServiceObject = new UserServiceObject(userEntity.getUserIdentifier(), userEntity.getFirstname(),
                    userEntity.getLastname(), userEntity.getUsername());
            return userServiceObject;
        }
        return null;

    }


    public List<UserGroupServiceObject> getUserGroups(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        List<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();

        return groupsUserBelongsTo.stream().map(userGroupEntity -> new UserGroupServiceObject(userGroupEntity.getTitle()))
                .collect(Collectors.toList());
    }

    //Gets all user's document types that he can create documents
    public Set<DocumentTypeServiceObject> getUserDocumentTypesHeCanCreate(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        List<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();
        Set<DocumentTypeEntity> allDocTypesUserCanCreate = new HashSet<>();

        for (UserGroupEntity userGroupEntity : groupsUserBelongsTo) {
            allDocTypesUserCanCreate.addAll(userGroupEntity.getAvailableDocumentTypesToUpload());
        }

        return allDocTypesUserCanCreate.stream().map((documentTypeEntity) ->
                new DocumentTypeServiceObject(documentTypeEntity.getTitle())).collect(Collectors.toSet());

    }

    @Transactional
    public UserServiceObject getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            UserServiceObject userServiceObject = new UserServiceObject();
            userServiceObject.setUserIdentifier(userEntity.getUserIdentifier());
            userServiceObject.setFirstname(userEntity.getFirstname());
            userServiceObject.setLastname(userEntity.getLastname());
            userServiceObject.setUsername(userEntity.getUsername());
            userServiceObject.setUserGroups(userEntity.getUserGroups());
            return userServiceObject;
        }
        return null;
    }


    @Transactional
    public UserServiceObject getUserByLastname(String lastname) {
        UserEntity userEntity = userRepository.findUserByLastname(lastname);
        if (userEntity != null) {
            UserServiceObject userServiceObject = new UserServiceObject();
            userServiceObject.setUserIdentifier(userEntity.getUserIdentifier());
            userServiceObject.setFirstname(userEntity.getFirstname());
            userServiceObject.setLastname(userEntity.getLastname());
            userServiceObject.setUsername(userEntity.getUsername());
            userServiceObject.setUserGroups(userEntity.getUserGroups());
            return userServiceObject;
        }
        return null;
    }


}




