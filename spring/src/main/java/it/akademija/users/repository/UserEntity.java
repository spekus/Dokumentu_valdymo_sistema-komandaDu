package it.akademija.users.repository;

import it.akademija.documents.MyGenerator;
import it.akademija.documents.repository.DocumentEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    @Column(unique=true, nullable=false)
//    @GeneratedValue(generator= MyGenerator.generatorName)
//    @GenericGenerator(name = MyGenerator.generatorName, strategy = "a.b.c.MyGenerator")
    private String userIdentifier;
    private String username;
    private String firstname;
    private String lastname;
    private String password;

    @OneToMany
    private Set<DocumentEntity> documentEntities=new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    private Set<UserGroupEntity> userGroups=new HashSet<>();

    public UserEntity() {

    }

    public UserEntity(String userIdentifier, String username, String firstname, String lastname, String password) {
        this.userIdentifier=userIdentifier;
        this.username=username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.password=password;
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

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
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

    //    public Set<UserGroupEntity> getUserGroups() {
//        return userGroups;
//    }
//
//    public void setUserGroups(Set<UserGroupEntity> userGroups) {
//        this.userGroups = userGroups;
//    }
//
//    public void addGroupToUser(UserGroupEntity userGroupEntity) {
//        this.userGroups.add(userGroupEntity);
//    }


}
