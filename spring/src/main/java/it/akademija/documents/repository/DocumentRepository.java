package it.akademija.documents.repository;

import it.akademija.documents.DocumentState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    DocumentEntity findDocumentByDocumentIdentifier(String documentIdentifier);
    Set<DocumentEntity> findByDocumentState(DocumentState state);
    Set<DocumentEntity> findByDocumentStateAndAuthor(DocumentState state, String author);
    void deleteDocumentByDocumentIdentifier(String documentIdentifier);
}
