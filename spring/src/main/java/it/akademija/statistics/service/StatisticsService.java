package it.akademija.statistics.service;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.statistics.repository.Statistics;
import it.akademija.statistics.repository.StatisticsDao;
import it.akademija.statistics.repository.StatisticsRepository;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatisticsDao statisticsDao;

    public StatisticsService() {
    }

    public StatisticsService(StatisticsRepository statisticsRepository, DocumentRepository documentRepository,
                             UserRepository userRepository, StatisticsDao statisticsDao) {
        this.statisticsRepository = statisticsRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.statisticsDao = statisticsDao;
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

    public StatisticsDao getStatisticsDao() {
        return statisticsDao;
    }

    public void setStatisticsDao(StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
    }

    //Gauname prisijungusio userio, patvirtintų dokumentų statistiką
    @Transactional
    public Collection<Statistics> getApprovedDocsStatistics(String username, LocalDateTime startDate,
                                                            LocalDateTime endDate) {
        return statisticsRepository.countApprovementsByState(getDocTypesToApprove(username),
                startDate, endDate, DocumentState.APPROVED);
    }

    //Gauname prisijungusio userio, atmestų dokumentų statistiką
    @Transactional
    public Collection<Statistics> getRejectedDocsStatistics(String username, LocalDateTime startDate,
                                                            LocalDateTime endDate) {
        return statisticsRepository.countRejectionsByState(getDocTypesToApprove(username),
                startDate, endDate, DocumentState.REJECTED);
    }

    //Gauname prisijungusio userio, gautų peržiūrai dokumentų statistiką
    @Transactional
    public Collection<Statistics> getPostedDocsStatistics(String username, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        return statisticsRepository.countPostedByState(getDocTypesToApprove(username), startDate, endDate);
    }

    /*Gauname surikiotą vartotojų sąrašą, kurie pateikė daugiausiai
     dokumentų user'ui, kuris gali tvirtinti to tipo dokumentus.*/
    @Transactional
    public Collection<Statistics> getUserListByPostedDocs(String username) {
        return statisticsDao.userListByPostedDocs(getDocTypesToApprove(username));
    }

    /*Ištraukiame iš userio kokius dokumentų tipus jis gali tvirtinti,
    tam kad išfiltruoti pateiktų dokumentų sąrašą šitam useriui*/
    @Transactional
    private Set<String> getDocTypesToApprove(String username) {
        UserEntity userEntity = userRepository.findUserByUsername(username);
        if (userEntity != null) {
            Set<UserGroupEntity> groupsUserBelongsTo = userEntity.getUserGroups();
            Set<DocumentTypeEntity> allDocTypesUserCanApprove = new HashSet<>();
            for (UserGroupEntity userGroupEntity : groupsUserBelongsTo) {
                allDocTypesUserCanApprove.addAll(userGroupEntity.getAvailableDocumentTypesToApprove());
            }
            return allDocTypesUserCanApprove.stream().map(documentTypeEntity ->
                    documentTypeEntity.getTitle()).collect(Collectors.toSet());
        }
        return null;
    }
}
