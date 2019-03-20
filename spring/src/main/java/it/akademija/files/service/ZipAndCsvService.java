package it.akademija.files.service;


import com.opencsv.CSVWriter;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.files.FolderAndFileNames;
import it.akademija.helpers.FileHelper;
import it.akademija.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class ZipAndCsvService {
    private static Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
    @Autowired
    UserService userService;
    
    @Autowired
    FileHelper fileHelper;

    @Transactional
    public File zippingUserFolder(String userName) throws IOException {
        LOGGER.info("user - " + userName + "is zipping its folder" );
        File zipFileLocation = prepareZipFileLocation(userName);
        initiateZiping(zipFileLocation, userName);
        return zipFileLocation;
    }
    @Transactional
    private File prepareZipFileLocation(String userName) {
        File zipFolderLocation = fileHelper.getUserZIPFolderLocation(userName);
        File zipFileLocation = new File(zipFolderLocation + File.separator
                + FolderAndFileNames.CompressedFile + ".zip");
        if(zipFileLocation.exists()){
            zipFileLocation.delete();
        }
        return zipFileLocation;
    }
    @Transactional
    private void initiateZiping(File zipFileLocation, String userName)throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFileLocation);

        ZipOutputStream zipOut = new ZipOutputStream(fos);
        String sourceFile = fileHelper.getUserPDFAndCSVFolderLocation(userName).toString();
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
        LOGGER.info("user - " + userName + "has finished zipping its data, the data is stored at - "
                + zipFileLocation.toString());
    }


    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    @Transactional
    public void writeCsv(String userName) throws IOException {
        Path pathToCSV = Paths.get(fileHelper.getUserPDFAndCSVFolderLocation(userName) + File.separator
                + FolderAndFileNames.UserInformation + ".csv");
        //where file will be written
        CSVWriter writer = new CSVWriter(new FileWriter(pathToCSV.toString()));
        List<String[]> stringList = new ArrayList<>();
        List<DocumentEntity> documents = userService.getAllUserDocuments(userName);
        //this is simply to get the field names on the top of csv file
        stringList.add(documents.get(0).getFieldNames().split(","));

        // goes through all dcumentEntities and converts them to string arrays and places arrays in a list
        for (DocumentEntity documentEntity: documents
        ) {
            String stringas = documentEntity.toString();
            String[] entries = stringas.split("/,/");
            stringList.add(entries);
        }
        //writes generated arrays in csv, one sting array is one line in csv
        for (String[] stringEntries:stringList
        ) {
            writer.writeNext(stringEntries);
        }
        //closing stream
        writer.close();
        LOGGER.info("user - " + userName + "has finished creating csv File, it is stored at - " + pathToCSV.toString());
    }

}

