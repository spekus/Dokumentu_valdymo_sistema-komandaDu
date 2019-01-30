package it.akademija.users.repository;


import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentTypeEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.Set;

@Entity
public class UserGroupEntity {
    @Id
    @GeneratedValue
    protected Long id;
    private String title;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<DocumentTypeEntity> availableDocumentTypesToApprove;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)

    private Set<DocumentTypeEntity> availableDocumentTypesToUpload;
    @OneToMany
    Set<DocumentEntity> documentsToApprove;


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

    public Set<DocumentTypeEntity> getAvailableDocumentTypesToApprove() {
        return availableDocumentTypesToApprove;
    }

    public void setAvailableDocumentTypesToApprove(Set<DocumentTypeEntity> availableDocumentTypesToApprove) {
        this.availableDocumentTypesToApprove = availableDocumentTypesToApprove;
    }

    public Set<DocumentTypeEntity> getAvailableDocumentTypesToUpload() {
        return availableDocumentTypesToUpload;
    }

    public void setAvailableDocumentTypesToUpload(Set<DocumentTypeEntity> availableDocumentTypesToUpload) {
        this.availableDocumentTypesToUpload = availableDocumentTypesToUpload;
    }

    public Set<DocumentEntity> getDocumentsToApprove() {
        return documentsToApprove;
    }

    public void setDocumentsToApprove(Set<DocumentEntity> documentsToApprove) {
        this.documentsToApprove = documentsToApprove;
    }


}