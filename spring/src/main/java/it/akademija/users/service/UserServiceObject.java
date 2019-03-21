package it.akademija.users.service;

        import java.util.HashSet;
        import java.util.Set;

public class UserServiceObject {

    private String userIdentifier;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private Set<UserGroupServiceObject> userGroups = new HashSet<>();


    public UserServiceObject() {
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserGroupServiceObject> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<UserGroupServiceObject> userGroups) {
        this.userGroups = userGroups;
    }
}
