package it.akademija.users.service;



import it.akademija.helpers.DocumentHelper;
import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.repository.UserEntity;

import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;

import it.akademija.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.CollationElementIterator;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DocumentHelper document;

private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService() {
    }

    public UserService(UserRepository userRepository, DocumentRepository documentRepository, UserGroupRepository userGroupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.userGroupRepository = userGroupRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    public UserRepository getUserRepository() {
//        return userRepository;
//    }
//
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//
//    public UserGroupRepository getUserGroupRepository() {
//        return userGroupRepository;
//    }
//
//    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
//        this.userGroupRepository = userGroupRepository;
//    }

    @Transactional
    public List<UserServiceObject> getAllUsers() {
        LOGGER.info("getAllUsers");
        List<UserEntity> users = userRepository.findAll();
        List<UserServiceObject> list = users.stream()
                .map(userEntity ->
                        SOfromEntity(userEntity))
                .collect(Collectors.toList());
        LOGGER.info("returning all users");
        return list;
    }

    @Transactional
    public UserServiceObject getUserByUsername(String username) {
        LOGGER.info("getUserByUsername");
        UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            LOGGER.info("returning user with username - "  + username);
            return SOfromEntity(userEntity);
        }
        throw new IllegalArgumentException("User with username -  " + username
                + " was not found");
    }

    @Transactional
    public List<UserGroupServiceObject> getUserGroups(String username) {
        LOGGER.info("getUserGroups");
        UserEntity userEntity = userRepository.findUserByUsername(username);
        Set<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();

        List<UserGroupServiceObject> userGroups =  groupsUserBelongsTo.stream().map(userGroupEntity -> UserGroupService.SOfromEntity(userGroupEntity))
                .collect(Collectors.toList());
        LOGGER.info("returning user groups of user - " + username);
        return userGroups;
    }


    //Gets all user's document types that he can create documents
    @Transactional
    public Set<DocumentTypeServiceObject> getUserDocumentTypesHeCanCreate(String username) {
        LOGGER.info("getUserDocumentTypesHeCanCreate");
        UserEntity userEntity = userRepository.findUserByUsername(username);

        if (userEntity != null) {
            Set<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();
            Set<DocumentTypeEntity> allDocTypesUserCanCreate = new HashSet<>();

            for (UserGroupEntity userGroupEntity : groupsUserBelongsTo) {
                allDocTypesUserCanCreate.addAll(userGroupEntity.getAvailableDocumentTypesToUpload());
            }
            Set<DocumentTypeServiceObject> returningDocumentTypesUserCanCreate =
                    allDocTypesUserCanCreate.stream().map((documentTypeEntity) ->
                            new DocumentTypeServiceObject(documentTypeEntity.getTitle())).collect(Collectors.toSet());
            LOGGER.info("returning user documents user - " +username + " can create");
            return returningDocumentTypesUserCanCreate;
        }
        throw new IllegalArgumentException("Username -  " + username
                + " does not have any documents he can create");
    }

    @Transactional
    public List<UserServiceObject> getUserByCriteria(String criteria) {
        LOGGER.info("getUserByCriteria");
        if (userRepository.findByUsernameOrLastname(criteria) != null) {
            List<UserServiceObject> userList = userRepository.findByUsernameOrLastname(criteria)
                    .stream()
                    .map(userEntity -> SOfromEntity(userEntity))
                    .collect(Collectors.toList());
            LOGGER.info("returning users according to criteria - " + criteria);
            return userList;
        }
        throw new IllegalArgumentException("No Users with criteria - " + criteria + " have been found");
    }


    @Transactional
    public void addNewUser(String firstname, String lastname, String username, String password) {
        LOGGER.info("addNewUser");
        UserEntity userEntityFromDataBase1 = userRepository.findUserByUsername(username);
        UserEntity userEntityFromDataBase2 = userRepository.findUserByUsername(username);
        if (userEntityFromDataBase1 == null && userEntityFromDataBase2 == null) {
            UserEntity userEntity = new UserEntity(firstname, lastname, username, passwordEncoder.encode(password));
            userRepository.save(userEntity);
            LOGGER.info("new user has been added, name - " + firstname + " lastname - "
                    + lastname + " username - " +username);
        }

    }

    @Transactional
    public void updateUserPassword(String username, String password) {
        LOGGER.info("updateUserPassword");
        UserEntity savedUserEntity = userRepository.findUserByUsername(username);
        savedUserEntity.setPassword(passwordEncoder.encode(password));
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
        LOGGER.info("password has been updated of user - " + username);
    }

    @Transactional
    public void updateUserInformation(String username, String newFirstname, String newLastname) {
        LOGGER.info("updateUserInformation");
        UserEntity savedUserEntity = userRepository.findUserByUsername(username);
        savedUserEntity.setFirstname(newFirstname);
        savedUserEntity.setLastname(newLastname);
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
        LOGGER.info("user information chanfed of user with username - " + username + " to : firstname - "
                + newFirstname + " lastname - " +newLastname);
    }

    @Transactional
    @Modifying
    public void deleteUserByUsername(String username) {
        LOGGER.info("deleteUserByUsername has been involved and is carried away");
            userRepository.deleteUserByUsername(username);
    }

//    @Transactional
//    public UserServiceObject getUserByLastname(String lastname) {
//        LOGGER.info("getUserByLastname, returning user by lastname");
//        UserEntity userEntity = userRepository.findUserByLastname(lastname);
//        if (userEntity != null) {
//            return SOfromEntity(userEntity);
//        }
//        return null;
//    }

    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LOGGER.info("loadUserByUsername");
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
        LOGGER.info("user with username - " + s + " details and security information is being returned");
        return userDetails;
    }

    public UserServiceObject SOfromEntity(UserEntity entity) {
        UserServiceObject so = new UserServiceObject();
        so.setFirstname(entity.getFirstname());
        so.setLastname(entity.getLastname());
        so.setUsername(entity.getUsername());
        so.setUserGroups(entity.getUserGroups()
                .stream()
                .map(ug ->
                        UserGroupService.SOfromEntity(ug))
                .collect(Collectors.toSet()));

        return so;
    }
    @Transactional
    public List<DocumentEntity> getAllUserDocuments(String userName) {
        LOGGER.info("getAllUserDocuments, THIS METHOD SHOULD NOT BE USED");
        return documentRepository.findByAuthor(userName);
    }


    @Transactional
    public Page<DocumentServiceObject> getAllUserDocuments(String userName, Pageable pageFormatInfo) {
        LOGGER.info("getAllUserDocuments");
        List<DocumentServiceObject> allUserDocuments = document.getDocumentsBy(userName);

        //paginimo logika
        List<DocumentServiceObject> filteredList= new ArrayList<>();
        int possition = pageFormatInfo.getPageNumber() * pageFormatInfo.getPageSize();
        int limit = possition + pageFormatInfo.getPageSize();
        if(limit > allUserDocuments.size()){
            // checks that limit set by paging is not bigger than total element count
            limit = allUserDocuments.size();
        }
        for(; possition < (limit); possition++){
            filteredList.add(allUserDocuments.get(possition));
        }
        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(filteredList,
                pageFormatInfo, allUserDocuments.size());
        LOGGER.info("All documents of user - " +userName + " are being returned"+
                " , returning page - " +pageFormatInfo.getPageNumber() +"\n" + " size of page is "
                + pageFormatInfo.getPageSize() + " Full ammount of elements is - " + allUserDocuments.size());
        return pageData;

    }
    @Transactional
    public Page<DocumentServiceObject> getUserDocumentsByState(String userName, DocumentState state, int page, int size) {
        LOGGER.info("getUserDocumentsByState");
        Set<DocumentEntity> documentEntitySet = userRepository.findByUsername(userName).getDocumentEntities();


        // konvertuojam dokumentu entity i objektus. deje reik konveruot visus, nes kitaip neveiks rikiavimas
        List<DocumentServiceObject> documentServiceObjects = documentEntitySet.stream()
                .filter(p -> p.getDocumentState().equals(state))
                .map(documentEntity -> document.SOfromEntity(documentEntity))
                .collect(Collectors.toList());
        // sortinam pagal title, kad galetume rikiuoti
        Collections.sort(documentServiceObjects);


        //paginimo logika
        List<DocumentServiceObject> filteredList= new ArrayList<>();
        int possition = page * size;
        int limit = possition + size;
        if(limit > documentServiceObjects.size()){
            // checks that limit set by paging is not bigger than total element count
            limit = documentServiceObjects.size();
        }
        for(; possition < (limit); possition++){
            filteredList.add(documentServiceObjects.get(possition));
        }

        Pageable pageInformation =
                PageRequest.of(page, size);

        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(filteredList,
                pageInformation, documentServiceObjects.size());

        LOGGER.info(" documents of user - " +userName + " with document state - " + state +" are being returned "+
                " , returning page - " +page +  "\n" + " size of page is " + size + " size of total data points  is - " + pageData.getTotalElements());
        return pageData;
    }



    @Transactional
    public Page<DocumentServiceObject> getDocumentsToApprove(String userName, Pageable sortByTitle) {
        LOGGER.info("getDocumentsToApprove");

        List<String> documentTypesUserCanAproove =
                document.getDocumentTypesUserCanAprooveBy(userName);
        List<DocumentServiceObject> documentsUserCanAproove =
                document.getDocumentsBy(documentTypesUserCanAproove, sortByTitle);
        // we need total ammount of documents to be displayed for pagination to work, thus we need second query.
        long documentCount = documentRepository.getDocumentsToApproveSize(documentTypesUserCanAproove);

        PageImpl<DocumentServiceObject> pagedData = new PageImpl<>(documentsUserCanAproove,
                sortByTitle, documentCount);
        LOGGER.info(" documents for approval of user - " +userName + " are being returned "+
                " , returning page - " +sortByTitle.getPageNumber() +"\n" + " size of page is "
                + sortByTitle.getPageSize() + " size of total data points  is - " + pagedData.getTotalElements());

        return pagedData;
    }

    @Transactional
    public Page<DocumentServiceObject> getDocumentsToApprove(String userName,Pageable sortByTitle, String filteringCriteria) {
        LOGGER.info("getDocumentsToApproveFiltered");

        List<String> documentTypesUserCanAproove =
                document.getDocumentTypesUserCanAprooveBy(userName);
        List<DocumentServiceObject> documentsUserCanAproove =
                document.getDocumentsBy(documentTypesUserCanAproove, sortByTitle, filteringCriteria);
        long filteredDocumentCount = documentRepository.getDocumentsToApproveFilteredSize(documentTypesUserCanAproove
                ,filteringCriteria);
        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(documentsUserCanAproove,
                sortByTitle, filteredDocumentCount);
        LOGGER.info(" filtering document of user - " +userName + " according to filteringCriteria - " + filteringCriteria +" search results are being returned "+
                " , page - " +sortByTitle.getPageNumber() +"\n" + " size of page is "
                + sortByTitle.getPageSize() + " size of total data points  is - " + pageData.getTotalElements());

        return pageData;
    }

}




