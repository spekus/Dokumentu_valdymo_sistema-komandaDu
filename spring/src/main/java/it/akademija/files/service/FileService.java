package it.akademija.files.service;


import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.FileObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    @Autowired
    DocumentService documentService;

    @Transactional
    public String addFileToDataBase(MultipartFile multipartFile) throws Exception {

        File uploadingLocation = uploadFileToLocalServer(multipartFile); //uploads file to the server
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
    public File uploadFileToLocalServer(MultipartFile file) throws Exception{

        try {
//            File fileLocation = new File(File.separator + "home"
//                    + File.separator + "augustas" + File.separator + "tmpDocs" + File.separator
//                    + file.getOriginalFilename());
//

//                File fileLocation = new File( ".." + File.separator + ".." + File.separator +".." + File.separator
//                        + ".." + File.separator +".." + File.separator  + "tmpDocs" + File.separator  +  file.getOriginalFilename());



//              for this to work create folder named - tmpDocs in relevant location
//              than in console run sudo chmod -R 777 tmpDocs , so that folder is accessible

            String currentUsersHomeDir = System.getProperty("user.home");
            File fileLocation = new File(currentUsersHomeDir + File.separator  + "tmpDocs" + File.separator  +  file.getOriginalFilename());
            File fileLocationDirectory = new File(currentUsersHomeDir + File.separator  + "tmpDocs");

            //if directory not created it creates one. and it SHOULD make directory writable for all users meaning to more need for chmod
            if(!fileLocationDirectory.isDirectory()){
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

        if (identifier.isEmpty() || identifier==null || fileRepository.getFileByIdentifier(identifier) == null) {
            throw new IllegalArgumentException("ERROR no valid File identifier provided!!");
        }
        if (identifier!=null && !identifier.isEmpty()) {
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
        if (identifier!=null && !identifier.isEmpty()) {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            return fileEntity;
        }
        else{
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


//    @Transactional
//    public FileObject downloadFileFromLocalServer(String identifier) {
//        FileServiceObject fileObject = findFile(identifier);
//        return fileObject;
//        File file = new File(
////                ".." + File.separator + ".." + File.separator +".." + File.separator
////                + ".." + File.separator +".." + File.separator  +
//                fileObject.getFileLocation());
//        System.out.println("FILE LOCATION  " + file.getAbsolutePath());
//        InputStreamResource resource = null;
//        try {
//            resource = new InputStreamResource(new FileInputStream(file));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //        HttpHeaders header = new HttpHeaders();
////
////        header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
////        header.setContentLength(fileEntity.getData().length);
////        header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());
//        System.out.println("File name = " + fileObject.getFileName());
//        System.out.println("File location = " + fileObject.getFileLocation());
//        System.out.println("File type = " + fileObject.getContentType());
//        System.out.println("File size = " + fileObject.getSize());
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment;filename=" + fileObject.getFileName())
//                .contentType(MediaType.valueOf(fileObject.getContentType())).contentLength(fileObject.getSize())
//                .body(resource);
//    }
}



