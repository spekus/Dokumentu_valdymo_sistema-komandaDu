package it.akademija.users.controller;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CreateUserCommand {

    @NotNull
    @Length(min=1, max=50)
    private String userIdentifier;

    @NotNull
    @Length(min=1, max=50)
    private String username;

    @NotNull
    @Length(min=1, max=50)
    private String firstname;

    @NotNull
    @Length(min=1, max=50)
    private String lastname;

    @NotNull
    @Length(min=1, max=50)
    private String password;

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
