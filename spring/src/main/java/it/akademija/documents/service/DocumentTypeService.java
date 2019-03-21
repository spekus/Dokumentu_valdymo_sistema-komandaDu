package it.akademija.documents.service;

import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import it.akademija.audit.service.AuditService;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    @Transactional
    public Set<DocumentTypeServiceObject> getAllDocumentTypes() {
        return documentTypeRepository.findAll().stream().map(
                (docType) ->
                        new DocumentTypeServiceObject(docType.getTitle()))
                .collect(Collectors.toSet());

    }

    @Transactional
    public void createNewDocumentType(String title, String username) {
        DocumentTypeEntity newDocumentTypeEntity = new DocumentTypeEntity(title);
        documentTypeRepository.save(newDocumentTypeEntity);

        UserEntity user = userRepository.findUserByUsername(username);
        if (user != null) {
            auditService.addNewAuditEntry(user, AuditActionEnum.CREATE_NEW_DOCUMENT_TYPE,
                    ObjectTypeEnum.DOCUMENTTYPE, title);
        }
    }

    @Transactional
    public void updateDocumentType(String currentTitle, String wantedTitle, String username) {
        DocumentTypeEntity documentType = documentTypeRepository.findDocumentTypeByTitle(currentTitle);
        if (wantedTitle != null && !wantedTitle.isEmpty()) {
            documentType.setTitle(wantedTitle);
            documentTypeRepository.save(documentType);

            UserEntity user = userRepository.findUserByUsername(username);
            if (user != null) {
                auditService.addNewAuditEntry(user, AuditActionEnum.MODIFY_DOCUMENT_TYPE,
                        ObjectTypeEnum.DOCUMENTTYPE, wantedTitle);
            }
        }
    }

    @Transactional
    public void deleteDocumentType(String title, String username) {
        documentTypeRepository.deleteDocumentTypeByTitle(title);

        UserEntity user = userRepository.findUserByUsername(username);
        if (user != null) {
            auditService.addNewAuditEntry(user, AuditActionEnum.DELETE_DOCUMENT_TYPE, ObjectTypeEnum.DOCUMENTTYPE, title);
        }
    }
}


