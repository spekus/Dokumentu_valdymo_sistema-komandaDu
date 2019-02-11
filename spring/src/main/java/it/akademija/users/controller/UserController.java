package it.akademija.users.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.akademija.auth.AppRoleEnum;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.service.UserGroupServiceObject;
import it.akademija.users.service.UserService;
import it.akademija.users.service.UserServiceObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
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


    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "Create user", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addNewUser(@RequestBody CreateUserCommand cuc) {
        userService.addNewUser(cuc.getUserIdentifier(), cuc.getFirstname(), cuc.getLastname(), cuc.getUsername(),
                cuc.getPassword());
    }

    // berods neveikia. Ir ar kazkur naudojame username kaip path variable
    //patikrinau-lyg ir veikia, pakeiciau i identifier vietoje username
    @RequestMapping(value = "/{userIdentifier}", method = RequestMethod.GET, produces = "application/json")
    //@ApiOperation(value = "Get info on user", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public UserServiceObject getUserByUserId(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        return userService.getUserByUserId(userIdentifier);
    }

    @RequestMapping(value = "/{userIdentifier}/usergroups", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's groups", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public List<UserGroupServiceObject> getUserGroups(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        return userService.getUserGroups(userIdentifier);
    }

    @RequestMapping(value = "/{userIdentifier}/documentTypes", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's document types that he can create", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Set<DocumentTypeServiceObject> getUserDocumentTypes(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        return userService.getUserDocumentTypesHeCanCreate(userIdentifier);
    }

    // GET /api/users - returns all users
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "List all users and all related info", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Collection<UserServiceObject> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/{userIdentifier}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete user", notes = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier,
                           @ApiIgnore HttpServletRequest request) {
        // neleidziam naudotojui istrinti pati save
        // kaip nustatyti kitose koks naudotojo vardas kitose vietose, pravers sitas puslapis:
        // https://www.baeldung.com/get-user-in-spring-security
        // Galima naudoti "HttpServletRequest request" arba "Authentication authentication"
        if (request.getRemoteUser().equals(userIdentifier)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete yourself!");
            // parodome exception, kuri galima pasiimti axios ... catch (response => ... response.data.message)
        } else {
            userService.deleteUserByIdentifier(userIdentifier);
        }
    }

    @RequestMapping(value = "/{userIdentifier}/password", method = RequestMethod.PUT)
    @ApiOperation(value = "Update users password", notes = "Should be removed later, of locked for admins only")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public void updateUserPassword(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier,
                                   @RequestParam("password") @Length(min = 1) String password,
                                   @ApiIgnore HttpServletRequest request) {

        // leidziam keisti passworda jeigu adminas arba jeigu naudotajas yra tas pats kaip prisijunges
        // kaip nustatyti, ar turi ADMIN role, mes suzinome is cia:
        // https://www.baeldung.com/spring-security-expressions-basic
        // 4 punktas
        boolean isAdmin = request.isUserInRole("ADMIN");
        boolean isHimself = request.getRemoteUser().equals(userIdentifier);
        if (isAdmin || isHimself) {
            userService.updateUserPassword(userIdentifier, password);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to change other user's password !");
        }
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "username", notes = "Returns user by username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public UserServiceObject getUserByUsername(@RequestParam("username") @Length(min = 1) String username) {
        return userService.getUserByUsername(username);

    }

    @RequestMapping(value = "/criteria", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "criteria", notes = "Returns users by criteria")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public Collection<UserServiceObject> getUserByCriteria(@RequestParam("criteria") @Length(min = 1) String criteria) {
        return userService.getUserByCriteria(criteria);

    }

    @RequestMapping(value = "/whoami", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "whoami", notes = "Returns user which is currently logged in")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public UserServiceObject getCurrentUser(@ApiIgnore Authentication authentication) {
        return userService.getUserByUserId(authentication.getName());

    }


}
