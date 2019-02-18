package it.akademija.documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {

    DocumentTypeEntity findDocumentTypeByTitle(String title);
    void deleteDocumentTypeByTitle(String title);

    @Query("select distinct dTta from UserEntity ue JOIN ue.userGroups ueG JOIN ueG.availableDocumentTypesToApprove dTta where ue.username=:username")
    List<DocumentTypeEntity> getDocumentTypesToApproveByUsername(@Param("username") String username);
}
