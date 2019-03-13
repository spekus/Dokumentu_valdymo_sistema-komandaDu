package it.akademija.helpers;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.repository.DocumentRepository;
import it.akademija.documents.repository.DocumentTypeEntity;
import it.akademija.documents.repository.DocumentTypeRepository;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.service.FileServiceObject;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;




@Component("DocumentHelper")
public class DocumentHelper {

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;
    
    public DocumentServiceObject SOfromEntity(DocumentEntity entity) {
        DocumentServiceObject so = new DocumentServiceObject();

        so.setApprovalDate(entity.getApprovalDate());
        so.setApprover(entity.getApprover());
        so.setAuthor(entity.getAuthor());
        so.setDescription(entity.getDescription());
        so.setDocumentIdentifier(entity.getDocumentIdentifier());
        so.setDocumentState(entity.getDocumentState());
        so.setPostedDate(entity.getPostedDate());
        so.setRejectedDate(entity.getRejectedDate());
        so.setRejectedReason(entity.getRejectionReason());
        so.setTitle(entity.getTitle());
        so.setType(entity.getType());


        so.setFilesAttachedToDocument(entity.getFileSet()
                .stream()
                .map(file -> new FileServiceObject(file.getFileName(), file.getContentType(), file.getSize(), file.getIdentifier()))
                .collect(Collectors.toSet()));
        return so;
    }

    public DocumentServiceObject SOfromEntityWithoutFiles(DocumentEntity entity) {
        DocumentServiceObject so = new DocumentServiceObject();
        so.setApprovalDate(entity.getApprovalDate());
        so.setApprover(entity.getApprover());
        so.setAuthor(entity.getAuthor());
        so.setDescription(entity.getDescription());
        so.setDocumentIdentifier(entity.getDocumentIdentifier());
        so.setDocumentState(entity.getDocumentState());
        so.setPostedDate(entity.getPostedDate());
        so.setRejectedDate(entity.getRejectedDate());
        so.setRejectedReason(entity.getRejectionReason());
        so.setTitle(entity.getTitle());
        so.setType(entity.getType());
        return so;
    }

    // without files is needed for perfomance boost
    public List<DocumentServiceObject> ConvertToServiceObjListWithoutFiles(Collection<DocumentEntity> allDocumentsToApprove) {
        return allDocumentsToApprove
                .stream()
                .map(documentEntity -> SOfromEntityWithoutFiles(documentEntity))
                .collect(Collectors.toList());
    }
    public List<DocumentServiceObject> ConvertToServiceObjList(Collection<DocumentEntity> allDocumentsToApprove) {
        return allDocumentsToApprove
                .stream()
                .map(documentEntity -> SOfromEntity(documentEntity))
                .collect(Collectors.toList());
    }

    public List<String> getDocumentTypesUserCanAprooveBy(String userName) {
        List<DocumentTypeEntity> documentTypeList =
                documentTypeRepository.getDocumentTypesToApproveByUsername(userName);
        return documentTypeList.stream().map(DocumentTypeEntity::getTitle).collect(Collectors.toList());
    }


    public List<DocumentServiceObject> getDocumentsBy(List<String> documentTypesForAproval, Pageable pageFormatDetails) {
        List<DocumentEntity> documentsByType =
                documentRepository.getDocumentsToApprove(documentTypesForAproval, pageFormatDetails);
        return ConvertToServiceObjListWithoutFiles(documentsByType);
    }
    //use this if you want to get all documents of the type and filter them by username or document type
    public List<DocumentServiceObject> getDocumentsBy(List<String> documentTypesForAproval
            , Pageable sortByTitle , String filteringCriteria) {
                List<DocumentEntity> documentsByTypeAndCriteria = documentRepository.getDocumentsToApproveByCriteria(
                documentTypesForAproval, sortByTitle, filteringCriteria);
        return ConvertToServiceObjListWithoutFiles(documentsByTypeAndCriteria);
    }
    public List<DocumentServiceObject> getDocumentsBy(String userName) {
        Set<DocumentEntity> allUserDocuments = userRepository.findByUsername(userName).getDocumentEntities();
        List<DocumentServiceObject> convertedUserDocuments = ConvertToServiceObjList(allUserDocuments);
        Collections.sort(convertedUserDocuments);
        return convertedUserDocuments;
    }
    public List<DocumentServiceObject> getDocumentsBy(String userName, DocumentState documentState) {
        Set<DocumentEntity> allUserDocuments = userRepository.findByUsername(userName).getDocumentEntities();
        List<DocumentServiceObject> filteredDocuments = filterDocumentsByState(allUserDocuments, documentState);
        Collections.sort(filteredDocuments);
        return filteredDocuments;
    }

    private List<DocumentServiceObject> filterDocumentsByState(Set<DocumentEntity> userDocuments, DocumentState documentState) {
        return userDocuments.stream()
                .filter(p -> p.getDocumentState().equals(documentState))
                .map(this::SOfromEntity).sorted().collect(Collectors.toList());
    }
    //TO BE REWORKED
    public PageImpl<DocumentServiceObject> getPage(Pageable pageFormatDetails, List<DocumentServiceObject> documentServiceObjects) {
        List<DocumentServiceObject> filteredList= new ArrayList<>();
        int possition = pageFormatDetails.getPageNumber() * pageFormatDetails.getPageSize();
        int limit = possition + pageFormatDetails.getPageSize();
        if(limit > documentServiceObjects.size()){
            // checks that limit set by paging is not bigger than total element count
            limit = documentServiceObjects.size();
        }
        for(; possition < (limit); possition++){
            filteredList.add(documentServiceObjects.get(possition));
        }
        PageImpl<DocumentServiceObject> pageData = new PageImpl<DocumentServiceObject>(filteredList,
                pageFormatDetails, documentServiceObjects.size());
        return pageData;


    }
}
