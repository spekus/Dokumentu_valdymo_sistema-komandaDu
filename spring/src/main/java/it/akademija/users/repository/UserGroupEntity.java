package it.akademija.users.repository;


import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentTypeEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    private String title;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<DocumentTypeEntity> availableDocumentTypesToApprove = new HashSet<>();

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<DocumentTypeEntity> availableDocumentTypesToUpload = new HashSet<>();
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<DocumentEntity> documentsToApprove= new HashSet<>();
    @ManyToMany(mappedBy = "userGroups")
    private Set<UserEntity> groupUsers=new HashSet<>();


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

    public void addAvailableDocumentTypeToUpload (DocumentTypeEntity documentTypeEntity) {
        this.availableDocumentTypesToUpload.add(documentTypeEntity);
    }

    public void addAvailableDocumentTypeToApprove (DocumentTypeEntity documentTypeEntity) {
        this.availableDocumentTypesToApprove.add(documentTypeEntity);
    }

    public void addDocumentsToApprove(DocumentEntity documentEntity) {
        this.documentsToApprove.add(documentEntity);
    }

    public Set<UserEntity> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(Set<UserEntity> groupUsers) {
        this.groupUsers = groupUsers;
    }
}