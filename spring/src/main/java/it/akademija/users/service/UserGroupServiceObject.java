package it.akademija.users.service;

import it.akademija.auth.AppRoleEnum;
import it.akademija.documents.service.DocumentTypeServiceObject;

import java.util.HashSet;
import java.util.Set;

public class UserGroupServiceObject {

    private String title;

    private AppRoleEnum role;

    private Set<DocumentTypeServiceObject> typesToUpload = new HashSet<>();

    private Set<DocumentTypeServiceObject> typesToApprove = new HashSet<>();

    public UserGroupServiceObject() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AppRoleEnum getRole() {
        return role;
    }

    public void setRole(AppRoleEnum role) {
        this.role = role;
    }

    public Set<DocumentTypeServiceObject> getTypesToApprove() {
        return typesToApprove;
    }

    public void setTypesToApprove(Set<DocumentTypeServiceObject> typesToApprove) {
        this.typesToApprove = typesToApprove;
    }

    public Set<DocumentTypeServiceObject> getTypesToUpload() {
        return typesToUpload;
    }

    public void setTypesToUpload(Set<DocumentTypeServiceObject> typesToUpload) {
        this.typesToUpload = typesToUpload;
    }
}
