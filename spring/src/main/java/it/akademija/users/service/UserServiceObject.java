package it.akademija.users.service;

import it.akademija.users.repository.UserGroupEntity;
import java.util.Set;

public class UserServiceObject {
    private String name;
    private String surname;
    private Set<UserGroupEntity> userGroups;

    public UserServiceObject() {
    }

    public UserServiceObject(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<UserGroupEntity> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroupEntity> userGroups) {
        this.userGroups = userGroups;
    }
}
