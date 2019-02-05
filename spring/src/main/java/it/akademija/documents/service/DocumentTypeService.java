package it.akademija.documents.service;

import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Transactional
    public Set<DocumentTypeServiceObject> getAllDocumentTypes () {
        return documentTypeRepository.findAll().stream().map((docType) ->
                new DocumentTypeServiceObject(docType.getTitle()))
                .collect(Collectors.toSet());

    }

    @Transactional
    public void createNewDocumentType (String title) {
        DocumentTypeEntity newDocumentTypeEntity=new DocumentTypeEntity(title);
        documentTypeRepository.save(newDocumentTypeEntity);
    }

    @Transactional
    public void updateDocumentType(String currentTitle, String wantedTitle) {
        DocumentTypeEntity documentType=documentTypeRepository.findDocumentTypeByTitle(currentTitle);
        if (wantedTitle!=null && !wantedTitle.isEmpty()) {
            documentType.setTitle(wantedTitle);
            documentTypeRepository.save(documentType);
        }

    }

    @Transactional
    public void deleteDocumentType(String title) {
        documentTypeRepository.deleteDocumentTypeByTitle(title);


    }

    public DocumentTypeRepository getDocumentTypeRepository() {
        return documentTypeRepository;
    }

    public void setDocumentTypeRepository(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }
}
