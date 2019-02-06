package it.akademija.users.controller;


import it.akademija.documents.service.DocumentServiceObject;

import io.swagger.annotations.ApiOperation;

import it.akademija.users.service.UserGroupService;
import it.akademija.users.service.UserGroupServiceObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController

@RequestMapping("/api/usergroup")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }



    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }



    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Create usergroup", notes = "")
    public void addNewUserGroup(@RequestBody UserGroupServiceObject userGroupServiceObject) {
        userGroupService.addNewUserGroup(userGroupServiceObject);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "Lists all usergroups", notes = "")
    public Collection<UserGroupServiceObject> getAllGroups() {
        return userGroupService.getAllGroups();
    }


    @RequestMapping(value = "/{userGroupTitle}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete usergroup", notes = "")
    public void deleteGroup(@PathVariable("userGroupTitle") String userGroupTitle) {
        userGroupService.deleteGroupByTitle(userGroupTitle);
    }

    @RequestMapping(value = "/{userGroupTitle}", method = RequestMethod.POST)
    @ApiOperation(value = "Renames usergroup", notes = "")
    public void updateUserPassword(@PathVariable("userGroupTitle") String userGroupTitle,
                                   @RequestParam("newTitle") String newTitle) {
        userGroupService.updateGroupByTitle(userGroupTitle, newTitle);
    }

    @RequestMapping(value = "/{userGroupTitle}/addDocumentTypeToUpload", method = RequestMethod.PUT)
    @ApiOperation(value = "Add document types allowed to upload", notes = "")
    public void addDocumentTypeToUpload(@PathVariable("userGroupTitle") String userGroupTitle,
                                        @RequestParam("documentTypeTitle") String documentTypeTitle) {
        userGroupService.addDocumentTypeToUpload(userGroupTitle,documentTypeTitle);
    }

    @RequestMapping(value = "/{userGroupTitle}/addDocumentTypeToApprove", method = RequestMethod.PUT)
    @ApiOperation(value = "Add document types allowed to approve", notes = "")
    public void addDocumentTypeToApprove(@PathVariable("userGroupTitle") String userGroupTitle,
                                        @RequestParam("documentTypeTitle") String documentTypeTitle) {
        userGroupService.addDocumentTypeToApprove(userGroupTitle,documentTypeTitle);
    }

//    @RequestMapping(value = "/{userGroupTitle}/addDocumentsToApprove", method = RequestMethod.PUT)
//    @ApiOperation(value = "Add documents to approve", notes = "")
//    public void addDocumentsToApprove(@PathVariable("userGroupTitle") String userGroupTitle,
//                                         @RequestParam("documentIdentifier") String documentIdentifier) {
//        userGroupService.addDocumentsToApprove(userGroupTitle,documentIdentifier);
//    }

    @RequestMapping(value = "/getDocumentsToApprove", method = RequestMethod.GET)
    public Set<DocumentServiceObject> getDocumentsToApprove(@RequestParam("userIdentifier") String userIdentifier
                                      ) {
        return userGroupService.getDocumentsToApprove(userIdentifier);
    }

}
