package it.akademija.statistics.repository;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatisticsRepository extends JpaRepository<DocumentEntity, Long> {

    @Query("SELECT new it.akademija.statistics.repository.Statistics(COUNT(de), de.type) " +
            "FROM DocumentEntity de " +
            "WHERE de.type IN :types AND de.documentState=:state " +
            "AND de.approvalDate BETWEEN :startDate AND :endDate " +
            "GROUP BY de.type")
    List<Statistics> countApprovementsByState(@Param("types") Set<String> types,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("state") DocumentState state);

    @Query("SELECT new it.akademija.statistics.repository.Statistics(COUNT(de), de.type) " +
            "FROM DocumentEntity de " +
            "WHERE de.type IN :types AND de.documentState=:state " +
            "AND de.rejectedDate BETWEEN :startDate AND :endDate " +
            "GROUP BY de.type")
    List<Statistics> countRejectionsByState(@Param("types") Set<String> types,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("state") DocumentState state);

    @Query("SELECT new it.akademija.statistics.repository.Statistics(COUNT(de), de.type) " +
            "FROM DocumentEntity de " +
            "WHERE de.type IN :types AND " +
            "de.postedDate BETWEEN :startDate AND :endDate " +
            "GROUP BY de.type")
    List<Statistics> countPostedByState(@Param("types") Set<String> types,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

}






