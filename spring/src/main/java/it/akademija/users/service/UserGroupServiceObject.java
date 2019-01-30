package it.akademija.users.service;

public class UserGroupServiceObject {

    private String title;

    public UserGroupServiceObject(){

    }

    public UserGroupServiceObject(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
