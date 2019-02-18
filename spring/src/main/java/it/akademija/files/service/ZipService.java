package it.akademija.zipping;


import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//            String currentUsersHomeDir = System.getProperty("user.home");
//                    File fileLocation = new File(currentUsersHomeDir + File.separator  + "tmpDocs" + File.separator  +  file.getOriginalFilename());
//                    File fileLocationDirectory = new File(currentUsersHomeDir + File.separator  + "tmpDocs");


public class ZipService {


    @Transactional
    public void zip(String userName) throws IOException {
//            File fileLocation = new File(currentUsersHomeDir + File.separator  + "tmpDocs"
//                    + File.separator  + name + File.separator  + file.getOriginalFilename());
//            File fileLocationDirectory = new File(currentUsersHomeDir + File.separator  + "tmpDocs" + File.separator  + name);

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
}

