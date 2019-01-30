package it.akademija.users.service;

import it.akademija.users.repository.UserEntity;

import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;

import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public void addNewUser(UserServiceObject userServiceObject) {
        UserEntity userEntity = new UserEntity(userServiceObject.getUserIdentifier(), userServiceObject.getFirstname(), userServiceObject.getLastname(),
                userServiceObject.getUsername(), userServiceObject.getPassword());
        UserEntity userEntityFromDataBase1 = userRepository.findUserByUserIdentifier(userServiceObject.getUserIdentifier());
        UserEntity userEntityFromDataBase2 = userRepository.findUserByUsername(userServiceObject.getUsername());
        if (userEntityFromDataBase1 == null && userEntityFromDataBase2 == null) {
            userRepository.save(userEntity);
        }

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
            userServiceObject.setPassword(userEntity.getPassword());
//            userServiceObject.setUserGroups(userEntity.getUserGroups());
            return userServiceObject;
        }
        return null;
    }

    @Transactional
    public List<UserServiceObject> getAllUsers() {
        return userRepository.findAll().stream().map(userEntity -> new UserServiceObject(userEntity.getUserIdentifier(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getUsername()))
                .collect(Collectors.toList());
    }


    @Transactional
    public List<UserServiceObject> getAllUsersWithPasswords () {
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
    public UserServiceObject getUserForLogin(String username, String password) {
        UserEntity userEntity = userRepository.findUserByUsernameAndPassword(username, password);
        if (userEntity != null) {
            UserServiceObject userServiceObject = new UserServiceObject(userEntity.getUserIdentifier(), userEntity.getFirstname(),
                    userEntity.getLastname(), userEntity.getUsername());
            return userServiceObject;
        }
        return null;
    }

    @Transactional
    public void addGroupToUser(String userIdentifier, String title) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(title);
        if (userEntity.getUserGroups() == null) {
            Set<UserGroupEntity> userGroups = new HashSet<>();
            userEntity.setUserGroups(userGroups);
        }
        userEntity.getUserGroups().add(userGroupEntity);
        userRepository.save(userEntity);
    }

    @Transactional
    public List<UserServiceObject> getAllUsersWithFullInfo() {
        return userRepository.findAll().stream().map(userEntity -> new UserServiceObject(userEntity.getUserIdentifier(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getUsername()))
                .collect(Collectors.toList());
    }

}





