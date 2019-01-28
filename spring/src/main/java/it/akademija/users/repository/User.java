package it.akademija.users.repository;

import it.akademija.documents.MyGenerator;
import it.akademija.documents.repository.Document;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true, nullable=false)
    @GeneratedValue(generator= MyGenerator.generatorName)
    @GenericGenerator(name = MyGenerator.generatorName, strategy = "a.b.c.MyGenerator")
    private String userIdentifier;
    private String username;
    private String firstname;
    private String lastname;

    @OneToMany
    private List<Document> documents;

    public User () {

    }

    public User (String username, String firstname, String lastname) {
        this.username=username;
        this.firstname=firstname;
        this.lastname=lastname;
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

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        this.documents.add(document);
    }
}
