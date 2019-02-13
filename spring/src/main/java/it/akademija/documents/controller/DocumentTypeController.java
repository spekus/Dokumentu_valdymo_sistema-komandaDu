package it.akademija.documents.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.akademija.documents.service.DocumentTypeService;
import it.akademija.documents.service.DocumentTypeServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Set;

@RestController
@Api(value="documentType")
@RequestMapping(value = "/api/documentTypes")
public class DocumentTypeController {
    private final DocumentTypeService documentTypeService;

    @Autowired
    public DocumentTypeController (DocumentTypeService documentTypeService) {
        this.documentTypeService=documentTypeService;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value="Get all document types", notes="Returns all created document types")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Set<DocumentTypeServiceObject> getDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }

    @RequestMapping(value = "/allowed", method = RequestMethod.GET)
    @ApiOperation(value="Get all document types which current user is allowed to upload", notes="Returns all allowed document types")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Set<DocumentTypeServiceObject> getAllowedDocumentTypes(@ApiIgnore Authentication authentication) {
        return documentTypeService.getAllAllowedDocumentTypes(authentication.getName());
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value="Create new document type", notes="Creates new document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createDocumentType (@RequestBody CreateDocumentTypeCommand p) {
        documentTypeService.createNewDocumentType(p.getTitle());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value="Update document type", notes="Updates document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateDocumentType (String currentTitle, CreateDocumentTypeCommand p) {
        documentTypeService.updateDocumentType(currentTitle, p.getTitle());
    }

    @RequestMapping(value = "/{title}", method = RequestMethod.DELETE)
//    @RequestMapping( method = RequestMethod.DELETE)
    @ApiOperation(value="Delete document type", notes="Deletes document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void deleteDocumentType (@PathVariable("title") String title) {
//    public void deleteDocumentType ( String title) {
        documentTypeService.deleteDocumentType(title);
    }


}
