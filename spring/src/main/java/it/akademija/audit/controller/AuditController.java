package it.akademija.audit.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.akademija.audit.AuditActionEnum;
import it.akademija.audit.ObjectTypeEnum;
import it.akademija.audit.service.AuditService;
import it.akademija.audit.service.AuditServiceObject;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.users.repository.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;

@RestController
@Api(value = "audit")
@RequestMapping("api/auditentries")
public class AuditController {


    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Lists all audit entries", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<AuditServiceObject> getAllAuditEntries(
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable sortByDate = PageRequest.of(page, size, Sort.by("date").descending());
//        Pageable sortByUsername = PageRequest.of(page, size, Sort.by("date").descending());
        try {
            return auditService.getAllAuditEntries(sortByDate);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Lists all audit entries filtered by criteria", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<AuditServiceObject> getAuditEntriesByCriteria(@RequestParam("criteria") String criteria,
                                                                    @RequestParam("page") int page,
                                                                    @RequestParam("size") int size) {
        Pageable sortByDate = PageRequest.of(page, size, Sort.by("date").descending());
        try {
            return auditService.getAuditEntriesByAnything(criteria, sortByDate);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
//
//    @RequestMapping(value = "user/get-documents-to-approve-filtered", method = RequestMethod.GET)
//    public Page<DocumentServiceObject> getDocumentsToApproveFiltered(@ApiIgnore Authentication authentication,
//                                                                     @RequestParam("page") int page,
//                                                                     @RequestParam("size") int size,
//                                                                     @RequestParam("criteria") String criteria) {
//        Pageable sortByTitle =
//                PageRequest.of(page, size, Sort.by("title").ascending());
//        try {
//            return userService.getDocumentsToApprove(authentication.getName(), sortByTitle, criteria);
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create new audit entry", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addNewAuditEntry(@RequestBody UserEntity ue,
                                 @RequestParam AuditActionEnum action,
                                 @RequestParam ObjectTypeEnum objectType,
                                 @RequestParam String objectIdentifier) {
        try {
            auditService.addNewAuditEntry(ue, action, objectType, objectIdentifier);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}


