package it.akademija.helpers;

import it.akademija.files.FolderAndFileNames;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.service.FileServiceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


@Component("FileHelper")
public class FileHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    public boolean IsFilePDF(MultipartFile multipartFile) throws Exception {
        String acceptableFileType = "application/pdf";
        String uploadingFileType = multipartFile.getContentType();
        if (acceptableFileType.equals(uploadingFileType)) {
            return true;
        } else {
            throw new Exception("Netinkamas failo formatas, formatas - " + uploadingFileType);
        }

    }

    @Transactional
    public File uploadFileToLocalServer(MultipartFile fileInTransfer, String userName) throws Exception {
        try {
            createFolderSystem(userName);
            File fileLocationDirectory = getUserPDFAndCSVFolderLocation(userName);
            File fileLocationOnServer = givingUniqueNameToFile(fileInTransfer, fileLocationDirectory);
            fileInTransfer.transferTo(fileLocationOnServer);
            return fileLocationOnServer;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
            throw new Exception("Klaida įkeliant failą į vietinį katalogą " +
                    ", klaidos pranešimas - " + e.getMessage());
        }

    }

    public void createFolderSystem(String userName) {
        LOGGER.info("Creating folders on server for user - " + userName);
        File mainFolderLocation = getMainFolderLocation();
        prepareDirectory(mainFolderLocation);

        File pdfAndCsvDirectoryLocation = new File(mainFolderLocation
                + File.separator + userName);
        prepareDirectory(pdfAndCsvDirectoryLocation);

        File zipParentDirectoryLocation = new File(mainFolderLocation
                + File.separator + FolderAndFileNames.ZipDirectory);
        prepareDirectory(zipParentDirectoryLocation);

        File zipFileDirectoryLocation = new File(zipParentDirectoryLocation + File.separator + userName);
        prepareDirectory(zipFileDirectoryLocation);
    }

    private void prepareDirectory(File mainFolder) {
        if (!mainFolder.exists()) {
            System.out.println(mainFolder.mkdir());
            System.out.println(mainFolder.setWritable(true));
        }

    }

    private File givingUniqueNameToFile(MultipartFile file, File fileLocationDirectory) {
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return new File(fileLocationDirectory + File.separator + time + file.getOriginalFilename());
    }


    public boolean isUserFolderForPDFsCreated(String userName) {
        File userFolder = getUserPDFAndCSVFolderLocation(userName);
        return userFolder.exists();
    }

    public File getUserPDFAndCSVFolderLocation(String userName) {
        File mainFolder = getMainFolderLocation();
        return new File(mainFolder
                + File.separator + userName);
    }

    public File getUserZIPFolderLocation(String userName) {
        File mainFolder = getMainFolderLocation();
        return new File(mainFolder
                + File.separator + FolderAndFileNames.ZipDirectory + File.separator + userName);
    }

    private File getMainFolderLocation() {
        String currentUsersHomeDir = System.getProperty("user.home");
        return new File(currentUsersHomeDir + File.separator + FolderAndFileNames.TMPDOCS);
    }

    public FileServiceObject SOfromEntity(FileEntity fileEntity) {
        FileServiceObject fileServiceObject = new FileServiceObject();
        fileServiceObject.setContentType(fileEntity.getContentType());
        fileServiceObject.setFileLocation(fileEntity.getFileLocation());
        fileServiceObject.setFileName(fileEntity.getFileName());
        fileServiceObject.setSize(fileEntity.getSize());
        fileServiceObject.setIdentifier(fileEntity.getIdentifier());
        return fileServiceObject;
    }
}
