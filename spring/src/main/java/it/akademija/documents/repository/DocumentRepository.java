package it.akademija.documents.repository;

import it.akademija.documents.DocumentState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    //we can choose to return either a Page<T>, a Slice<T> or a List<T>
    // from any of our custom methods returning a paginated data.
    // so no Set. Otherwise server will crash
    public List<DocumentEntity> findByAuthor(String Author, Pageable pageable);

    public List<DocumentEntity> findByAuthor(String Author);

    @Query("select dta From DocumentEntity dta " +
            "where dta.documentState='SUBMITTED' AND dta.type IN:types")
    List<DocumentEntity> getDocumentsToApprove(@Param("types") List<String> types, Pageable pageable);

    @Query("select dta From DocumentEntity dta " +
            "where dta.documentState='SUBMITTED' AND dta.type IN:types")
    List<DocumentEntity> getDocumentsToApprove(@Param("types") List<String> types);

//    @Query( //THIS IS NATIVE query which can be used to generate CSV file more efficiently
//            value = "CALL CSVWRITE('test.csv', 'SELECT * FROM DOCUMENTS ', 'charset=UTF-8 fieldSeparator=' || CHAR(9));",
//            nativeQuery = true)
//    public void findAllNative();

//    public List<DocumentEntity> findByOrderByAuthorAscTitleAsc(String Author,Pageable pageable);

}

