package it.akademija.files.repository;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;

@Entity
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName;

    private String contentType;

    private String fileLocation;

    private Long size;
//
//    @UniqueElements //crashina sitas, reikia kazkaip patikrin kad butu unique
    private String identifier;

//
//    @Lob
//    private byte[] data;

    public FileEntity() {
        generateUniqueIdentifier();
    }

    public FileEntity(String fileName) {
        this.fileName = fileName;
        generateUniqueIdentifier();
    }

    public FileEntity(String fileName, String contentType, String fileLocation, Long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLocation = fileLocation;
        this.size = size;
        generateUniqueIdentifier();

    }

    private void generateUniqueIdentifier (){
        this.identifier = this.fileName + (Math.random() * ((1000000000 - 0) + 1)) + 1;
        //(Math.random() * ((max - min) + 1)) + min
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}