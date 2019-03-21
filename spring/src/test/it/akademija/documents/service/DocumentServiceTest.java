package it.akademija.documents.service;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.users.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    private final String IDENTIFIER="test1";

    private DocumentEntity documentEntity=new DocumentEntity();

    private DocumentServiceObject documentServiceObject=new DocumentServiceObject();

    @Mock
    DocumentRepository documentRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DocumentService documentService;


    @Test
    public void shouldGetDocumentByItsIdentifier () {
        //given
        documentEntity.setDocumentIdentifier(IDENTIFIER);
        when(documentRepository.findDocumentByDocumentIdentifier(IDENTIFIER)).thenReturn(documentEntity);
        //when
        documentServiceObject=documentService.getDocumentByDocumentIdentifier(IDENTIFIER);
        //then
        verify(documentRepository, times(1)).findDocumentByDocumentIdentifier(IDENTIFIER);
        assertEquals(documentEntity.getDocumentIdentifier(), documentServiceObject.getDocumentIdentifier());
    }


    @Test
    public void checkIfDocumentDeletedWithRejected(){
        //given
        documentEntity.setDocumentState(DocumentState.REJECTED);
        when(documentRepository.findDocumentByDocumentIdentifier(anyString())).thenReturn(documentEntity);
        //when
        documentService.deleteDocument(anyString());
        //then
        verify(documentRepository, times(1)).deleteDocumentByDocumentIdentifier(anyString());
    }

    @Test
    public void checkIfDocumentDeletedWithCreated(){
        //given
        documentEntity.setDocumentState(DocumentState.CREATED);
        when(documentRepository.findDocumentByDocumentIdentifier(anyString())).thenReturn(documentEntity);
        //when
        documentService.deleteDocument(anyString());
        //then
        verify(documentRepository, times(1)).deleteDocumentByDocumentIdentifier(anyString());
    }

    @Test
    public void checkIfDocumentIsNotDeletedWithApproved(){
        //given
        documentEntity.setDocumentState(DocumentState.APPROVED);
        when(documentRepository.findDocumentByDocumentIdentifier(anyString())).thenReturn(documentEntity);
        //when
        documentService.deleteDocument(anyString());
        //then
        verify(documentRepository, times(0)).deleteDocumentByDocumentIdentifier(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfDocumentExistsWithEmptyParameter(){
        //given
        documentService.getDocumentByDocumentIdentifier("");

    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfDocumentExistsWithNullParameter(){
        //given
        documentService.getDocumentByDocumentIdentifier("");
    }

}


