package it.akademija.users.service;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.files.service.FileServiceObject;
import it.akademija.users.repository.UserEntity;

import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;

import it.akademija.users.repository.UserRepository;
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
    DocumentRepository documentRepository;
    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService() {
    }

    public UserService(UserRepository userRepository, DocumentRepository documentRepository, UserGroupRepository userGroupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
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
    public List<UserServiceObject> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserServiceObject> list = users.stream()
                .map(userEntity ->
                        SOfromEntity(userEntity))
                .collect(Collectors.toList());
        return list;
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
    public List<UserGroupServiceObject> getUserGroups(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        Set<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();

        return groupsUserBelongsTo.stream().map(userGroupEntity -> UserGroupService.SOfromEntity(userGroupEntity))
                .collect(Collectors.toList());
    }


    //Gets all user's document types that he can create documents
    @Transactional
    public Set<DocumentTypeServiceObject> getUserDocumentTypesHeCanCreate(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username);

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
    public List<UserServiceObject> getUserByCriteria(String criteria) {
        if (userRepository.findByUsernameOrLastname(criteria) != null) {
            return userRepository.findByUsernameOrLastname(criteria)
                    .stream()
                    .map(userEntity -> SOfromEntity(userEntity))
                    .collect(Collectors.toList());
        }
        return null;

    }

    @Transactional
    public Page<DocumentServiceObject> getDocumentsToApprove(String username, Integer page, Integer size) {

        List<DocumentTypeEntity> documentTypeEntityList =
                documentTypeRepository.getDocumentTypesToApproveByUsername(username);
        //take all document types which this particular user can aprove
        List<String> documentTypesForAproval = documentTypeEntityList.stream().map((documentTypeEntity) ->
                documentTypeEntity.getTitle()).collect(Collectors.toList());
        // create pageable settings, so that later query knows which part of database to search
        Pageable sortedByTitleDesc =
                PageRequest.of(page, size, Sort.by("title").ascending());

        //send query to get all needed document
        List<DocumentEntity> allDocumentsToApprove =
                documentRepository.getDocumentsToApprove(documentTypesForAproval, sortedByTitleDesc);
        // we need total ammount of documents to be displayed for pagination to work, thus we need second query.
        // this part is not efficient, would anyone know how to replace?
        long getTotalSize = documentRepository.getDocumentsToApproveSize(documentTypesForAproval);

        //conversion from one type to another
        List<DocumentServiceObject> listOfDocumentServiceObject =
                allDocumentsToApprove
                        .stream()
                        .map(documentEntity -> SOfromEntityWithoutFiles(documentEntity))
                        .collect(Collectors.toList());

        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(listOfDocumentServiceObject,
                sortedByTitleDesc, getTotalSize);
        return pageData;
    }

    @Transactional
    public Page<DocumentServiceObject> getDocumentsToApproveFiltered(String username, Integer page, Integer size, String criteria) {
        List<DocumentTypeEntity> documentTypeEntityList =
                documentTypeRepository.getDocumentTypesToApproveByUsername(username);
        List<String> documentTypesForAproval = documentTypeEntityList.stream().map((documentTypeEntity) ->
                documentTypeEntity.getTitle()).collect(Collectors.toList());
        Pageable sortedByTitleDesc =
                PageRequest.of(page, size, Sort.by("title").ascending());

        List<DocumentEntity> documentsToApproveFiltered = documentRepository.getDocumentsToApproveByCriteria(documentTypesForAproval,
                sortedByTitleDesc, criteria);

        List<DocumentServiceObject> listOfDocumentServiceObject =
                documentsToApproveFiltered
                        .stream()
                        .map(documentEntity -> SOfromEntityWithoutFiles(documentEntity))
                        .collect(Collectors.toList());

        long filteredDocumentsSize=documentRepository.getDocumentsToApproveFilteredSize(documentTypesForAproval,criteria);

        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(listOfDocumentServiceObject,
                sortedByTitleDesc, filteredDocumentsSize);

        return pageData;
    }

    @Transactional
    public void addNewUser(String firstname, String lastname, String username, String password) {
        UserEntity userEntityFromDataBase1 = userRepository.findUserByUsername(username);
        UserEntity userEntityFromDataBase2 = userRepository.findUserByUsername(username);
        if (userEntityFromDataBase1 == null && userEntityFromDataBase2 == null) {
            UserEntity userEntity = new UserEntity(firstname, lastname, username, passwordEncoder.encode(password));
            userRepository.save(userEntity);
        }

    }

    @Transactional
    public void updateUserPassword(String username, String password) {
        UserEntity savedUserEntity = userRepository.findUserByUsername(username);
        savedUserEntity.setPassword(passwordEncoder.encode(password));
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);
    }

    @Transactional
    public void updateUserInformation(String username, String newFirstname, String newLastname) {
        UserEntity savedUserEntity = userRepository.findUserByUsername(username);
        savedUserEntity.setFirstname(newFirstname);
        savedUserEntity.setLastname(newLastname);
        UserEntity updateUserEntity = userRepository.save(savedUserEntity);

    }

    @Transactional
    @Modifying
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
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

    private DocumentServiceObject SOfromEntity(DocumentEntity entity) {
        DocumentServiceObject so = new DocumentServiceObject();

        so.setApprovalDate(entity.getApprovalDate());
        so.setApprover(entity.getApprover());
        so.setAuthor(entity.getAuthor());
        so.setDescription(entity.getDescription());
        so.setDocumentIdentifier(entity.getDocumentIdentifier());
        so.setDocumentState(entity.getDocumentState());
        so.setPostedDate(entity.getPostedDate());
        so.setRejectedDate(entity.getRejectedDate());
        so.setRejectedReason(entity.getRejectionReason());
        so.setTitle(entity.getTitle());
        so.setType(entity.getType());


        so.setFilesAttachedToDocument(entity.getFileSet()
                .stream()
                .map(file -> new FileServiceObject(file.getFileName(), file.getContentType(), file.getSize(), file.getIdentifier()))
                .collect(Collectors.toSet()));
        return so;
    }

    private DocumentServiceObject SOfromEntityWithoutFiles(DocumentEntity entity) {
        DocumentServiceObject so = new DocumentServiceObject();

        so.setApprovalDate(entity.getApprovalDate());
        so.setApprover(entity.getApprover());
        so.setAuthor(entity.getAuthor());
        so.setDescription(entity.getDescription());
        so.setDocumentIdentifier(entity.getDocumentIdentifier());
        so.setDocumentState(entity.getDocumentState());
        so.setPostedDate(entity.getPostedDate());
        so.setRejectedDate(entity.getRejectedDate());
        so.setRejectedReason(entity.getRejectionReason());
        so.setTitle(entity.getTitle());
        so.setType(entity.getType());
        return so;
    }

    @Transactional
    public Page<DocumentServiceObject> getAllUserDocuments(String userIdentifier, int page, int size) {


        Pageable sortedByTitleDesc =
                PageRequest.of(page, size, Sort.by("title").ascending());

        List<DocumentServiceObject> listOfDocumentServiceObject = documentRepository.findByAuthor(userIdentifier, sortedByTitleDesc)
                .stream()
                .map(documentEntity -> SOfromEntity(documentEntity))
                .collect(Collectors.toList());
        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(listOfDocumentServiceObject,
                sortedByTitleDesc, documentRepository.findByAuthor(userIdentifier).size());
        return pageData;

    }

    @Transactional
    public List<DocumentEntity> getAllUserDocuments(String userName) {


        return documentRepository.findByAuthor(userName);
    }
