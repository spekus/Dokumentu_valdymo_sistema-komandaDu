package it.akademija.users.service;

import it.akademija.users.repository.UserGroupEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserServiceObject {

    private String userIdentifier;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private List<UserGroupEntity> userGroups=new ArrayList<>();


    public UserServiceObject() {
    }

    public UserServiceObject(String userIdentifier, String firstname, String lastname, String username, String password) {
        this.userIdentifier = userIdentifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public UserServiceObject(String userIdentifier, String firstname, String lastname, String username) {
        this.userIdentifier = userIdentifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;

    }

    public UserServiceObject(String userIdentifier, String firstname, String lastname, String username, String password, List<UserGroupEntity> userGroups) {
        this.userIdentifier = userIdentifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.userGroups = userGroups;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserGroupEntity> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroupEntity> userGroups) {
        this.userGroups = userGroups;
    }
}
