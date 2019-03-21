package it.akademija.files.service;

import it.akademija.documents.repository.DocumentRepository;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.repository.FileRepository;
import it.akademija.helpers.FileHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}

