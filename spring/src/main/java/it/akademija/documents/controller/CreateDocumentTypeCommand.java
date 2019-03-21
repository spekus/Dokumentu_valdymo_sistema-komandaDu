package it.akademija.documents.controller;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CreateDocumentTypeCommand {

    @NotNull
    @Length(min = 1, max = 50)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
