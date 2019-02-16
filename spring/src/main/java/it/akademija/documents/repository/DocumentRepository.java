package it.akademija.documents.repository;

import it.akademija.documents.DocumentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Set;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    DocumentEntity findDocumentByDocumentIdentifier(String documentIdentifier);
    Set<DocumentEntity> findByDocumentState(DocumentState state);
    Set<DocumentEntity> findByDocumentStateAndAuthor(DocumentState state, String author);
    List<DocumentEntity> findByDocumentStateAndAuthor(DocumentState state, String author, Pageable pageable);
    void deleteDocumentByDocumentIdentifier(String documentIdentifier);

    //we can choose to return either a Page<T>, a Slice<T> or a List<T>
    // from any of our custom methods returning a paginated data.
    // so no Set. Otherwise server will crash
    public List<DocumentEntity> findByAuthor(String Author, Pageable pageable);
    public List<DocumentEntity> findByAuthor(String Author);

//    public List<DocumentEntity> findByOrderByAuthorAscTitleAsc(String Author,Pageable pageable);



}
