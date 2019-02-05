package it.akademija.files.service;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FileDocumentCommand {
    @NotNull
    private String FileIdentifier;

    @NotNull
    private String DocumentIdentifier;

    public String getFileIdentifier() {
        return FileIdentifier;
    }

    public void setFileIdentifier(String fileIdentifier) {
        FileIdentifier = fileIdentifier;
    }

    public String getDocumentIdentifier() {
        return DocumentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        DocumentIdentifier = documentIdentifier;
    }
}
