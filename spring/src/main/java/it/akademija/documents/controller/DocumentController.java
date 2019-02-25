package it.akademija.documents.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import it.akademija.documents.DocumentState;

import it.akademija.documents.repository.DocumentEntity;

import it.akademija.documents.service.DocumentService;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.exceptions.NoApproverAvailableException;
import it.akademija.files.ResponseTransfer;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
@Api(value = "document")
@RequestMapping(value = "/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @RequestMapping(value = "/{state}", method = RequestMethod.GET)
    @ApiOperation(value = "Get all documents by state", notes = "Returns all documents in a given state")
    public Set<DocumentServiceObject> getDocumentsByState(
            @ApiParam(value = "State", required = true)
            @Valid @PathVariable DocumentState state) throws IllegalArgumentException {
        return documentService.getDocumentsByState(state);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get document", notes = "Returns one document")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public DocumentServiceObject getDocument(@ApiParam(value = "DocumentIdentifier", required = true)

                                             @RequestParam @Valid @NotNull @Length(min = 1) String documentIdentifier) {
        try {

            return documentService.getDocument(documentIdentifier);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }




    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create user document", notes = "Creates new user's document")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public ResponseTransfer addDocument(@ApiIgnore Authentication authentication,
                                        @ApiParam(value = "New document data", required = true) @Valid @RequestBody final CreateDocumentCommand p) {
        //creates document
        DocumentEntity documentEntity =
                documentService.createDocument(authentication.getName(), p.getTitle(), p.getType(), p.getDescription());
        //this added so that somehow we can get document identifier to merge document and file.
        return new ResponseTransfer(documentEntity.getDocumentIdentifier());

    }

    @RequestMapping(value = "/{documentIdentifier}/submit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Submit document", notes = "Submits document for approval")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void submitDocument(
            @ApiParam(value = "DocumentEntity identifier", required = true)
            @Valid
            @PathVariable final @NotNull @Length(min = 1) String documentIdentifier) {

        try {
            documentService.submitDocument(documentIdentifier);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (NoApproverAvailableException e) {
            throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
        }

    }

    @RequestMapping(value = "/{documentIdentifier}/approve", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Approve document", notes = "Approves document")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void approveDocument(
            @ApiParam(value = "DocumentEntity identifier", required = true)
            @Valid
            @PathVariable @NotNull @Length(min = 1) String documentIdentifier,
            @ApiIgnore Authentication authentication) {
        try {
            documentService.approveOrRejectDocument(documentIdentifier, authentication.getName(), DocumentState.APPROVED, "");
        }
        catch (IllegalArgumentException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (SecurityException e)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @RequestMapping(value = "/{documentIdentifier}/reject", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Reject document", notes = "Rejects document")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void rejectDocument(
            @ApiParam(value = "DocumentEntity identifier", required = true)
            @Valid
            @PathVariable @NotNull @Length(min = 1) String documentIdentifier,
            @ApiIgnore Authentication authentication,
            @RequestParam @NotNull @Length(min = 1) String rejectedReason) {
        try {
            documentService.approveOrRejectDocument(documentIdentifier, authentication.getName(), DocumentState.REJECTED, rejectedReason);
        }
        catch (IllegalArgumentException e)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch (SecurityException e)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }


    }

    @RequestMapping(value = "/{documentIdentifier}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete document", notes = "Deletes one document")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void deleteDocument(@ApiParam(value = "DocumentIdentifier", required = true)
                               @Valid @PathVariable @NotNull @Length(min = 1) String documentIdentifier) {
        documentService.deleteDocument(documentIdentifier);
    }


}
