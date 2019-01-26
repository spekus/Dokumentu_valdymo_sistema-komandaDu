package it.akademija.users.repository;

import it.akademija.documents.repository.DocumentEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class UserGroupEntity {
    @Id
    @GeneratedValue
    protected Long id;
    private String title;

    @OneToMany
    private List<DocumentEntity> documentToApprove;
    @OneToMany
    private List<DocumentEntity> documentToUpload;

    protected UserGroupEntity(){}

    public UserGroupEntity(String title) {
        this.title = title;
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

    public List<DocumentEntity> getDocumentToApprove() {
        return documentToApprove;
    }

    public void setDocumentToApprove(List<DocumentEntity> documentToApprove) {
        this.documentToApprove = documentToApprove;
    }

    public List<DocumentEntity> getDocumentToUpload() {
        return documentToUpload;
    }

    public void setDocumentToUpload(List<DocumentEntity> documentToUpload) {
        this.documentToUpload = documentToUpload;
    }
}
