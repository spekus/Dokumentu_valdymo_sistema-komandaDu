package it.akademija.users.repository;

import javax.persistence.*;

@Entity
public class UserLoginDataEntity {
    @Id
    @GeneratedValue
    protected Long id;
    private String login;
    private String password;
    @OneToOne(cascade= CascadeType.ALL)
    private UserEntity user;

    protected UserLoginDataEntity(){}

    public UserLoginDataEntity(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
