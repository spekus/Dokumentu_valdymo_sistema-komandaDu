package it.akademija.files.controller;


import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.ResponseTransfer;
import it.akademija.files.service.FileDocumentCommand;
import it.akademija.files.service.FileService;
import it.akademija.files.service.FileServiceObject;
import it.akademija.files.service.ZipAndCsvService;
import it.akademija.helpers.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ZipAndCsvService zipAndCsvService;

    @Autowired
    private FileHelper fileHelper;


    @RequestMapping(value = "/download/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable final String identifier)
            throws IOException {
        FileServiceObject fileObject = fileService.findFile(identifier);
        File fileLocation = new File(
                fileObject.getFileLocation());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileLocation));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + fileObject.getFileName())
                .contentType(MediaType.valueOf(fileObject.getContentType())).contentLength(fileObject.getSize())
                .body(resource);
    }


//    // creates a zippingUserFolder from user files and CSV file with user document information
////    @RequestMapping(value = "/zip", method = RequestMethod.GET, produces="application/zip")
//    @RequestMapping(value = "/zip", method = RequestMethod.GET, produces="application/zip")
//    public ResponseEntity<Resource> makeZip(
//            @ApiIgnore Authentication authentication) throws Exception {
////        if(authentication.isAuthenticated() && fileHelper.isUserFolderForPDFsCreated(authentication.getName())){
////            zipAndCsvService.writeCsv(authentication.getName());
//            zipAndCsvService.writeCsv("id123");
////            File zipFileLocation = zipAndCsvService.zippingUserFolder(authentication.getName());
//            File zipFileLocation = zipAndCsvService.zippingUserFolder("id123");
//            InputStreamResource zipFileStream = new InputStreamResource(new FileInputStream(zipFileLocation));
//            //returns zippedUserFolder as a stream
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            return new ResponseEntity<>(zipFileStream, headers, HttpStatus.OK);
////        }
//        throw new Exception("User - " + authentication.getName() + " does not have access , or User has yet to upload file");
//    }
// creates a zippingUserFolder from user files and CSV file with user document information
@RequestMapping(value = "/zip", method = RequestMethod.GET)
public ResponseEntity<InputStreamResource> makeZip(
        @ApiIgnore Authentication authentication)  {
    System.out.println("authentication" + authentication);
    System.out.println("authentication.getName()" + authentication.getName());
    System.out.println("authentication.toString()" + authentication.toString());
//    if(authentication.isAuthenticated() && fileHelper.isUserFolderForPDFsCreated(authentication.getName())){
    try {
        zipAndCsvService.writeCsv("id123");
    } catch (IOException e) {
        e.printStackTrace();
    }
    File zipFileLocation = null;
    try {
        zipFileLocation = zipAndCsvService.zippingUserFolder("id123");
    } catch (IOException e) {
        e.printStackTrace();
    }
    InputStreamResource zipFileStream = null;
    try {
        zipFileStream = new InputStreamResource(new FileInputStream(zipFileLocation));
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    //returns zippedUserFolder as a stream
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        return new ResponseEntity<>(zipFileStream, headers, HttpStatus.OK);
//    }
    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "PAVADINIMAS" + "\"")
            .body(zipFileStream);
//            .header(HttpHeaders.CONTENT_DISPOSITION,
//                    "attachment;filename= none")
//            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .body(zipFileStream);
//    throw new Exception("User - " + authentication.getName() + " does not have access , or User has yet to upload file");
}




    @PostMapping
    @ResponseBody
    // create a file and upload it. return unique identifier.
    public ResponseTransfer uploadNewFile(
                        @ApiIgnore Authentication authentication,
                        @NotNull @RequestParam("file") MultipartFile fileSentForUploading) throws Exception{
        if(fileHelper.IsFilePDF(fileSentForUploading)) {
            String uniqueIdentifier = fileService.uploadFile(fileSentForUploading, authentication.getName());
            FileServiceObject fileServiceObject = fileService.findFile(uniqueIdentifier);
            return new ResponseTransfer(fileServiceObject.getIdentifier());
        }
        throw new Exception("error during initial uploading");
    }

    // this is used to add file to document, can be used for multiple file
    @RequestMapping(value = "/addFileToDocument", method = RequestMethod.POST)
    public ResponseEntity < String >  addFileToDocument(@NotNull @RequestBody FileDocumentCommand fileDocumentComand) throws Exception {
        try{
            fileService.addFileToDocument(fileDocumentComand.getFileIdentifier()
                    , fileDocumentComand.getDocumentIdentifier());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e){
            throw new Exception("Cant add file to document, file identifier - " + fileDocumentComand.getFileIdentifier() +
                    " Document identifier - " + fileDocumentComand.getDocumentIdentifier() );
        }

    }


//
//    @RequestMapping(value = "/findAllFilesByDocumentIdentifier", method = RequestMethod.GET)
//    public List<String> getAllFileIdentifiers(@NotNull @RequestParam("documentIdentifier") String documentIdentifier){
//        ArrayList <String> identifierList = new ArrayList<>();
//        DocumentServiceObject documentServiceObject = null;
//        documentServiceObject = documentService.getDocumentByDocumentIdentifier(documentIdentifier);
//
//        Set<FileServiceObject> fileList =  documentServiceObject.getFilesAttachedToDocument();
//
//
//        for (FileServiceObject file: fileList
//             ) {
//            identifierList.add(file.getIdentifier());
//        }
//        return identifierList;
//    }






}





