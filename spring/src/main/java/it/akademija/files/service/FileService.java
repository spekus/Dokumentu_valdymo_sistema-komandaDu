package it.akademija.files.service;


import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    @Autowired
    DocumentService documentService;

    @Transactional
    public String addFileToDataBase(MultipartFile multipartFile) {

        File uploadingLocation = uploadFileToLocalServer(multipartFile); //uploads file to the server

        //creating and saving data base entity
        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename());
        fileEntity.setFileLocation(uploadingLocation.getAbsolutePath());
        fileEntity.setContentType(multipartFile.getContentType());
        fileEntity.setSize(multipartFile.getSize());

        fileRepository.save(fileEntity);
        return fileEntity.getIdentifier();

    }
    @Transactional
    public File uploadFileToLocalServer(MultipartFile file) {

        try {
            File fileLocation = new File(File.separator + "home"
                    + File.separator + "augustas" + File.separator + "tmpDocs" + File.separator
                    + file.getOriginalFilename());

            file.transferTo(fileLocation);
            return fileLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return new File(File.separator + "home"
                    + File.separator + "augustas" + File.separator + "tmpDocs");
        }
    }

    @Transactional
    //Finds file in database and converts it to object
    public FileServiceObject findFile(String identifier) {
        if (!identifier.isEmpty() && identifier!=null) {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            FileServiceObject fileServiceObject = new FileServiceObject();
            fileServiceObject.setContentType(fileEntity.getContentType());
            fileServiceObject.setFileLocation(fileEntity.getFileLocation());
            fileServiceObject.setFileName(fileEntity.getFileName());
            fileServiceObject.setSize(fileEntity.getSize());
            fileServiceObject.setIdentifier(fileEntity.getIdentifier());
            return fileServiceObject;
        }
        else{
            throw new IllegalArgumentException("ERROR no valid File identifier provided");
        }
    }

    @Transactional
    //Finds file in database and converts it to object
    public FileEntity findFileEntity(String identifier) {
        if (!identifier.isEmpty() && identifier!=null) {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            return fileEntity;
        }
        else{
            throw new IllegalArgumentException("ERROR no valid File identifier provided");
        }
    }

    @Transactional
    public void addFileToDocument(String fileIdentifier, String documentIdentifier) {
        DocumentServiceObject documentServiceObject =
                documentService.getDocumentByDocumentIdentifier(documentIdentifier);
        if(documentServiceObject.equals(null)){
            throw new IllegalArgumentException("ERROR , identifier for file does not exist");
        }
        FileEntity fileEntity =
                findFileEntity(fileIdentifier);
        documentService.addFileToDocument(documentIdentifier, fileEntity);
    }




}



