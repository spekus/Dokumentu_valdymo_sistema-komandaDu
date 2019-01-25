package it.akademija.files.service;

public class FileServiceObject {

    private String fileName;

    private String contentType;

    private String fileLocation;

    public FileServiceObject(String fileName, String contentType, String fileLocation) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLocation = fileLocation;
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
}
