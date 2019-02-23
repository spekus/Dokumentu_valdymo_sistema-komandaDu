package it.akademija.statistics.service;


import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.statistics.repository.Statistics;
import it.akademija.statistics.repository.StatisticsRepository;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;



@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    public StatisticsService(){}

    public StatisticsService(StatisticsRepository statisticsRepository, DocumentRepository documentRepository, UserRepository userRepository) {
        this.statisticsRepository = statisticsRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    public StatisticsRepository getStatisticsRepository() {
        return statisticsRepository;
    }

    public void setStatisticsRepository(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public DocumentRepository getDocumentRepository() {
        return documentRepository;
    }

    public void setDocumentRepository(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Collection<Statistics> getApprovedDocsStatistics(String username, LocalDateTime startDate, LocalDateTime endDate) {
        return statisticsRepository.countOperationsByState(getUserInitialsByUsername(username),startDate,endDate,DocumentState.APPROVED);
    }

    @Transactional
    public Collection<Statistics> getRejectedDocsStatistics(String username, LocalDateTime startDate, LocalDateTime endDate) {
        return statisticsRepository.countOperationsByState(getUserInitialsByUsername(username),startDate,endDate,DocumentState.REJECTED);
    }

    @Transactional
    private String getUserInitialsByUsername(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            String initials=userEntity.getFirstname()+" "+userEntity.getLastname();
            return initials;
        }
        return null;
    }

}
