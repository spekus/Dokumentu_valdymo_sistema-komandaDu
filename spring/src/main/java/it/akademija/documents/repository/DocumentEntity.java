package it.akademija.documents.repository;

import it.akademija.documents.DocumentState;
import it.akademija.files.repository.FileEntity;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserGroupEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "DOCUMENTS", indexes = {
        @Index(columnList = "documentIdentifier"),
        @Index(columnList = "documentState")
})
@NamedQueries({
        @NamedQuery(name = "DocumentEntity.getDocumentsToApprove",
                query = "select dta From DocumentEntity dta " +
                        "where dta.documentState='SUBMITTED' AND dta.type IN:types"),
        @NamedQuery(name = "DocumentEntity.getDocumentsToApproveSize",
                query = "select count(id) From DocumentEntity dta where dta.documentState='SUBMITTED' AND dta.type IN:types"),
        @NamedQuery(name="DocumentEntity.getDocumentsToApproveByCriteria",
        query="select dta From DocumentEntity dta where dta.documentState='SUBMITTED' AND dta.type IN:types " +
                "AND (dta.author=:criteria OR dta.type=:criteria)"),
        @NamedQuery(name="DocumentEntity.getDocumentsToApproveFilteredSize",
        query="select count(id) From DocumentEntity dta where dta.documentState='SUBMITTED' AND dta.type IN:types " +
        "AND (dta.author=:criteria OR dta.type=:criteria)")
})

public class DocumentEntity implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "documentIdentifier", unique = true, nullable = false)
    //@GeneratedValue(strategy=GenerationType.IDENTITY) //CIA DEL DUOMBAZES
    private String documentIdentifier = UUID.randomUUID().toString().replace("-", "");

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "documentState", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentState documentState = DocumentState.CREATED;

        @Column(name = "filesAttachedToDocument", nullable = false)
    @OneToMany(fetch = FetchType.LAZY)
//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<FileEntity> filesAttachedToDocument = new HashSet<>();

    @Column(name = "postedDate")
    private LocalDateTime postedDate;

    @Column(name = "approvalDate")
    private LocalDateTime approvalDate;

    @Column(name = "rejectedDate")
    private LocalDateTime rejectedDate;

    @Column(name = "approver")
    private String approver;

    @Column(name = "rejectionReason")
    private String rejectionReason;


    public DocumentEntity() {

    }

    public DocumentEntity(String title, String description, String type) {
        this.title = title;
        this.description = description;
        this.type = type;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;

    }

    public void setType(String type) {
        this.type = type;
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

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public DocumentState getDocumentState() {
        return documentState;
    }

    public void setDocumentState(DocumentState documentState) {
        this.documentState = documentState;

    }

    public Set<FileEntity> getFileSet() {
        return filesAttachedToDocument;
    }

    public void setFileSet(Set<FileEntity> filesAttachedToDocument) {
        this.filesAttachedToDocument = filesAttachedToDocument;
    }

    public void addFileToDocument(FileEntity fileEntity) {
        filesAttachedToDocument.add(fileEntity);
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public Set<FileEntity> getFilesAttachedToDocument() {
        return filesAttachedToDocument;
    }

    public void setFilesAttachedToDocument(Set<FileEntity> filesAttachedToDocument) {
        this.filesAttachedToDocument = filesAttachedToDocument;
    }

    @Override
    //using this for csv generation
    public String toString() {

        List<String> listOfFiles = new ArrayList<>();
        //get all file names attached this the documents
        for (FileEntity FileEntity: filesAttachedToDocument
             ) {
            listOfFiles.add(FileEntity.getFileName());
        }
        //converts file list to one string
        String fileNames = String.join("   ", listOfFiles);
        return
                documentIdentifier + "/,/" +
                        author + "/,/" +
                        title + "/,/" +
                        description + "/,/" +
                        type + "/,/" +
                        postedDate + "/,/" +
                        approvalDate + "/,/" +
                        rejectedDate + "/,/" +
                        approver + "/,/" +
                        rejectionReason + "/,/" +
                        fileNames
                ;
    }

    public String getFieldNames() {
        return "documentIdentifier" + ',' +
                "author" + ',' +
                "title" + ',' +
                "description" + ',' +
                "type" + ',' +
                "postedDate" + ',' +
                "approvalDate" + ',' +
                "rejectedDate" + ',' +
                "approver" + ',' +
                "rejectionReason" + ',' +
                "Attached file(s)"
                ;
    }

}


