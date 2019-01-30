package it.akademija.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {

    DocumentTypeEntity findDocumentTypeByTitle(String title);
    void deleteDocumentTypeByTitle(String title);
}
