package it.akademija.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    public DocumentEntity findDocumentByDocumentIdentifier(String documentIdentifier);
//    public Set<DocumentEntity> findDocumentsByUserIdentifier(String userIdentifier);
}
