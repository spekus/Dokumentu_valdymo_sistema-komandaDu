package it.akademija.documents.controller;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CreateDocumentCommand {

    @NotNull
    @Length(min=1, max=50)
    private String title;

    @NotNull
    @Length(min=1, max=100)
    private String description;

    @NotNull
    @Length(min=1, max=50)
    private String type;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
