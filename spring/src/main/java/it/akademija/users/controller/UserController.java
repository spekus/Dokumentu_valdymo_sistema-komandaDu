package it.akademija.users.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.akademija.documents.DocumentState;
import it.akademija.documents.service.DocumentServiceObject;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.service.UserGroupServiceObject;
import it.akademija.users.service.UserService;
import it.akademija.users.service.UserServiceObject;
import org.hibernate.validator.constraints.Length;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "user/documents/{state}", method = RequestMethod.GET)
    @ApiOperation(value = "Get user's documents by state", notes = "Returns wanted user's documents by state")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Page<DocumentServiceObject> getDocuments(@ApiIgnore Authentication authentication,
                                                    @ApiParam(value = "State", required = true)
                                                    @Valid @PathVariable DocumentState state,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size) throws IllegalArgumentException {
        Pageable pagingInformation =
                PageRequest.of(page, size);
        try {
            return userService.getUserDocumentsByState(authentication.getName(), state, pagingInformation);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "user/documents", method = RequestMethod.GET)
    @ApiOperation(value = "Get all user's documents", notes = "Returns wanted user's all documents")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Page<DocumentServiceObject> getAllUserDocuments(@ApiIgnore Authentication authentication
            , @RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable sortedByTitle =
                PageRequest.of(page, size, Sort.by("title").ascending());
        try {
            return userService.getAllUserDocuments(authentication.getName(), sortedByTitle);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "List all users and all related info", notes = "Lists all users all information")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Page<UserServiceObject> getAllUsers(@RequestParam("page") int page,
                                               @RequestParam("size") int size) {
        Pageable sortByUsername = PageRequest.of(page, size, Sort.by("username").ascending());

        try {
            return userService.getAllUsers(sortByUsername);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get info on user", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public UserServiceObject getUserByUsername(@PathVariable("username") @NotNull @Length(min = 1) String username) {
        return userService.getUserByUsername(username);
    }

    @RequestMapping(value = "/user/usergroups", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's groups", notes = "Gets user's groups")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<UserGroupServiceObject> getUserGroups(@ApiIgnore Authentication authentication) {
        return userService.getUserGroups(authentication.getName());
    }

    @RequestMapping(value = "/user/document-types", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's document types that he can create", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Set<DocumentTypeServiceObject> getUserDocumentTypes(@ApiIgnore Authentication authentication) {
        return userService.getUserDocumentTypesHeCanCreate(authentication.getName());
    }

    @RequestMapping(value = "/criteria", method = RequestMethod.GET)
    @ApiOperation(value = "criteria", notes = "Returns users by criteria")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Page<UserServiceObject> getUserByCriteria(@RequestParam("criteria") @NotNull @Length(min = 1) String criteria,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable sortByUsername = PageRequest.of(page, size, Sort.by("username").ascending());
        try {
            return userService.getUserByCriteria(criteria, sortByUsername);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "/whoami", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "whoami", notes = "Returns user which is currently logged in")
    public UserServiceObject getCurrentUser(@ApiIgnore Authentication authentication) {
        return userService.getUserByUsername(authentication.getName());
    }

    @RequestMapping(value = "user/get-documents-to-approve", method = RequestMethod.GET)
    public Page<DocumentServiceObject> getDocumentsToApprove(@ApiIgnore Authentication authentication,
                                                             @RequestParam("page") int page,
                                                             @RequestParam("size") int size) {
        Pageable sortByTitle =
                PageRequest.of(page, size, Sort.by("title").ascending());
        try {
            return userService.getDocumentsToApprove(authentication.getName(), sortByTitle);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "user/get-documents-to-approve-filtered", method = RequestMethod.GET)
    public Page<DocumentServiceObject> getDocumentsToApproveFiltered(@ApiIgnore Authentication authentication,
                                                                     @RequestParam("page") int page,
                                                                     @RequestParam("size") int size,
                                                                     @RequestParam("criteria") String criteria) {
        Pageable sortByTitle =
                PageRequest.of(page, size, Sort.by("title").ascending());
        try {
            return userService.getDocumentsToApprove(authentication.getName(), sortByTitle, criteria);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create user", notes = "Creates new user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createNewUser(@ApiIgnore Authentication authentication,
                              @RequestBody CreateUserCommand cuc) {
        try {
            userService.createNewUser(cuc.getFirstname(), cuc.getLastname(), cuc.getUsername(),
                    cuc.getPassword(
                    ), authentication.getName());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.POST)
    @ApiOperation(value = "Update user's info", notes = "Updates user's information")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void updateUserInformation(@PathVariable("username") @NotNull @Length(min = 1) String username,
                                      @RequestParam("firstname") @NotNull @Length(min = 1) String newFirstname,
                                      @RequestParam("lastname") @NotNull @Length(min = 1) String newLastname,
                                      @ApiIgnore HttpServletRequest request) {
        boolean isAdmin = request.isUserInRole("ADMIN");
        boolean isHimself = request.getRemoteUser().equals(username);
        if (isAdmin || isHimself) {
            userService.updateUserInformation(username, newFirstname, newLastname, request.getLocalName());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to change other user's information!");
        }
    }

    @RequestMapping(value = "/{username}/password", method = RequestMethod.POST)
    @ApiOperation(value = "Update users password", notes = "Should be removed later, of locked for admins only")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void updateUserPassword(@PathVariable("username") @NotNull @Length(min = 1) String username,
                                   @RequestParam("password") @NotNull @Length(min = 1) String password,
                                   @ApiIgnore HttpServletRequest request) {

        boolean isAdmin = request.isUserInRole("ADMIN");
        boolean isHimself = request.getRemoteUser().equals(username);
        if (isAdmin || isHimself) {
            userService.updateUserPassword(username, password, request.getLocalName());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to change other user's password !");
        }
    }
}
