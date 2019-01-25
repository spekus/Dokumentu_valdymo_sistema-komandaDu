package it.akademija.files.controller;


import it.akademija.files.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class FileController {

    @Autowired
    private FileService fileService;

//    private final FileRepository fileRepository;
//
//    public FileController(FileRepository fileRepository) {
//        this.fileRepository = fileRepository;
//    }
//
//    @GetMapping
//    public ResponseEntity<byte[]> getRandomFile() {
//
//        long amountOfFiles = fileRepository.count();
//        Long randomPrimaryKey;
//
//        if (amountOfFiles == 0) {
//            return ResponseEntity.ok(new byte[0]);
//        } else if (amountOfFiles == 1) {
//            randomPrimaryKey = 1L;
//        } else {
//            randomPrimaryKey = ThreadLocalRandom.current().nextLong(1, amountOfFiles + 1);
//        }
//
//        FileEntity fileEntity = fileRepository.findById(randomPrimaryKey).get();
//
//        HttpHeaders header = new HttpHeaders();
//
//        header.setContentType(MediaType.valueOf(fileEntity.getContentType()));
//        header.setContentLength(fileEntity.getData().length);
//        header.set("Content-Disposition", "attachment; filename=" + fileEntity.getFileName());
//
//        return new ResponseEntity<>(fileEntity.getData(), header, HttpStatus.OK);
//    }

    @PostMapping
    public void uploadNewFile(@NotNull @RequestParam("file") MultipartFile multipartFile) throws IOException {

        fileService.addFileToDataBase(multipartFile);

    }


}





