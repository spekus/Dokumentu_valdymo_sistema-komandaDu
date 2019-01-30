package it.akademija.users.controller;

import it.akademija.users.service.UserService;
import it.akademija.users.service.UserServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addNewUser(@RequestBody UserServiceObject userServiceObject) {
        userService.addNewUser(userServiceObject);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
    public UserServiceObject getUser(@RequestParam("username") String username) {
        return userService.getUserByUsername(username);
    }

    @RequestMapping(value = "/showAll", method = RequestMethod.GET)
    public Collection<UserServiceObject> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/AdminShowAll", method = RequestMethod.GET)
    public Collection<UserServiceObject> getAllUsersWithPasswords() {
        return userService.getAllUsersWithPasswords();
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUser(@RequestParam("userIdentifier") String userIdentifier) {
        userService.deleteUserByIdentifier(userIdentifier);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateUserPassword(@RequestParam("userIdentifier") String userIdentifier,
                                   @RequestParam("password") String password) {
        userService.updateUserPassword(userIdentifier, password);
    }



}
