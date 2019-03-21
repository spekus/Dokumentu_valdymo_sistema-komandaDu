package it.akademija.audit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AuditRepository extends PagingAndSortingRepository<AuditEntryEntity, Long> {

    List<AuditEntryEntity> findAllByOrderByDateDesc();

    List<AuditEntryEntity> findAll();

    List<AuditEntryEntity> findByUsernameIgnoreCase(String username);

    @Query("select a from AuditEntryEntity a where " +
            "lower(a.firstname)=?1 " +
            "OR lower(a.lastname)=?1 " +
            "OR lower(a.username)=?1 " +
            "OR lower(a.objectIdentifier) LIKE CONCAT('%', ?1, '%')  ORDER BY date desc")
    List<AuditEntryEntity> findByAnything(String criteria, Pageable pageAble);


    @Query("select a from AuditEntryEntity a")
    List<AuditEntryEntity> getAllAuditEntries(Pageable pageAble);

    @Query("select count(ae.id) From AuditEntryEntity ae")
    long getTotalAuditEntriesCount();

}
