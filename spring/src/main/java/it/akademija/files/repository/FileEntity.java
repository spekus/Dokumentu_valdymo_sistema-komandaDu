package it.akademija.files.repository;

import javax.persistence.*;

@Entity
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    private String fileLocation;

    private Long size;

//
//    @Lob
//    private byte[] data;

    public FileEntity() {
    }

    public FileEntity(String fileName) {
        this.fileName = fileName;
    }

    public FileEntity(String fileName, String contentType, String fileLocation, Long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLocation = fileLocation;
        this.size = size;


    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

//    public byte[] getData() {
//        return data;
//    }
//
//    public void setData(byte[] data) {
//        this.data = data;
//    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}