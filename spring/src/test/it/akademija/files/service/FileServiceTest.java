package it.akademija.files.service;

import it.akademija.documents.repository.DocumentRepository;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    private FileEntity fileEntity;

    private final String IDENTIFIER="12345";

    @Mock
    DocumentRepository documentRepository;

    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileService fileService;


    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsWithEmptyParameter(){
        //given
        fileService.findFileEntity("");
        fileService.findFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsWithNullParameter(){
        //given
        fileService.findFileEntity(null);
        fileService.findFile(null);
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void checkIfFileExistsWithNullParameter1(){
//        //given
//        fileService.findFile(null);
//    }

    @Test
    public void checkIfFileExistsWithValidParameter(){
        FileEntity fileEntity=new FileEntity();
        fileEntity.setIdentifier("aaa");
        when(fileRepository.getFileByIdentifier(IDENTIFIER)).thenReturn(fileEntity);
        FileEntity returned=fileService.findFileEntity(IDENTIFIER);
        verify(fileRepository, times(1)).getFileByIdentifier(IDENTIFIER);
        assertEquals(fileEntity, returned);
    }








}

