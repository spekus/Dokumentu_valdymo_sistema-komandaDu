package it.akademija.documents.repository;

import javax.persistence.*;

@Entity
public class DocumentTypeEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String title;

    public DocumentTypeEntity () {

    }

    public DocumentTypeEntity (String title) {
        this.title=title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
