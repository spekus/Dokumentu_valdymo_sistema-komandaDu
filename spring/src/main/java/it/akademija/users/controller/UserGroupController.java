package it.akademija.users.controller;


import io.swagger.annotations.ApiOperation;
import it.akademija.users.service.UserGroupService;
import it.akademija.users.service.UserGroupServiceObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController

@RequestMapping("/api/usergroups")
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



    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "Lists all usergroups", notes = "")
    public Collection<UserGroupServiceObject> getAllGroups() {
        return userGroupService.getAllGroups();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create usergroup", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addNewUserGroup(@ApiIgnore Authentication authentication, @RequestBody CreateUserGroupCommand createUserGroupCommand) {
        userGroupService.addNewUserGroup(createUserGroupCommand, authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}", method = RequestMethod.POST)
    @ApiOperation(value = "Renames usergroup", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateGroupByTitle(@PathVariable("userGroupTitle") String userGroupTitle,
                                   @RequestParam("newTitle") String newTitle, @ApiIgnore Authentication authentication) {
        userGroupService.updateGroupByTitle(userGroupTitle, newTitle, authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}/add-person", method = RequestMethod.PUT)
    @ApiOperation(value = "Add group to user", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addGroupToUser(@ApiIgnore Authentication authentication,
                               @PathVariable("userGroupTitle") @NotNull @Length(min = 1) String userGroupTitle,
                               @RequestParam("username") @NotNull @Length(min = 1) String username) {

        userGroupService.addGroupToUser(userGroupTitle,username,authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}/remove-person", method = RequestMethod.PUT)
    @ApiOperation(value = "Remove group from user", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void removeGroupFromUser(@PathVariable("userGroupTitle") @NotNull @Length(min = 1) String userGroupTitle,
                                    @RequestParam("username") @NotNull @Length(min = 1) String username, @ApiIgnore Authentication authentication)
    {

        userGroupService.removeGroupFromUser(userGroupTitle,username, authentication.getName());
    }

    @RequestMapping(value = "/suspend-user", method = RequestMethod.PUT)
    @ApiOperation(value = "Suspend user", notes = "Suspends user so that he cannot log in to the system")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void suspendUser(
            @RequestParam("username") @NotNull @Length(min=1) String username, @ApiIgnore Authentication authentication) {
        userGroupService.suspendUser(username, authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}/add-document-type-to-upload", method = RequestMethod.PUT)
    @ApiOperation(value = "Add document types allowed to create documents for a group", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addDocumentTypeToUpload(@ApiIgnore Authentication authentication,
                                        @PathVariable("userGroupTitle") @NotNull @Length(min=1) String userGroupTitle,
                                        @RequestParam("documentTypeTitle") @NotNull @Length(min=1) String documentTypeTitle) {
        userGroupService.addDocumentTypeToUpload(userGroupTitle, documentTypeTitle,authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}/add-document-type-to-approve", method = RequestMethod.PUT)
    @ApiOperation(value = "Add document types allowed to approve for a group", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addDocumentTypeToApprove(@ApiIgnore Authentication authentication,
                                         @PathVariable("userGroupTitle") @NotNull @Length(min=1) String userGroupTitle,
                                         @RequestParam("documentTypeTitle") @NotNull @Length(min=1) String documentTypeTitle) {
        userGroupService.addDocumentTypeToApprove(userGroupTitle, documentTypeTitle,authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}/remove-document-type-to-upload", method = RequestMethod.PUT)
    @ApiOperation(value = "Removes document type allowed to create documents for a group", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void removeDocumentTypeToUpload(@ApiIgnore Authentication authentication,
                                           @PathVariable("userGroupTitle") @NotNull @Length(min=1) String userGroupTitle,
                                           @RequestParam("documentTypeTitle") @NotNull @Length(min=1) String documentTypeTitle) {
        userGroupService.removeDocumentTypeToUpload(userGroupTitle, documentTypeTitle,authentication.getName());
    }

    @RequestMapping(value = "/{userGroupTitle}/remove-document-type-to-approve", method = RequestMethod.PUT)
    @ApiOperation(value = "Removes document type allowed to approve documents for a group", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void removeDocumentTypeToApprove(@ApiIgnore Authentication authentication,
                                            @PathVariable("userGroupTitle") @NotNull @Length(min=1) String userGroupTitle,
                                            @RequestParam("documentTypeTitle") @NotNull @Length(min=1) String documentTypeTitle) {
        userGroupService.removeDocumentTypeToApprove(userGroupTitle, documentTypeTitle,authentication.getName());
    }





    @RequestMapping(value = "/{userGroupTitle}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete usergroup", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteGroup(@PathVariable("userGroupTitle") @NotNull @Length(min=1) String userGroupTitle, @ApiIgnore Authentication authentication) {
        userGroupService.deleteGroupByTitle(userGroupTitle, authentication.getName());
    }

//    @RequestMapping(value = "/{userGroupTitle}/addDocumentsToApprove", method = RequestMethod.PUT)
//    @ApiOperation(value = "Add documents to approve", notes = "")
//    public void addDocumentsToApprove(@PathVariable("userGroupTitle") String userGroupTitle,
//                                      @RequestParam("documentIdentifier") String documentIdentifier) {
//        userGroupService.addDocumentsToApprove(userGroupTitle, documentIdentifier);
//    }




}