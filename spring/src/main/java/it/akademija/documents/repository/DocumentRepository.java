package it.akademija.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    DocumentEntity findDocumentByDocumentIdentifier(String documentIdentifier);
}
