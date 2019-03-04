package it.akademija.statistics.repository;


import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

@Repository
public class StatisticsDao {

    @PersistenceContext
    private EntityManager entityManager;


   //Dažniausiai pateikiančių dokumentus vartotojų sąrašas, surikiotas pagal pateiktų dok.skaičių.
    public List<Statistics> userListByPostedDocs(Set<String> types) {
        Query query = entityManager.createQuery("SELECT new it.akademija.statistics.repository.Statistics (COUNT(de), de.author) " +
                "FROM DocumentEntity de " +
                "WHERE de.type IN :types AND " +
                "de.postedDate is NOT NULL " +
                "GROUP BY de.author " +
                "ORDER BY COUNT(de) desc")
                .setParameter("types", types)
                .setMaxResults(7);
        return query.getResultList();
    }
}
