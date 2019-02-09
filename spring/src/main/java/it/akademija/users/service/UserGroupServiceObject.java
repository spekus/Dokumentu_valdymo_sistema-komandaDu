package it.akademija.users.service;

import it.akademija.auth.AppRoleEnum;

public class UserGroupServiceObject {

    private String title;

    private AppRoleEnum role;

    public UserGroupServiceObject(){

    }

    public UserGroupServiceObject(String title, AppRoleEnum role) {
        this.title = title;
        this.role = role;
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
}
