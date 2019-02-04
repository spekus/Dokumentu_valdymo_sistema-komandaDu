package it.akademija.files.service;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FileDocumentCommand {
    @NotNull
    private String FileIdentifier;

    @NotNull
    private String DocumentIdentifier;


}
