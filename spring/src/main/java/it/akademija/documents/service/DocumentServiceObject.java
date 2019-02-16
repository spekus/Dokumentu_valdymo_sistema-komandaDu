package it.akademija.documents.service;

import it.akademija.documents.DocumentState;
import it.akademija.files.repository.FileEntity;
import it.akademija.files.service.FileServiceObject;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class DocumentServiceObject {

    private String documentIdentifier;
    private String author;
    private String title;
    private String type;
    private DocumentState documentState;
    private String description;
    private LocalDateTime postedDate;
    private LocalDateTime approvalDate;
    private LocalDateTime rejectedDate;
    private String rejectedReason;
    private String approver;



    private Set<FileServiceObject> filesAttachedToDocument=new HashSet<>();

    public DocumentServiceObject(){

    }



    //What information document creator gets from API when the document is only created but not submitted
    public DocumentServiceObject(String documentIdentifier, String title, String type, String description) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.documentIdentifier =  documentIdentifier;

    }


    //What information document creator gets from database when the document is submitted
    public DocumentServiceObject(String documentIdentifier, String title, String type, String description, LocalDateTime postedDate) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.postedDate = postedDate;
        this.documentIdentifier =  documentIdentifier;


    }

    //What information document creator gets from database when the document is approved
    public DocumentServiceObject(String documentIdentifier, String title, String type, String description, LocalDateTime postedDate,
                                 LocalDateTime approvalDate, String approver) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.postedDate = postedDate;
        this.approvalDate = approvalDate;
        this.approver = approver;
        this.documentIdentifier =  documentIdentifier;

    }

    //What information document creator gets from database when the document is rejected
    public DocumentServiceObject(String documentIdentifier, String title, String type, String description, LocalDateTime postedDate,
                                 String approver, LocalDateTime rejectedDate, String rejectedReason) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.postedDate = postedDate;
        this.rejectedDate = rejectedDate;
        this.rejectedReason = rejectedReason;
        this.approver = approver;
        this.documentIdentifier =  documentIdentifier;

    }

    //What information document approver gets from database when the document is submitted from user
    public DocumentServiceObject(String documentIdentifier, String author, String title, String type, String description, LocalDateTime postedDate
    ) {
        this.author = author;
        this.title = title;
        this.type = type;
        this.description = description;
        this.postedDate = postedDate;
        this.documentIdentifier =  documentIdentifier;

    }

    public DocumentServiceObject(String documentIdentifier, String author, String title, String type, DocumentState documentState, String description, LocalDateTime postedDate, LocalDateTime approvalDate, LocalDateTime rejectedDate, String rejectedReason, String approver) {
        this.documentIdentifier = documentIdentifier;
        this.author = author;
        this.title = title;
        this.type = type;
        this.documentState = documentState;
        this.description = description;
        this.postedDate = postedDate;
        this.approvalDate = approvalDate;
        this.rejectedDate = rejectedDate;
        this.rejectedReason = rejectedReason;
        this.approver = approver;
    }

    //    //What information document approver gets from database when he already approved/rejected the document. ??
//    public DocumentServiceObject(String author, String title, String type, String description, LocalDateTime postedDate
//    ) {
//        this.author=author;
//        this.title = title;
//        this.type = type;
//        this.description = description;
//        this.postedDate=postedDate;
//
//    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(LocalDateTime rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }


    public DocumentState getDocumentState() {
        return documentState;
    }

    public void setDocumentState(DocumentState documentState) {
        this.documentState = documentState;
    }

    public Set<FileServiceObject> getFilesAttachedToDocument() {
        return filesAttachedToDocument;
    }

    public void setFilesAttachedToDocument(Set<FileServiceObject> filesAttachedToDocument) {
        this.filesAttachedToDocument = filesAttachedToDocument;
    }

}
