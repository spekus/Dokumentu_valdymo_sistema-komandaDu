package it.akademija.users.service;


import it.akademija.App;
import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import it.akademija.audit.service.AuditService;
import it.akademija.auth.AppRoleEnum;
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

    @Autowired
    private AuditService auditService;

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService() {
    }

    @Transactional
    public Page<UserServiceObject> getAllUsers(Pageable pageFormatDetails) {
        LOGGER.info("getAllUsers");
        List<UserEntity> allUsers = userRepository.getAllUsers(pageFormatDetails);
        List<UserServiceObject> allUsersAsServiceObjects = allUsers.stream()
                .map(userEntity ->
                        SOfromEntity(userEntity))
                .collect(Collectors.toList());
        long userCount = userRepository.getTotalUsersCount();

        PageImpl<UserServiceObject> pagedData=new PageImpl<>(allUsersAsServiceObjects,pageFormatDetails,userCount);
        LOGGER.info("returning all users");
        return pagedData;
    }


    @Transactional
    public UserServiceObject getUserByUsername(String username) {
        LOGGER.info("getUserByUsername");
        UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            LOGGER.info("returning user with username - " + username);
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

        List<UserGroupServiceObject> userGroups = groupsUserBelongsTo.stream().map(userGroupEntity -> UserGroupService.SOfromEntity(userGroupEntity))
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
            LOGGER.info("returning user documents user - " + username + " can create");
            return returningDocumentTypesUserCanCreate;
        }
        throw new IllegalArgumentException("Username -  " + username
                + " does not have any documents he can create");
    }

    @Transactional
    public Page<UserServiceObject> getUserByCriteria(String criteria, Pageable pageFormatDetails) {
        LOGGER.info("getUserByCriteria");

        String criteriaInLowerCaps=criteria.toLowerCase();
        if (userRepository.findByUsernameOrLastname(criteriaInLowerCaps, pageFormatDetails) != null) {
            List<UserServiceObject> userList = userRepository.findByUsernameOrLastname(criteriaInLowerCaps, pageFormatDetails)
                    .stream()
                    .map(userEntity -> SOfromEntity(userEntity))
                    .collect(Collectors.toList());

            long usersByCriteriaCount=userRepository.getUsersByCriteriaSize(criteriaInLowerCaps);

            PageImpl <UserServiceObject> pagedData=new PageImpl<>(userList, pageFormatDetails, usersByCriteriaCount);

            LOGGER.info("returning users according to criteria - " + criteria);
            return pagedData;
        }
        throw new IllegalArgumentException("No Users with criteria - " + criteria + " have been found");
    }



    @Transactional
    public void createNewUser(String firstname, String lastname, String username, String password, String myusername) {
        LOGGER.info("createNewUser");
        UserEntity existingUser = userRepository.findByUsernameIgnoreCase(username);
        if (existingUser == null) {
            UserEntity userEntity = new UserEntity(firstname, lastname, username, passwordEncoder.encode(password));
            userRepository.save(userEntity);

            if (!myusername.isEmpty()) {
                UserEntity user = userRepository.findUserByUsername(myusername);
                if (user != null) {
                    auditService.addNewAuditEntry(user, AuditActionEnum.CREATE_NEW_USER, ObjectTypeEnum.USER, username);
                }
            }

            LOGGER.info("new user has been added, name - " + firstname + " lastname - "
                    + lastname + " username - " + username);

        } else {
            throw new IllegalArgumentException("Naudotojo vardas " + username + " jau yra");
        }

    }

    @Transactional
    public void updateUserPassword(String username, String password, String myusername) {
        LOGGER.info("updateUserPassword");
        UserEntity savedUserEntity = userRepository.findUserByUsername(username);
        savedUserEntity.setPassword(passwordEncoder.encode(password));
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
        LOGGER.info("password has been updated of user - " + username);

        if (!myusername.isEmpty()) {
            UserEntity user = userRepository.findUserByUsername(myusername);
            if (user != null) {
                auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USER, ObjectTypeEnum.USER, username + " (password)");
            }
        }
    }

    @Transactional
    public void updateUserInformation(String username, String newFirstname, String newLastname, String myusername) {
        LOGGER.info("updateUserInformation");
        UserEntity savedUserEntity = userRepository.findUserByUsername(username);
        savedUserEntity.setFirstname(newFirstname);
        savedUserEntity.setLastname(newLastname);
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
        LOGGER.info("user information chanfed of user with username - " + username + " to : firstname - "
                + newFirstname + " lastname - " + newLastname);

        if (!myusername.isEmpty()) {
            UserEntity user = userRepository.findUserByUsername(myusername);
            if (user != null) {
                auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_USER, ObjectTypeEnum.USER, username);
            }
        }
    }



    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LOGGER.info("loadUserByUsername");
        UserEntity user = userRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        List<AppRoleEnum> userRoles = user.getUserGroups()
                .stream()
                .map(group -> group.getRole())
                .collect(Collectors.toList());

        // jei tarp roliu yra suspended - ji vienintele kuria pridedam
        if (userRoles.contains(AppRoleEnum.ROLE_SUSPENDED)) {
            authorities.add(new SimpleGrantedAuthority(AppRoleEnum.ROLE_SUSPENDED.toString()));
        } else // o jei ne - pridedam visas roles.
        {
            authorities.addAll(
                    userRoles
                            .stream()
                            .map(role ->
                                    new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toList())
            );
        }

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
        LOGGER.info("getAllUserDocuments, with one constructor");
        return documentRepository.findByAuthor(userName);
    }


    @Transactional
    public Page<DocumentServiceObject> getAllUserDocuments(String userName, Pageable pageFormatDetails) {
        LOGGER.info("getAllUserDocuments with 2 constructors");
        List<DocumentServiceObject> allUserDocuments = document.getDocumentsBy(userName);
        PageImpl<DocumentServiceObject> pagedData = document.getPage(pageFormatDetails, allUserDocuments);
        LOGGER.info("All documents of user - " + userName + " are being returned" +
                " , returning page - " + pageFormatDetails.getPageNumber() + "\n" + " size of page is "
                + pageFormatDetails.getPageSize() + " Full ammount of elements is - " + allUserDocuments.size());
        return pagedData;

    }

    @Transactional
    public Page<DocumentServiceObject> getUserDocumentsByState(String userName, DocumentState documentState, Pageable pageFormatDetails) {
        LOGGER.info("getUserDocumentsByState");
        List<DocumentServiceObject> filteredDocuments = document.getDocumentsBy(userName, documentState);
        PageImpl<DocumentServiceObject> pagedData = document.getPage(pageFormatDetails, filteredDocuments);
        LOGGER.info(" documents of user - " + userName + " with document state - " + documentState + " are being returned " +
                " , returning page - " + pageFormatDetails.getPageNumber() + "\n" + " size of page is "
                + pageFormatDetails.getPageSize() + " size of total data points  is - " + pagedData.getTotalElements());
        return pagedData;
    }

    @Transactional
    public Page<DocumentServiceObject> getDocumentsToApprove(String userName, Pageable pageFormatDetails) {
        LOGGER.info("getDocumentsToApprove");

        List<String> documentTypesUserCanAproove =
                document.getDocumentTypesUserCanAprooveBy(userName);
        List<DocumentServiceObject> documentsUserCanAproove =
                document.getDocumentsBy(documentTypesUserCanAproove, pageFormatDetails);
        // we need total ammount of documents to be displayed for pagination to work, thus we need second query.
        long documentCount = documentRepository.getDocumentsToApproveSize(documentTypesUserCanAproove);

        PageImpl<DocumentServiceObject> pagedData = new PageImpl<>(documentsUserCanAproove,
                pageFormatDetails, documentCount);
        LOGGER.info(" documents for approval of user - " + userName + " are being returned " +
                " , returning page - " + pageFormatDetails.getPageNumber() + "\n" + " size of page is "
                + pageFormatDetails.getPageSize() + " size of total data points  is - " + pagedData.getTotalElements());

        return pagedData;
    }

    @Transactional
    public Page<DocumentServiceObject> getDocumentsToApprove(String userName, Pageable pageFormatDetails, String filteringCriteria) {
        LOGGER.info("getDocumentsToApproveFiltered");

        List<String> documentTypesUserCanAproove = document.getDocumentTypesUserCanAprooveBy(userName);
        List<DocumentServiceObject> documentsUserCanAproove = document.getDocumentsBy(documentTypesUserCanAproove
                , pageFormatDetails, filteringCriteria);
        long filteredDocumentsCount = documentRepository.getDocumentsToApproveFilteredSize(documentTypesUserCanAproove
                , filteringCriteria);
        PageImpl<DocumentServiceObject> pageData = new PageImpl<>(documentsUserCanAproove,
                pageFormatDetails, filteredDocumentsCount);
        LOGGER.info(" filtering document of user - " + userName + " according to filteringCriteria - "
                + filteringCriteria + " search results are being returned " +
                " , page - " + pageFormatDetails.getPageNumber() + "\n" + " size of page is "
                + pageFormatDetails.getPageSize() + " size of total data points  is - " + pageData.getTotalElements());

        return pageData;
    }

}




