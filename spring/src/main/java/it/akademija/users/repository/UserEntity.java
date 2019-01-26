package it.akademija.users.repository;

import javax.persistence.*;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue
    protected Long id;
    private String name;
    private String surname;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<UserGroupEntity> userGroups;


    protected UserEntity() {
    }

    public UserEntity(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

