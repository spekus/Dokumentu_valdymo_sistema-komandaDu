package it.akademija.users.controller;

import it.akademija.auth.AppRoleEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class CreateUserGroupCommand {

    @NotNull
    @Length(min=1, max=50)
    private String title;

    @NotNull
    private AppRoleEnum role;

    public CreateUserGroupCommand(String title, AppRoleEnum role) {
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
