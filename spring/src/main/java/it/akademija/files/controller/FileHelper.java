package it.akademija.files.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component("FileHelper")
public class FileHelper {
    public boolean IsFilePDF(MultipartFile multipartFile) throws Exception {
        String acceptableFileType = "application/pdf";
        String uploadingFileType = multipartFile.getContentType();
        if(acceptableFileType.equals(uploadingFileType)){
            return true;
        }else{
            throw new Exception("File format is not valid, uploadingFileType is -" +  uploadingFileType);
        }

    }
}
