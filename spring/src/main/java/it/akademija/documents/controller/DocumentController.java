package it.akademija.documents.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import it.akademija.documents.DocumentState;

import it.akademija.documents.repository.DocumentEntity;

import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.files.ResponseTransfer;
import it.akademija.files.service.FileServiceObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@Api(value="document")
@RequestMapping(value = "/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService=documentService;
    }

    @RequestMapping(path="/{userIdentifier}/documents/{state}",method = RequestMethod.GET)
    @ApiOperation(value="Get user's documents by state", notes="Returns wanted user's documents by state")
    public Set<DocumentServiceObject> getDocuments(@ApiParam(value="UserIdentifier", required=true)
                                             @Valid @PathVariable String userIdentifier,
                                                   @ApiParam(value="State", required=true)
    @Valid @PathVariable DocumentState state) {
        return documentService.getDocumentsByState(userIdentifier, state);
    }

    @RequestMapping(path="/{userIdentifier}/documents",method = RequestMethod.GET)
    @ApiOperation(value="Get all user's documents", notes="Returns wanted user's all documents")
    public Set<DocumentServiceObject> getAllUserDocuments(@ApiParam(value="UserIdentifier", required=true)
                                                    @Valid @PathVariable String userIdentifier)
                                                     {
        return documentService.getAllUserDocuments(userIdentifier);
    }


    @RequestMapping(path="/{userIdentifier}/documentAddToGroups", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value="Add user's document", notes="Adds new document to user's account")
    public ResponseTransfer addDocument(@ApiParam(value="UniqueIdentifier", required=true) @PathVariable String userIdentifier,
                               @ApiParam(value="New document data", required=true) @Valid @RequestBody final CreateDocumentCommand p) {
        //creates document
        DocumentEntity documentEntity =
        documentService.addDocument(userIdentifier, p.getTitle(), p.getType(), p.getDescription());
        //this added so that somehow we can get document identifier to merge document and file.
        return new ResponseTransfer(documentEntity.getDocumentIdentifier());

    }

//    @RequestMapping(path="/documents/{documentIdentifier}", method = RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.OK)
//    @ApiOperation(value="Edit document",notes="Changes selected document's data")
//    public void updateDocument(
//            @ApiParam(value="DocumentEntity identifier",required=true)
//            @Valid
//            @PathVariable final String documentIdentifier,
//            @ApiParam(value="DocumentEntity data",required=true)
//            @Valid
//            @RequestBody final CreateDocumentCommand cmd) {
//
//        documentService.updateDocument(documentIdentifier,cmd.getTitle(),cmd.getDescription(),
//                cmd.getType());
//    }

    @RequestMapping(path="/documents/{documentIdentifier}/submitForApproval", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value="Submit document",notes="Submits document for approval")
    public void submitDocument(
            @ApiParam(value="DocumentEntity identifier",required=true)
            @Valid
            @PathVariable final String documentIdentifier) {


        documentService.submitDocument(documentIdentifier);
    }

    @RequestMapping(path="/documents/{documentIdentifier}/approve", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value="Approve document",notes="Approve's document")
    public void approveDocument(
            @ApiParam(value="DocumentEntity identifier",required=true)
            @Valid
            @PathVariable final String documentIdentifier) {
        documentService.approveDocument(documentIdentifier);
    }






//    @RequestMapping(path="/documents/{documentIdentifier}/submit", method = RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.OK)
//    @ApiOperation(value="Submit document",notes="Submits document for approval")
//    public void approveDocument(
//            @ApiParam(value="DocumentEntity identifier",required=true)
//            @Valid
//            @PathVariable final String documentIdentifier) {
//
//
//        documentService.submitDocument(documentIdentifier);
//    }

    @RequestMapping(path="/{documentIdentifier}",method = RequestMethod.GET)
    @ApiOperation(value="Get document", notes="Returns one document")
    public DocumentServiceObject getDocument(@ApiParam(value="DocumentIdentifier", required=true)
                                                          @Valid @PathVariable @NotNull @Length(min=1) String documentIdentifier)
    {
        return documentService.getDocument(documentIdentifier);
    }






}
