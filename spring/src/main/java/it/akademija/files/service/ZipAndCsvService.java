package it.akademija.files.service;


import com.opencsv.CSVWriter;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.users.service.UserService;
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
//BAISUS KODAS, KOL KAS NEZIURET :D
public class ZipAndCsvService {
    @Autowired
    UserService userService;


    //THIS NEEED TOTAL REMAKING
    private File createFolder(String userName, String fileName){
        //user file location
        String currentUsersHomeDir = System.getProperty("user.home");
        File fileLocationDirectory = new File(currentUsersHomeDir + File.separator
                + "tmpDocs" + File.separator  + userName);

        //ZIP saving locations
        File zipGeneralDirectory = new File(currentUsersHomeDir + File.separator
                + "tmpDocs" + File.separator + "zipFiles");
        File zipSavingLocation = new File(currentUsersHomeDir + File.separator
                + "tmpDocs" + File.separator + "zipFiles" + File.separator + userName);
        // makes sure that previous zip is deleted.
        if(!fileLocationDirectory.isDirectory()){
            System.out.println(fileLocationDirectory.mkdir());
            System.out.println(fileLocationDirectory.setWritable(true));
        }
        if(!zipSavingLocation.isDirectory()){
            System.out.println(zipGeneralDirectory.mkdir());
            System.out.println(zipGeneralDirectory.setWritable(true));
//                zipSavingLocation.delete();
            System.out.println(zipSavingLocation.mkdir());
            System.out.println(zipSavingLocation.setWritable(true));
        }
        File filePath = new File(zipSavingLocation + File.separator  + fileName);

        //makes sure old file is delted
        if(filePath.exists()){
            filePath.delete();
        }
        return filePath;

    }

    @Transactional
    public File zip(String userName) throws IOException {

        String currentUsersHomeDir = System.getProperty("user.home");

        //user file location
        File fileLocationDirectory = new File(currentUsersHomeDir + File.separator
                + "tmpDocs" + File.separator  + userName);

        //ZIP saving locations
        File zipGeneralDirectory = new File(currentUsersHomeDir + File.separator
                + "tmpDocs" + File.separator + "zipFiles");
        File zipSavingLocation = new File(currentUsersHomeDir + File.separator
                + "tmpDocs" + File.separator + "zipFiles" + File.separator + userName);
        // makes sure that previous zip is deleted.
        if(!fileLocationDirectory.isDirectory()){
            System.out.println(fileLocationDirectory.mkdir());
            System.out.println(fileLocationDirectory.setWritable(true));
        }
        if(!zipSavingLocation.isDirectory()){
            System.out.println(zipGeneralDirectory.mkdir());
            System.out.println(zipGeneralDirectory.setWritable(true));
//                zipSavingLocation.delete();
            System.out.println(zipSavingLocation.mkdir());
            System.out.println(zipSavingLocation.setWritable(true));
        }
        File zipName = new File(zipSavingLocation + File.separator  + "Compressed.zip");

        if(zipName.exists()){
            zipName.delete();
        }

//            String sourceFile = "zipTest";
        String sourceFile = fileLocationDirectory.toString();

        //location of new file
        FileOutputStream fos = new FileOutputStream(zipName);
        //creates output stream
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
        return zipName;
    }

    @Transactional
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
        //creates all folder if those do not yet exist
        createFolder(userName, "test");
        List<DocumentEntity> documents = userService.getAllUserDocuments(userName);
        Path path = null;
        try {
            //creates path in user computer
            String currentUsersHomeDir = System.getProperty("user.home");
            path = Paths.get(currentUsersHomeDir + "/tmpDocs/"+ userName +"/UserInformation.csv");
        } catch (Exception ex) {
            throw new IOException("we are not able to create directory - " + path.toString() + ex);
        }

        //where file will be written
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));

        List<String[]> stringList = new ArrayList<>();
        
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
    }

}

