package it.akademija.users.service;

import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserGroupService {

    @Autowired
    private UserGroupRepository userGroupRepository;

    public UserGroupService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    public UserGroupRepository getUserGroupRepository() {
        return userGroupRepository;
    }

    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Transactional
    public void addNewUserGroup(UserGroupServiceObject userGroupServiceObject) {
        UserGroupEntity userGroupEntity = new UserGroupEntity(userGroupServiceObject.getTitle());
        UserGroupEntity userGroupEntityFromDataBase = userGroupRepository.findGroupByTitle(userGroupServiceObject.getTitle());
        if (userGroupEntityFromDataBase == null) {
            userGroupRepository.save(userGroupEntity);
        }
    }

    @Transactional
    public UserGroupServiceObject getGroupByTitle(String title) {
        UserGroupEntity userGroupEntity = userGroupRepository.findGroupByTitle(title);
        if (userGroupEntity != null) {
            UserGroupServiceObject userGroupServiceObject = new UserGroupServiceObject(userGroupEntity.getTitle());
            return userGroupServiceObject;
        }
        return null;
    }

    @Transactional
    public Collection<UserGroupServiceObject> getAllGroups() {
        return userGroupRepository.
                findAll()
                .stream()
                .map(userGroupEntity -> new UserGroupServiceObject(userGroupEntity.getTitle()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Modifying
    public void deleteGroupByTitle(String title) {
        userGroupRepository.deleteGroupByTitle(title);
    }

    @Transactional
    public void updateGroupByTitle(String title) {
        UserGroupEntity savedUserGroupEntity=userGroupRepository.findGroupByTitle(title);
        savedUserGroupEntity.setTitle(title);
        UserGroupEntity updateUserGroupEntit=userGroupRepository.save(savedUserGroupEntity);
    }
}


