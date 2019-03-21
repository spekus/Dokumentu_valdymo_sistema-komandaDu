package it.akademija.files.service;


import it.akademija.documents.service.DocumentService;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import it.akademija.helpers.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private FileHelper file;

    private Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    @Transactional
    public String uploadFile(MultipartFile multipartFile, String userName) throws Exception {
        LOGGER.info(userName + " is uploading file with name - " + multipartFile.getName());
        File fileLocationOnServer = file.uploadFileToLocalServer(multipartFile, userName);
        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename());
        fileEntity.setFileLocation(fileLocationOnServer.getAbsolutePath());
        fileEntity.setContentType(multipartFile.getContentType());
        fileEntity.setSize(multipartFile.getSize());
        fileRepository.save(fileEntity);
        LOGGER.info("File has been uploaded to location - " + fileLocationOnServer.toString() + " by "  + userName );
        return fileEntity.getIdentifier();
    }

    @Transactional
    public FileServiceObject findFile(String identifier) {
        LOGGER.info("Trying to find file by identifier - " + identifier);
        if (identifier==null || identifier.isEmpty() || fileRepository.getFileByIdentifier(identifier) == null) {
            throw new IllegalArgumentException("Klaida - nepateiktas teisingas failo identifikacinis numeris");
        }else {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            return file.SOfromEntity(fileEntity);
        }
    }

    @Transactional
    public void addFileToDocument(String fileIdentifier, String documentIdentifier) {
        LOGGER.info("adding file - " + fileIdentifier + " to Document - " + documentIdentifier);
        FileEntity fileEntity =
                findFileEntity(fileIdentifier);
        documentService.addFileToDocument(documentIdentifier, fileEntity);
    }

    @Transactional
    private FileEntity findFileEntity(String identifier) {
        if (identifier != null && !identifier.isEmpty()) {
            FileEntity fileEntity = fileRepository.getFileByIdentifier(identifier);
            return fileEntity;
        } else {
            throw new IllegalArgumentException("Klaida - nepateiktas teisingas failo identifikacinis numeris");
        }
    }
}
