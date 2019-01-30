package it.akademija.documents.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.akademija.documents.service.DocumentTypeService;
import it.akademija.documents.service.DocumentTypeServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@Api(value="documentType")
@RequestMapping(value = "/api/documentTypes")
public class DocumentTypeController {
    private final DocumentTypeService documentTypeService;

    @Autowired
    private DocumentTypeController (DocumentTypeService documentTypeService) {
        this.documentTypeService=documentTypeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value="Get all document types", notes="Returns all created document types")
    public Set<DocumentTypeServiceObject> getDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value="Create new document type", notes="Creates new document type")
    public void createDocumentType (CreateDocumentTypeCommand p) {
        documentTypeService.createNewDocumentType(p.getTitle());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value="Update document type", notes="Updates document type")
    public void updateDocumentType (String currentTitle, CreateDocumentTypeCommand p) {
        documentTypeService.updateDocumentType(currentTitle, p.getTitle());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(value="Delete document type", notes="Deletes document type")
    public void deleteDocumentType (String title) {
        documentTypeService.deleteDocumentType(title);
    }




}
