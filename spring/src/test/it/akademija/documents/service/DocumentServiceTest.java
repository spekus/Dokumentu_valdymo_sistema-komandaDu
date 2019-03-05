package it.akademija.documents.service;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.users.repository.UserRepository;
import net.bytebuddy.pool.TypePool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    @Mock
    DocumentRepository documentRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DocumentService documentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetDocumentByItsIdentifier () {
        DocumentEntity doc = new DocumentEntity();
        doc.setDocumentIdentifier("test1");
        when(documentRepository.findDocumentByDocumentIdentifier("test1")).thenReturn(doc);
        DocumentServiceObject documentEntity=documentService.getDocumentByDocumentIdentifier("test1");
        verify(documentRepository).findDocumentByDocumentIdentifier("test1");
        assertEquals("test1", documentEntity.getDocumentIdentifier());
    }

    @Test
    public void shouldGetIllegalArgumentExceptionIfNullIsReturnWhileGettingUnknownDocument () {
        DocumentEntity doc = new DocumentEntity();
        doc.setDocumentIdentifier("test1");
        when(documentRepository.findDocumentByDocumentIdentifier("test1")).thenReturn(doc);
        DocumentServiceObject documentEntity=documentService.getDocumentByDocumentIdentifier("test1");
        verify(documentRepository).findDocumentByDocumentIdentifier("test1");
        assertThatThrownBy(()->documentService.getDocumentByDocumentIdentifier("test2")).isInstanceOf(NullPointerException.class);
    }

//    @Test
//    public void shouldReturnDocumentsByState() {
//        DocumentServiceObject doc1 = new DocumentServiceObject();
//        doc1.setDocumentState(DocumentState.CREATED);
//        doc1.setDocumentIdentifier("test1");
//        when(documentService.getDocumentByDocumentIdentifier("test1")).thenReturn(doc1);
//        DocumentServiceObject doc2 = new DocumentServiceObject();
//        doc2.setDocumentState(DocumentState.CREATED);
//        doc2.setDocumentIdentifier("test2");
//        when(documentService.getDocumentByDocumentIdentifier("test1")).thenReturn(doc2);
//        DocumentServiceObject doc3 = new DocumentServiceObject();
//        doc3.setDocumentState(DocumentState.APPROVED);
//        doc3.setDocumentIdentifier("test3");
//        when(documentService.getDocumentByDocumentIdentifier("test1")).thenReturn(doc3);
//        DocumentServiceObject doc4 = new DocumentServiceObject();
//        doc4.setDocumentState(DocumentState.REJECTED);
//        doc4.setDocumentIdentifier("test4");
//        when(documentService.getDocumentByDocumentIdentifier("test1")).thenReturn(doc4);
//
//        Set<DocumentServiceObject> documentsByState = documentService.getDocumentsByState(DocumentState.CREATED);
//
//        assertTrue(documentsByState.contains(doc1));
//        assertTrue(documentsByState.contains(doc2));
//
//
//    }




}