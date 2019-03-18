package it.akademija.files.service;

import it.akademija.documents.repository.DocumentRepository;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import it.akademija.helpers.FileHelper;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.Null;

import java.io.File;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    private FileEntity fileEntity;

    private final String IDENTIFIER="12345";

    @Mock
    private FileHelper file;

    @Mock
    DocumentRepository documentRepository;

    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileService fileService;


    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsWithEmptyParameter(){
        //given
        fileService.findFile("");

    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsWithNullParameter(){
        //given
        fileService.findFile(null);

    }

    @Test
    public void checkIfFileExistsWithValidParameter(){
        //given
        FileEntity fileEntity=new FileEntity();
        fileEntity.setFileName("smth");
        when(fileRepository.getFileByIdentifier(IDENTIFIER)).thenReturn(fileEntity);
        FileServiceObject fileServiceObject=new FileServiceObject();
        when(file.SOfromEntity(fileEntity)).thenReturn(fileServiceObject);
        //when
        FileServiceObject returned=fileService.findFile(IDENTIFIER);
        //then
        verify(fileRepository, times(2)).getFileByIdentifier(IDENTIFIER);
        assertEquals(returned, fileServiceObject);
    }

//    @Test
//    public void checkIfFileUploaded() throws Exception {
//        //given
//        File fileLocationOnServer = new File("name");
//        when(file.uploadFileToLocalServer(any(MultipartFile.class),anyString())).thenReturn(fileLocationOnServer);
//        MultipartFile multipartFile=new CommonsMultipartFile(new DefaultFileItem());
//        //when
//
//        String uploadedFileName=fileService.uploadFile(any(MultipartFile.class),anyString());
//        verify(fileRepository, times(1));
//        assertTrue(uploadedFileName!=null);
//
//    }








}

