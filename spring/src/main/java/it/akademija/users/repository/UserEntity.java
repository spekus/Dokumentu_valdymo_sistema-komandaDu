package it.akademija.users.repository;

import it.akademija.documents.repository.DocumentEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
//    @GeneratedValue(generator= MyGenerator.generatorName)
//    @GenericGenerator(name = MyGenerator.generatorName, strategy = "a.b.c.MyGenerator")
    private String userIdentifier;
    private String firstname;
    private String lastname;
    private String username;
    @NotBlank
    private String password;

    @OneToMany
    private Set<DocumentEntity> userDocumentEntities;

    @OneToMany
    private Set<UserGroupEntity> userGroups;


    public UserEntity() {}

    public UserEntity(String userIdentifier, String firstname, String lastname, String username, String password) {
        this.userIdentifier = userIdentifier;
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

    public Set<DocumentEntity> getUserDocumentEntities() {
        return userDocumentEntities;
    }

    public void setUserDocumentEntities(Set<DocumentEntity> userDocumentEntities) {
        this.userDocumentEntities = userDocumentEntities;
    }

    public Set<UserGroupEntity> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroupEntity> userGroups) {
        this.userGroups = userGroups;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addDocument(DocumentEntity documentEntity) {
        this.userDocumentEntities.add(documentEntity);
    }
}
