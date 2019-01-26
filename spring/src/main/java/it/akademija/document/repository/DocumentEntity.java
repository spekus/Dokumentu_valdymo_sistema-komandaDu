package it.akademija.document.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DocumentEntity {
    @Id
    @GeneratedValue
    protected Long id;

    private String documentType;
    private String documentTitle;

    protected DocumentEntity(){}

    public DocumentEntity(String documentType, String documentTitle) {
        this.documentType = documentType;
        this.documentTitle = documentTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }
}
