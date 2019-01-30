package it.akademija.documents.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value="document")
@RequestMapping(value = "/api/users")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService=documentService;
    }

    @RequestMapping(path="/{userIdentifier}/documents/{state}",method = RequestMethod.GET)
    @ApiOperation(value="Get user's documents", notes="Returns wanted user's documents")
    public List<DocumentServiceObject> getDocuments(@ApiParam(value="UserIdentifier", required=true)
                                             @Valid @PathVariable String userIdentifier,
                                                    @ApiParam(value="State", required=true)
    @Valid @PathVariable String state) {
        return documentService.getDocuments(userIdentifier, state);
    }

    @RequestMapping(path="/{userIdentifier}/documents", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value="Add user's document", notes="Adds new document to user's account")
    public void addDocument(@ApiParam(value="UniqueIdentifier", required=true) @PathVariable String userIdentifier,
                               @ApiParam(value="New document data", required=true) @Valid @RequestBody final CreateDocumentCommand p) {
        documentService.addDocument(userIdentifier, p.getTitle(), p.getType(), p.getDescription());
    }

    @RequestMapping(path="/documents/{documentIdentifier}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value="Edit document",notes="Changes selected document's data")
    public void updateDocument(
            @ApiParam(value="DocumentEntity identifier",required=true)
            @Valid
            @PathVariable final String documentIdentifier,
            @ApiParam(value="DocumentEntity data",required=true)
            @Valid
            @RequestBody final CreateDocumentCommand cmd) {

        documentService.updateDocument(documentIdentifier,cmd.getTitle(),cmd.getDescription(),
                cmd.getType());
    }

    @RequestMapping(path="/documents/{documentIdentifier}/submit", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value="Submit document",notes="Submits document for approval")
    public void submitDocument(
            @ApiParam(value="DocumentEntity identifier",required=true)
            @Valid
            @PathVariable final String documentIdentifier) {


        documentService.submitDocument(documentIdentifier);
    }




}
