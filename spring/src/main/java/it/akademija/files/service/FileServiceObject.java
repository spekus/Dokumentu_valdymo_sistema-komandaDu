package it.akademija.files.service;

public class FileServiceObject {

    private String fileName;

    private String contentType;

    private String fileLocation;

    private Long size;

    private String identifier;


    public FileServiceObject() {
        generateUniqueIdentifier();
    }

    public FileServiceObject(String fileName, String contentType, String fileLocation, Long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLocation = fileLocation;
        this.size = size;
        generateUniqueIdentifier();
    }

    public FileServiceObject(String fileName, String contentType, Long size, String identifier) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.identifier = identifier;
    }

    private void generateUniqueIdentifier() {
        this.identifier = this.fileName + (Math.random() * ((1000000000 - 0) + 1)) + 0;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
