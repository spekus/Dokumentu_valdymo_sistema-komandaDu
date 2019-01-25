package it.akademija.files.service;


import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    @Transactional
    public void addFileToDataBase(MultipartFile multipartFile) {

        File uploadingLocation = uploadFileToLocalServer(multipartFile, multipartFile.getContentType()); //uploads file to the server
        System.out.println("uploading done - " + uploadingLocation);
        System.out.println("file name - " + multipartFile.getOriginalFilename());

        FileEntity fileEntity = new FileEntity(multipartFile.getOriginalFilename());
        fileEntity.setFileLocation(uploadingLocation.getAbsolutePath());
        fileEntity.setContentType(multipartFile.getContentType());
        fileRepository.save(fileEntity);



//        fileRepository.save(fileEntity);


//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
//        return ResponseEntity.created(location).build();
//



//        public void addSventeToDataBase(SventeObject cmd) {
//            Svente svente = new Svente();
//            svente.setDescription(cmd.getDescription());
//            svente.setName(cmd.getName());
//            svente.setFlagUpTrue(cmd.isFlagUpTrue());
//            svente.setPicture(cmd.getPicture());
//            svente.setType(cmd.getType());
//
//            sventeRepository.save(svente);

        }

    public File uploadFileToLocalServer(MultipartFile file, String format) {


        String fileName = file.getOriginalFilename();
        if (true) {


            File targetFile = new File(File.separator + "home"
                    + File.separator + "tmpDocs" + File.separator + "files" + File.separator, fileName);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            // 保存
            try {
                File fileLocation = new File(File.separator + "home"
                        + File.separator + "augustas" + File.separator + "tmpDocs" + File.separator
                        + file.getOriginalFilename());
//                fileLocation.mkdir();
                file.transferTo(fileLocation);

//                fileLocation.createNewFile();
//                System.out.println("fdfdf");

                return fileLocation;
            } catch (Exception e) {
                e.printStackTrace();
                return new File(File.separator + "home"
                        + File.separator + "augustas" + File.separator + "tmpDocs");
            }



        }
        return new File(File.separator + "home"
                + File.separator + "augustas" + File.separator + "tmpDocs");
    }



    }



