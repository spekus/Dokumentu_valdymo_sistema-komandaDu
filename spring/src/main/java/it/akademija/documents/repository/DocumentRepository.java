package it.akademija.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Document findDocumentByDocumentIdentifier(String documentIdentifier);
}
