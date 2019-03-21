package it.akademija.audit.service;

import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import it.akademija.audit.repository.AuditEntryEntity;
import it.akademija.audit.repository.AuditRepository;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

// getEntriesByObjectIdentifier, getEntriesByObjectType, getEntriesByLastname

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addNewAuditEntry(UserEntity ue, AuditActionEnum action, ObjectTypeEnum objectType, String objectIdentifier) throws IllegalArgumentException {
        if (ue == null) {
            throw new IllegalArgumentException("Naudotojo nėra");
        }
        if (objectIdentifier == null) {
            throw new IllegalArgumentException("Modifikuojamo objekto nėra");
        }

        AuditEntryEntity auditEntryEntity = new AuditEntryEntity();
        auditEntryEntity.setUsername(ue.getUsername());
        auditEntryEntity.setFirstname(ue.getFirstname());
        auditEntryEntity.setLastname(ue.getLastname());
        auditEntryEntity.setDate(LocalDateTime.now());
        auditEntryEntity.setAction(action);
        auditEntryEntity.setObjectType(objectType);
        auditEntryEntity.setObjectIdentifier(objectIdentifier);
        auditRepository.save(auditEntryEntity);

    }


    @Transactional
    public Page<AuditServiceObject> getAllAuditEntries(Pageable pageFormatDetails) {
        List<AuditServiceObject> allentriesSO =
                auditRepository
                .findAll(pageFormatDetails)
                .stream()
                .map(auditEntryEntity -> SOfromEntity(auditEntryEntity))
                .collect(Collectors.toList());
        long auditEntriesCount = auditRepository.getTotalAuditEntriesCount();
        PageImpl<AuditServiceObject> pagedData=new PageImpl<>(allentriesSO,pageFormatDetails,auditEntriesCount);
        return pagedData;
    }



    @Transactional
    public Page<AuditServiceObject> getAuditEntriesByAnything(String thing, Pageable pageFormatDetails) throws IllegalArgumentException {
        List<AuditServiceObject> allentriesSOByCriteria =
                auditRepository
                .findByAnything(thing.toLowerCase(), pageFormatDetails)
                .stream()
                .map(auditEntryEntity -> SOfromEntity(auditEntryEntity))
                .collect(Collectors.toList());
//        long auditEntriesCount = auditRepository.getTotalAuditEntriesCount();
        PageImpl<AuditServiceObject> pagedData=new PageImpl<>(allentriesSOByCriteria,pageFormatDetails,allentriesSOByCriteria.size());
        return pagedData;
    }


    static AuditServiceObject SOfromEntity(AuditEntryEntity entity) {
        AuditServiceObject so = new AuditServiceObject();
        so.setUsername(entity.getUsername());
        so.setFirstname(entity.getFirstname());
        so.setLastname(entity.getLastname());
        so.setDate(entity.getDate());
        so.setAction(entity.getAction());
        so.setObjectType(entity.getObjectType());
        so.setObjectIdentifier(entity.getObjectIdentifier());
        return so;
    }
}


