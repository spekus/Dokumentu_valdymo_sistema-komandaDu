package it.akademija.files.service;



import it.akademija.documents.service.DocumentService;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    @Autowired
    DocumentService documentService;

    @Transactional
    public String addFileToDataBase(MultipartFile multipartFile, String userName) throws Exception {

        File uploadingLocation = uploadFileToLocalServer(multipartFile, userName); //uploads file to the server
//        System.out.println("FILE LOCATION - " + uploadingLocation.getAbsolutePath());
        //creating and saving data base entity
        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename());
        fileEntity.setFileLocation(uploadingLocation.getAbsolutePath()); //changed this
        fileEntity.setContentType(multipartFile.getContentType());
        fileEntity.setSize(multipartFile.getSize());

        fileRepository.save(fileEntity);
        return fileEntity.getIdentifier();

    }

    @Transactional
    public File uploadFileToLocalServer(MultipartFile file, String name) throws Exception {
        try {
            // this is for later naming each saved file, so that files with identical name would not be named identically
            String time = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String currentUsersHomeDir = System.getProperty("user.home");
            File fileLocation = new File(currentUsersHomeDir + File.separator + "tmpDocs"
                    + File.separator + name + File.separator + file.getOriginalFilename() + time);
            File generalLocation = new File(currentUsersHomeDir + File.separator + "tmpDocs");
            File fileLocationDirectory = new File(currentUsersHomeDir + File.separator + "tmpDocs"
                    + File.separator + name);

            //if directory not created it creates one. and it SHOULD make directory writable for all users meaning to more need for chmod
            if (!fileLocationDirectory.isDirectory()) {
                System.out.println(generalLocation.mkdir());
                System.out.println(generalLocation.setWritable(true));
                System.out.println(fileLocationDirectory.mkdir());
                System.out.println(fileLocationDirectory.setWritable(true));
            }
            System.out.println("File location is    -  " + fileLocation.getAbsolutePath());


            file.transferTo(fileLocation);
            return fileLocation;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("issue with uploading file to local directory  " +
                    ", exception message - " + e.getMessage());
        }
    }

    @Transactional
    //Finds file in database and converts it to object
    public FileServiceObject findFile(String identifier) {

        if (identifier.isEmpty() || identifier == null || fileRepository.getFileByIdentifier(identifier) == null) {
            throw new IllegalArgumentException("ERROR no valid File identifier provided!!");
        }
        if (identifier != null && !identifier.isEmpty()) {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            FileServiceObject fileServiceObject = new FileServiceObject();
            fileServiceObject.setContentType(fileEntity.getContentType());
            fileServiceObject.setFileLocation(fileEntity.getFileLocation());
            fileServiceObject.setFileName(fileEntity.getFileName());
            fileServiceObject.setSize(fileEntity.getSize());
            fileServiceObject.setIdentifier(fileEntity.getIdentifier());
            return fileServiceObject;
        } else {
            throw new IllegalArgumentException("ERROR no valid File identifier provided");
        }
    }

    @Transactional
    //Finds file in database and converts it to object
    public FileEntity findFileEntity(String identifier) {
        if (identifier != null && !identifier.isEmpty()) {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            return fileEntity;
        } else {
            throw new IllegalArgumentException("ERROR no valid File identifier provided");
        }
    }

    @Transactional
    public void addFileToDocument(String fileIdentifier, String documentIdentifier) {
        FileEntity fileEntity =
                findFileEntity(fileIdentifier);
        documentService.addFileToDocument(documentIdentifier, fileEntity);
    }


    public ArrayList<String> getAllFileIdentifiers(String documentIdentifier) {
        return null;
    }



}
