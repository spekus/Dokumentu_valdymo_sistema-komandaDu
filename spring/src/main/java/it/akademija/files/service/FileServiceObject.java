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

    // this constructor is used when we create a new File. As it is new, we generate unique identifier for it
    public FileServiceObject(String fileName, String contentType, String fileLocation, Long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLocation = fileLocation;
        this.size = size;
        generateUniqueIdentifier();
    }

    // this constructor is used when we create FileServiceObject from existing files, to show in document details
    // we intentionally do not set fileLocation, becaus we don't want our API users to know where in the file system
    // files are stored. This would disclose if we have Winodws or Linux on server and it is not needed
    // in frontend anyway
    public FileServiceObject(String fileName, String contentType, Long size, String identifier) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.identifier = identifier;
    }

    private void generateUniqueIdentifier (){
        this.identifier = this.fileName + (Math.random() * ((1000000000 - 0) + 1)) + 0;
        //(Math.random() * ((max - min) + 1)) + min
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
