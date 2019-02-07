package it.akademija.users.controller;

import io.swagger.annotations.ApiOperation;
import it.akademija.documents.service.DocumentTypeServiceObject;
import it.akademija.users.service.UserGroupServiceObject;
import it.akademija.users.service.UserService;
import it.akademija.users.service.UserServiceObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void addNewUser(@RequestBody CreateUserCommand cuc) {
        userService.addNewUser(cuc.getUserIdentifier(), cuc.getFirstname(), cuc.getLastname(), cuc.getUsername(),
                cuc.getPassword());
    }

    // berods neveikia. Ir ar kazkur naudojame username kaip path variable
    //patikrinau-lyg ir veikia, pakeiciau i identifier vietoje username
    @RequestMapping(value = "/{userIdentifier}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get info on user", notes = "")
    public UserServiceObject getUserByUserId(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        return userService.getUserByUserId(userIdentifier);
    }

    @RequestMapping(value = "/{userIdentifier}/usergroups", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's groups", notes = "")
    public List<UserGroupServiceObject> getUserGroups(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        return userService.getUserGroups(userIdentifier);
    }

    @RequestMapping(value = "/{userIdentifier}/documentTypes", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's document types that he can create", notes = "")
    public Set<DocumentTypeServiceObject> getUserDocumentTypes(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        return userService.getUserDocumentTypesHeCanCreate(userIdentifier);
    }

    // GET /api/users - returns all users
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "List all users and all related info", notes = "")
    public Collection<UserServiceObject> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /api/users/administrative should give all administrative info
    @RequestMapping(value = "/administrative", method = RequestMethod.GET)
    @ApiOperation(value = "List all users with administrative info", notes = "Should be removed later")
    public Collection<UserServiceObject> getAllUsersWithPasswords() {
        return userService.getAllUsersWithPasswords();
    }

    @RequestMapping(value = "/{userIdentifier}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete user", notes = "")
    public void deleteUser(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
        userService.deleteUserByIdentifier(userIdentifier);
    }

    @RequestMapping(value = "/{userIdentifier}/password", method = RequestMethod.PUT)
    @ApiOperation(value = "Update users password", notes = "Should be removed later, of locked for admins only")
    public void updateUserPassword(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier,
                                   @RequestParam("password") @Length(min = 1) String password) {
        userService.updateUserPassword(userIdentifier, password);
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Login user", notes = "Returns user info")
    public UserServiceObject getUser(@RequestParam("username") @Length(min = 1) String username,
                                     @RequestParam("password") @Length(min = 1) String password) {
        return userService.userLogin(username, password);

    }


    @RequestMapping(value = "/username", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "username", notes = "Returns user by username")
    public UserServiceObject getUserByUsername(@RequestParam("username") @Length(min = 1) String username) {
        return userService.getUserByUsername(username);

    }

    @RequestMapping(value = "/lastname", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "lastname", notes = "Returns user by lastname")
    public UserServiceObject getUserByLastname(@RequestParam("lastname") @Length(min = 1) String lastname) {
        return userService.getUserByLastname(lastname);

    }

    @RequestMapping(value = "/criteria", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "criteria", notes = "Returns user by criteria")
    public UserServiceObject getUserByCriteria(@RequestParam("criteria") @Length(min = 1) String criteria) {
        return userService.getUserByCriteria(criteria);

    }

}