////                .stream()
////                .map(documentEntity -> SOfromEntity(documentEntity))
////                .collect(Collectors.toSet());
//
////        List<DocumentServiceObject>  listOfDocumentServiceObject= documentRepository.findByAuthor(userIdentifier, sortedByTitleDesc)
////                .stream()
////                .map(documentEntity -> SOfromEntity(documentEntity))
////                .collect(Collectors.toList());
////        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(listOfDocumentServiceObject,
////                sortedByTitleDesc, documentRepository.findByAuthor(userIdentifier).size()) ;
////        return null;
//
//    }


    @Transactional
    public Page<DocumentServiceObject> getUserDocumentsByState(String userName, DocumentState state, int page, int size) {
        // pasitikrinam ar yra toks naudotojas
        UserEntity userEntity = userRepository.findUserByUsername(userName);

        if (userEntity == null) {
            throw new IllegalArgumentException("User with username '" + userName + "' does not exits.");
        }
        Pageable sortedByTitleDesc =
                PageRequest.of(page, size, Sort.by("title").ascending());

        List<DocumentServiceObject> listOfDocumentServiceObject = documentRepository.findByDocumentStateAndAuthor(state, userName, sortedByTitleDesc)
                .stream()
                .map(documentEntity -> SOfromEntity(documentEntity))
                .collect(Collectors.toList());
        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(listOfDocumentServiceObject,
                sortedByTitleDesc, documentRepository.findByDocumentStateAndAuthor(state, userName).size());
        return pageData;
    }


}




