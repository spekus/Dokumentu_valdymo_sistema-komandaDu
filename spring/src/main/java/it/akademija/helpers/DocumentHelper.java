package it.akademija.documents;

import it.akademija.documents.repository.DocumentEntity;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.service.FileServiceObject;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("DocumentHelper")
public class DocumentHelper {
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



}
