package it.akademija.documents.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.akademija.documents.service.DocumentTypeService;
import it.akademija.documents.service.DocumentTypeServiceObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
@Api(value = "documentType")
@RequestMapping(value = "/api/document-types")
public class DocumentTypeController {
    private final DocumentTypeService documentTypeService;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Get all document types", notes = "Returns all created document types")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Set<DocumentTypeServiceObject> getDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create new document type", notes = "Creates new document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createDocumentType(@RequestBody CreateDocumentTypeCommand p, @ApiIgnore Authentication authentication) {
        documentTypeService.createNewDocumentType(p.getTitle(), authentication.getName());
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ApiOperation(value = "Update document type", notes = "Updates document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateDocumentType(@RequestParam("currentTitle") @NotNull @Length(min = 1) String currentTitle,
                                   @RequestBody CreateDocumentTypeCommand p, @ApiIgnore Authentication authentication) {
        documentTypeService.updateDocumentType(currentTitle, p.getTitle(), authentication.getName());
    }

    @RequestMapping(value = "/{title}", method = RequestMethod.DELETE)

    @ApiOperation(value = "Delete document type", notes = "Deletes document type")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void deleteDocumentType(@PathVariable("title") @NotNull @Length(min = 1) String title,
                                   @ApiIgnore Authentication authentication) {
        documentTypeService.deleteDocumentType(title, authentication.getName());
    }


}
