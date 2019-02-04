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
    @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get info on user", notes = "")
    public UserServiceObject getUser(@PathVariable("username") @Length(min = 1) String username) {
        return userService.getUserByUsername(username);
    }

    @RequestMapping(value = "/{userIdentifier}/usergroups", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get user's groups", notes = "")
    public Set<UserGroupServiceObject> getUserGroups(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier) {
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


    @RequestMapping(value = "/{userIdentifier}/login", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Login user", notes = "Returns user info")
    public UserServiceObject getUser(@PathVariable("username") @Length(min = 1) String username,
                                     @RequestParam("password") @Length(min = 1) String password) {
        return userService.getUserForLogin(username, password);

    }



    @RequestMapping(value = "/{userIdentifier}/group", method = RequestMethod.PUT)
    @ApiOperation(value = "Add group to user", notes = "")
    public void addGroupToUser(@PathVariable("userIdentifier") @Length(min = 1) String userIdentifier,
                               @RequestParam("title") @Length(min = 1) String title) {

        userService.addGroupToUser(userIdentifier, title);
    }

}
