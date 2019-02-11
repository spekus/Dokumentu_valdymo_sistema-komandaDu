package it.akademija.documents.repository;

import it.akademija.documents.DocumentState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    public DocumentEntity findDocumentByDocumentIdentifier(String documentIdentifier);
    public Set<DocumentEntity> findByDocumentState(DocumentState state);
    public Set<DocumentEntity> findByDocumentStateAndAuthor(DocumentState state, String author);
//    public Set<DocumentEntity> findDocumentsByUserIdentifier(String userIdentifier);
    void deleteDocumentByDocumentIdentifier(String documentIdentifier);
}
