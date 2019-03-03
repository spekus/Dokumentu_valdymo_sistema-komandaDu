package it.akademija.users.repository;


import it.akademija.documents.repository.DocumentEntity;
import org.h2.engine.Role;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;


@Entity
@Table(indexes = {
        @Index(columnList = "username"),

})
public class UserEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username", unique=true)
    private String username;
    private String firstname;
    private String lastname;

    private String password;


    @OneToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<DocumentEntity> documentEntities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_usergroup",
            joinColumns = @JoinColumn(name = "userEntity_id"),
            inverseJoinColumns = @JoinColumn(name = "userGroupEntity_id"))
    private Set<UserGroupEntity> userGroups = new HashSet<>();


    public UserEntity() {
    }

    public UserEntity(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

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

    public Set<DocumentEntity> getDocuments() {
        return documentEntities;
    }

    public void setDocuments(Set<DocumentEntity> documentEntities) {
        this.documentEntities = documentEntities;
    }

    public void addDocument(DocumentEntity documentEntity) {
        this.documentEntities.add(documentEntity);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<DocumentEntity> getDocumentEntities() {
        return documentEntities;
    }

    public void setDocumentEntities(Set<DocumentEntity> documentEntities) {
        this.documentEntities = documentEntities;
    }

    public Set<UserGroupEntity> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroupEntity> userGroups) {
        this.userGroups = userGroups;
    }


}
