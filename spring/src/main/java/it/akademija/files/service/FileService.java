package it.akademija.files.service;


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

    @Transactional
    public void addFileToDataBase(MultipartFile multipartFile) {

        File uploadingLocation = uploadFileToLocalServer(multipartFile); //uploads file to the server

        //creating and saving data base entity
        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename());
        fileEntity.setFileLocation(uploadingLocation.getAbsolutePath());
        fileEntity.setContentType(multipartFile.getContentType());
        fileEntity.setSize(multipartFile.getSize());
        fileRepository.save(fileEntity);

    }

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


    public FileServiceObject findFile(String identifier) {
        FileEntity fileEntity = fileRepository.getFileByFileName(identifier);
        FileServiceObject fileServiceObject = new FileServiceObject();
        fileServiceObject.setContentType(fileEntity.getContentType());
        fileServiceObject.setFileLocation(fileEntity.getFileLocation());
        fileServiceObject.setFileName(fileEntity.getFileName());
        fileServiceObject.setSize(fileEntity.getSize());
        return fileServiceObject;
    }
}



