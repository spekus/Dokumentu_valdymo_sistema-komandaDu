package it.akademija.statistics.repository;

import it.akademija.documents.repository.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<DocumentEntity, Long> {

    @Query("SELECT new it.akademija.statistics.repository.Statistics(COUNT(de), de.type) " +
            "FROM DocumentEntity de " +
            "WHERE de.approver=:approver AND de.documentState=:state " +
            " and de.approvalDate BETWEEN :startDate AND :endDate " +
            "GROUP BY de.type")
    List<Statistics> countOperationsByState(@Param("approver") String approver,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("state")Enum state);

}






