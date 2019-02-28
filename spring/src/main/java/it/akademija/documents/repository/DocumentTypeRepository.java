package it.akademija.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {

    DocumentTypeEntity findDocumentTypeByTitle(String title);
    void deleteDocumentTypeByTitle(String title);

//    @Query("select distinct dTta from " +
//            "UserEntity ue JOIN ue.userGroups ueG " +
//            "JOIN ueG.availableDocumentTypesToApprove dTta " +
//            "where ue.username=:username")
    List<DocumentTypeEntity> getDocumentTypesToApproveByUsername(@Param("username") String username);


//    @Modifying
//    @Query(value ="insert into DOCUMENT_TYPE_ENTITY (TITLE) VALUES ('labDFSFSasassdsd')", nativeQuery = true)
////    @Query("select * from Document_Type_Entity")
//    void putTestData();



    //used for generating dummy data for document types
    @Modifying
    @Query(value ="insert into DOCUMENT_TYPE_ENTITY (TITLE) VALUES (:TITLE)", nativeQuery = true)
    void putDummyDocumentTypes(@Param("TITLE") String title);
}
