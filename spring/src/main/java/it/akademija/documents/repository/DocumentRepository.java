package it.akademija.documents.repository;

import it.akademija.documents.DocumentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    DocumentEntity findDocumentByDocumentIdentifier(String documentIdentifier);

    Set<DocumentEntity> findByDocumentState(DocumentState state);

    Set<DocumentEntity> findByDocumentStateAndAuthor(DocumentState state, String author);

    List<DocumentEntity> findByDocumentStateAndAuthor(DocumentState state, String author, Pageable pageable);

    void deleteDocumentByDocumentIdentifier(String documentIdentifier);

    Page<DocumentEntity> findByAuthor(String Author, Pageable pageable);

    List<DocumentEntity> findByAuthor(String Author);

    Long countByAuthor(String Author);

    List<DocumentEntity> getDocumentsToApprove(@Param("types") List<String> types, Pageable pageable);

    long getDocumentsToApproveSize(@Param("types") List<String> types);

    List<DocumentEntity> getDocumentsToApproveByCriteria(@Param("types") List<String> types, Pageable pageable,
                                                         @Param("criteria") String criteria);

    long getDocumentsToApproveFilteredSize(@Param("types") List<String> types, @Param("criteria") String criteria);

    @Modifying
    @Query(value = "insert into DOCUMENTS (DOCUMENT_IDENTIFIER ,AUTHOR, DESCRIPTION , DOCUMENT_STATE " +
            ", TITLE , TYPE) VALUES (:DocumentIdentifier,:AUTHOR, :DESCRIPTION, :STATE, :TITLE, 'Paraiška')",
            nativeQuery = true)
    void putDummyDocumentTypes(@Param("DocumentIdentifier") String DocumentIdentifier
            , @Param("TITLE") String title
            , @Param("DESCRIPTION") String description
            , @Param("AUTHOR") String author
            , @Param("STATE") String state);

    @Query("SELECT COUNT(d) FROM DocumentEntity d WHERE d.author =?1")
    Long getDocumentCountByUsername(String username);
}

