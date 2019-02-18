package it.akademija.files.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.ResponseTransfer;
import it.akademija.files.service.FileDocumentCommand;
import it.akademija.files.service.FileService;
import it.akademija.files.service.FileServiceObject;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
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

    //    private final FileRepository fileRepository;
//
//    public FileController(FileRepository fileRepository) {
//        this.fileRepository = fileRepository;
//    }
//

//        @RequestMapping(path = "/pdf/{fileName:.+}", method = RequestMethod.GET)
//        public void downloadPDFResource( HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         @PathVariable("fileName") String identifier)
////                                         @RequestHeader String referer)
//        {
//
//            //Check the renderer
////            if(referer != null && !referer.isEmpty()) {
////                //do nothing
////                //or send error
////            }
//            //If user is not authorized - he should be thrown out from here itself
//
//            //Authorized user will download the file
//            FileServiceObject fileObject = fileService.findFile(identifier);
//            String dataDirectory = fileObject.getFileLocation();
//            Path file = Paths.get(dataDirectory);
//            if (Files.exists(file))
//            {
//                response.setContentType("application/pdf");
//                response.addHeader("Content-Disposition", "attachment; filename="+ fileObject.getFileName());
//                try
//                {
//                    Files.copy(file, response.getOutputStream());
//                    response.getOutputStream().flush();
//                }
//                catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }

    // downloads a file, need unique document identifier
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
//
//    // Using ResponseEntity<ByteArrayResource>
////    @GetMapping("/download2")
////    public ResponseEntity<ByteArrayResource> downloadFile2(@NotNull @RequestParam String identifier)
//    @GetMapping("/download2/{identifier}")
//    public ResponseEntity<ByteArrayResource> downloadFile2(@NotNull @PathVariable final String identifier)
//            throws IOException {
//        FileServiceObject fileObject = fileService.findFile(identifier);
//        File file = new File(fileObject.getFileLocation());
//
//        Path path = Paths.get(file.getAbsolutePath());
//        byte[] data = Files.readAllBytes(path);
//        ByteArrayResource resource = new ByteArrayResource(data);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment;filename=" + path.getFileName().toString())
//                .contentType(MediaType.APPLICATION_PDF).contentLength(data.length)
//                .body(resource);
//    }

    //
//
//    @GetMapping("/siunciam")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@NotNull @RequestParam String identifier) throws IOException {
//        FileServiceObject fileObject = fileService.findFile(identifier);
//        File file = new File(fileObject.getFileLocation());
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + fileObject.getFileName())
//                .body(resource);
//    }
//
//    // Using HttpServletResponse
//    @GetMapping("/download3")
//    public void downloadFile3(HttpServletResponse resonse, @NotNull @RequestParam String identifier)
//            throws IOException {
//        FileServiceObject fileObject = fileService.findFile(identifier);
//        File file = new File(fileObject.getFileLocation());
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//
//        resonse.setContentType("application/pdf");
//        resonse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
//        BufferedInputStream inStrem = new BufferedInputStream(new FileInputStream(file));
//        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());
//
//        byte[] buffer = new byte[1024];
//        int bytesRead = 0;
//        while ((bytesRead = inStrem.read(buffer)) != -1) {
//            outStream.write(buffer, 0, bytesRead);
//        }
//        outStream.flush();
//        inStrem.close();
//    }


    @PostMapping
    @ResponseBody
    // create a file and upload it. return unique identifier.
    public ResponseTransfer uploadNewFile(@NotNull @RequestParam("file") MultipartFile multipartFile)
            throws Exception{
        String uniqueIdentifier = fileService.addFileToDataBase(multipartFile);
        //just sends identifier for a file as a JSON fi private Set<FileEntity> filesAttachedToDocument=le, visible on swager and react.
        FileServiceObject fileServiceObject = fileService.findFile(uniqueIdentifier);
        return new ResponseTransfer(fileServiceObject.getIdentifier());
    }

    // this is used to add file to document, can beused for multiple file
    @RequestMapping(value = "/addFileToDocument", method = RequestMethod.POST)
    public ResponseEntity < String >  addFileToDocument(@NotNull @RequestBody FileDocumentCommand fileDocumentComand){

//            @NotNull @RequestParam("FileIdentifier") String fileIdentifier,
//                                  @NotNull @RequestParam("DocumentIdentifier") String documentIdentifier)
//    {        fileService.addFileToDocument(fileIdentifier, documentIdentifier);
//        return ResponseEntity.status(HttpStatus.CREATED).build();}

        fileService.addFileToDocument(fileDocumentComand.getFileIdentifier()
                , fileDocumentComand.getDocumentIdentifier());
        return ResponseEntity.status(HttpStatus.CREATED).build();


    }
//    //not working yet
//    @RequestMapping(value = "/findAllFilesByDocument", method = RequestMethod.GET)
//    public Set<FileEntity> giveMEALllFileSSS(@NotNull @RequestParam("DocumentIdentifier") String documentIdentifier){
//        DocumentServiceObject documentServiceObject = null;
//        documentServiceObject = documentService.getDocumentByDocumentIdentifier(documentIdentifier);
//        return documentServiceObject.getFilesAttachedToDocument();
////        return ResponseEntity.status(HttpStatus.CREATED).build();
//
//
//    }

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





