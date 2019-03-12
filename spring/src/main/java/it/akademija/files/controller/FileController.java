package it.akademija.files.controller;


import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.ResponseTransfer;
import it.akademija.files.service.FileDocumentCommand;
import it.akademija.files.service.FileService;
import it.akademija.files.service.FileServiceObject;
import it.akademija.files.service.ZipAndCsvService;
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
    private  FileHelper fileHelper;




    @RequestMapping(value = "/download/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable final String identifier)
            throws IOException {
//        FileServiceObject  = fileService.downloadFileFromLocalServer(identifier);
//        return data;
        FileServiceObject fileObject = fileService.findFile(identifier);
        File file = new File(
                fileObject.getFileLocation());
        System.out.println("FILE LOCATION" + file.getAbsolutePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        //        HttpHeaders header = new HttpHeaders();
//
//        header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
//        header.setContentLength(fileEntity.getData().length);
//        header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());
        System.out.println("File name = " + fileObject.getFileName());
        System.out.println("File location = " + fileObject.getFileLocation());
        System.out.println("File type = " + fileObject.getContentType());
        System.out.println("File size = " + fileObject.getSize());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + fileObject.getFileName())
                .contentType(MediaType.valueOf(fileObject.getContentType())).contentLength(fileObject.getSize())
                .body(resource);
    }
    // creates a zip from user files and CSV file with user document information
    @RequestMapping(value = "/zip", method = RequestMethod.GET)
    public ResponseEntity<Resource> makeZip(
            @ApiIgnore Authentication authentication) throws IOException {
        // first action is to write csv file in location
        zipAndCsvService.writeCsv(authentication.getName());

        //zips files
        File file = zipAndCsvService.zip(authentication.getName());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        //returns zip as a stream
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }




    @PostMapping
    @ResponseBody
    // create a file and upload it. return unique identifier.
    public ResponseTransfer uploadNewFile(
                        @ApiIgnore Authentication authentication,
                        @NotNull @RequestParam("file") MultipartFile fileSentForUploading) throws Exception{
        if(fileHelper.IsFilePDF(fileSentForUploading)) {
            String uniqueIdentifier = fileService.addFileToDataBase(fileSentForUploading, authentication.getName());
            //just sends identifier for a file as a JSON fi private Set<FileEntity> filesAttachedToDocument=le, visible on swager and react.

            FileServiceObject fileServiceObject = fileService.findFile(uniqueIdentifier);
            return new ResponseTransfer(fileServiceObject.getIdentifier());
        }
        throw new Exception("error during initial uploading");
    }

    // this is used to add file to document, can beused for multiple file
    @RequestMapping(value = "/addFileToDocument", method = RequestMethod.POST)
    public ResponseEntity < String >  addFileToDocument(@NotNull @RequestBody FileDocumentCommand fileDocumentComand){


        fileService.addFileToDocument(fileDocumentComand.getFileIdentifier()
                , fileDocumentComand.getDocumentIdentifier());
        return ResponseEntity.status(HttpStatus.CREATED).build();


    }


    //not working yet
    @RequestMapping(value = "/findAllFilesByDocumentIdentifier", method = RequestMethod.GET)
    public List<String> getAllFileIdentifiers(@NotNull @RequestParam("documentIdentifier") String documentIdentifier){
        ArrayList <String> identifierList = new ArrayList<>();
//        identifierList = fileService.getAllFileIdentifiers(documentIdentifier);
        DocumentServiceObject documentServiceObject = null;
//        Hibernate.initialize(documentService.getDocumentByDocumentIdentifier(documentIdentifier).getFilesAttachedToDocument());
        documentServiceObject = documentService.getDocumentByDocumentIdentifier(documentIdentifier);

        Set<FileServiceObject> fileList =  documentServiceObject.getFilesAttachedToDocument();


        for (FileServiceObject file: fileList
             ) {
            System.out.println("identifier " + file.getIdentifier());
            identifierList.add(file.getIdentifier());
        }
        return identifierList;
    }






}





