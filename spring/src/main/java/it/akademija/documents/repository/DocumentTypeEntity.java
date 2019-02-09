package it.akademija.documents.repository;

import javax.persistence.*;

@Entity
public class DocumentTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    public DocumentTypeEntity () {

    }

    public DocumentTypeEntity (String title) {
        this.title=title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
