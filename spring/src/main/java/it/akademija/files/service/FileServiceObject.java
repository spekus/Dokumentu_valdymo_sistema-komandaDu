package it.akademija.files.service;

public class FileServiceObject {

    private String fileName;

    private String contentType;

    private String fileLocation;

    private Long size;


    public FileServiceObject() {}

    public FileServiceObject(String fileName, String contentType, String fileLocation, Long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLocation = fileLocation;
        this.size = size;
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
