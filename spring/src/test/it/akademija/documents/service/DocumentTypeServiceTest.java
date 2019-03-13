package it.akademija.documents.service;

import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.users.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import javax.swing.text.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeServiceTest {

    private DocumentTypeEntity documentTypeEntity;

    private final String OLDTITLE="oldTitle";

    private final String NEWTITLE="newTitle";

    @Mock
    DocumentTypeRepository documentTypeRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DocumentTypeService documentTypeService;

    @Before
    public void setDocumentTypeEntity(){
        documentTypeEntity=new DocumentTypeEntity(OLDTITLE);
    }

    @Test
    public void checkIfNewDocumentTypeCreatedAndSaved(){
        //given
        //when
        documentTypeService.createNewDocumentType(anyString());
        //then
        verify(documentTypeRepository, times(1)).save(any(DocumentTypeEntity.class));
     }

     @Test
     public void checkIfDocumentTypeUpdated(){
        //given
         given(documentTypeRepository.findDocumentTypeByTitle(anyString())).willReturn(documentTypeEntity);
         //when
         documentTypeService.updateDocumentType(OLDTITLE,NEWTITLE);
         //then
        assertEquals(documentTypeEntity.getTitle(),NEWTITLE);

     }

    @Test
    public void checkIfDocumentTypeNotUpdatedWithNull(){
        //given
        given(documentTypeRepository.findDocumentTypeByTitle(anyString())).willReturn(documentTypeEntity);
        //when
        documentTypeService.updateDocumentType(OLDTITLE,null);
        //then
        assertEquals(documentTypeEntity.getTitle(),OLDTITLE);
        verify(documentTypeRepository, times(0)).save(any(DocumentTypeEntity.class));
    }

    @Test
    public void checkIfDocumentTypeNotUpdatedWithEmpty(){
        //given
        given(documentTypeRepository.findDocumentTypeByTitle(anyString())).willReturn(documentTypeEntity);
        //when
        documentTypeService.updateDocumentType(OLDTITLE,"");
        //then
        assertEquals(documentTypeEntity.getTitle(),OLDTITLE);
        verify(documentTypeRepository, times(0)).save(any(DocumentTypeEntity.class));
    }

    @Test
    public void checkIfNewDocumentTypeDeleted(){
        //given
        //when
        documentTypeService.deleteDocumentType(anyString());
        //then
        verify(documentTypeRepository, times(1)).deleteDocumentTypeByTitle(anyString());
    }

    @Test
    public void findAll() {
        //given
        List<DocumentTypeEntity> types = new ArrayList<>();
        types.add(new DocumentTypeEntity("1"));
        when(documentTypeRepository.findAll()).thenReturn(types);
        //when
        Set result=documentTypeService.getAllDocumentTypes();
        //then
        assertTrue(result.iterator().next() instanceof DocumentTypeServiceObject);

        verify(documentTypeRepository, atLeastOnce()).findAll();
    }
}


