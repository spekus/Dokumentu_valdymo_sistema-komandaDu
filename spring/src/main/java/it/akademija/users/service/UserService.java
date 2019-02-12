package it.akademija.users.service;

import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.repository.UserEntity;

import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;

import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService() {
    }

    public UserService(UserRepository userRepository, UserGroupRepository userGroupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.passwordEncoder = passwordEncoder;
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
        UserEntity userEntityFromDataBase1 = userRepository.findUserByUserIdentifier(userIdentifier);
        UserEntity userEntityFromDataBase2 = userRepository.findUserByUsername(username);

        if (userEntityFromDataBase1 == null && userEntityFromDataBase2 == null) {
            UserEntity userEntity = new UserEntity(userIdentifier, firstname, lastname, username, passwordEncoder.encode(password));
            userRepository.save(userEntity);
        }

    }

    @Transactional
    public UserServiceObject getUserByUserId(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        if (userEntity != null) {
            return SOfromEntity(userEntity);
        }
        return null;
    }

    @Transactional
    public List<UserServiceObject> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserServiceObject> list = users.stream()
                .map(userEntity ->
                        SOfromEntity(userEntity))
                .collect(Collectors.toList());
        return list;
    }

    @Transactional
    @Modifying
    public void deleteUserByIdentifier(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        for (DocumentEntity documentEntity:userEntity.getDocumentEntities()) {
            userEntity.removeDocument(documentEntity);
        }
        userRepository.deleteByUserIdentifier(userIdentifier);
    }

    @Transactional
    public void updateUserPassword(String userIdentifier, String password) {
        UserEntity savedUserEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        savedUserEntity.setPassword(passwordEncoder.encode(password));
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
    }

    @Transactional
    public List<UserGroupServiceObject> getUserGroups(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);
        Set<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();

        return groupsUserBelongsTo.stream().map(userGroupEntity -> new UserGroupServiceObject(userGroupEntity.getTitle(), userGroupEntity.getRole()))
                .collect(Collectors.toList());
    }

    //Gets all user's document types that he can create documents
    @Transactional
    public Set<DocumentTypeServiceObject> getUserDocumentTypesHeCanCreate(String userIdentifier) {
        UserEntity userEntity = userRepository.findUserByUserIdentifier(userIdentifier);

        if (userEntity != null) {
            Set<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();
            Set<DocumentTypeEntity> allDocTypesUserCanCreate = new HashSet<>();

            for (UserGroupEntity userGroupEntity : groupsUserBelongsTo) {
                allDocTypesUserCanCreate.addAll(userGroupEntity.getAvailableDocumentTypesToUpload());
            }

            return allDocTypesUserCanCreate.stream().map((documentTypeEntity) ->
                    new DocumentTypeServiceObject(documentTypeEntity.getTitle())).collect(Collectors.toSet());
        }

        return null;

    }

    @Transactional
    public UserServiceObject getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            return SOfromEntity(userEntity);
        }
        return null;
    }


    @Transactional
    public UserServiceObject getUserByLastname(String lastname) {
        UserEntity userEntity = userRepository.findUserByLastname(lastname);
        if (userEntity != null) {
            return SOfromEntity(userEntity);
        }
        return null;
    }

    @Transactional
    public List<UserServiceObject> getUserByCriteria(String criteria) {
        if (userRepository.findByUsernameOrLastnameOrId(criteria) != null) {
            return userRepository.findByUsernameOrLastnameOrId(criteria)
                    .stream()
                    .map(userEntity -> SOfromEntity(userEntity))
                    .collect(Collectors.toList());
        }
        return null;

    }

    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getUserGroups().forEach(group -> {
            authorities.add(new SimpleGrantedAuthority(group.getRole().toString()));
        });

        UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), authorities);

        return userDetails;
    }

    public UserServiceObject SOfromEntity(UserEntity entity){
        UserServiceObject so = new UserServiceObject();
        so.setUserIdentifier(entity.getUserIdentifier());
        so.setFirstname(entity.getFirstname());
        so.setLastname(entity.getLastname());
        so.setUsername(entity.getUsername());
        so.setUserGroups(entity.getUserGroups()
                .stream()
                .map(ug ->
                        new UserGroupServiceObject(ug.getTitle(),ug.getRole()))
                .collect(Collectors.toSet()));
        return so;
    }

}




