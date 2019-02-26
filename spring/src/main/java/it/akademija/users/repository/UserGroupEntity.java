package it.akademija.users.repository;


import it.akademija.auth.AppRoleEnum;
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
    protected long id;
    private String title;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "usergroup_types_to_upload",
            joinColumns = @JoinColumn(name = "usergroup_id"),
            inverseJoinColumns = @JoinColumn(name = "documenttype_id"))
    private Set<DocumentTypeEntity> availableDocumentTypesToUpload = new HashSet<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "usergroup_types_to_approve",
            joinColumns = @JoinColumn(name = "usergroup_id"),
            inverseJoinColumns = @JoinColumn(name = "documenttype_id"))
    private Set<DocumentTypeEntity> availableDocumentTypesToApprove = new HashSet<>();

    @ManyToMany(mappedBy = "userGroups")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<UserEntity> groupUsers = new HashSet<>();

    protected UserGroupEntity() {
    }

    public UserGroupEntity(String title, AppRoleEnum role) {
        this.title = title;
        this.role = role;
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

     public void addAvailableDocumentTypeToUpload(DocumentTypeEntity documentTypeEntity) {
        this.availableDocumentTypesToUpload.add(documentTypeEntity);
    }

    public void addAvailableDocumentTypeToApprove(DocumentTypeEntity documentTypeEntity) {
        this.availableDocumentTypesToApprove.add(documentTypeEntity);
    }

    public void removeAvailableDocumentTypeToUpload(DocumentTypeEntity documentTypeEntity) {
        this.availableDocumentTypesToUpload.remove(documentTypeEntity);

    }

    public void removeAvailableDocumentTypeToApprove(DocumentTypeEntity documentTypeEntity) {
        this.availableDocumentTypesToApprove.remove(documentTypeEntity);
    }

    public Set<UserEntity> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(Set<UserEntity> groupUsers) {
        this.groupUsers = groupUsers;
    }

    @Enumerated(EnumType.STRING)
    private AppRoleEnum role;

    public AppRoleEnum getRole() {
        return role;
    }

    public void setRole(AppRoleEnum role) {
        this.role = role;
    }
}