package it.akademija.users.controller;

import it.akademija.users.service.UserService;
import it.akademija.users.service.UserServiceObject;
import org.h2.command.ddl.CreateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void addNewUser(@RequestBody CreateUserCommand cuc) {
        userService.addNewUser(cuc.getUserIdentifier(), cuc.getFirstname(), cuc.getLastname(), cuc.getUsername(),
                cuc.getPassword());
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



    @RequestMapping(value = "/get/login", method = RequestMethod.GET, produces = "application/json")
    public UserServiceObject getUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password) {
        return userService.getUserForLogin(username, password);

    }


    @RequestMapping(value = "/addGroup", method = RequestMethod.PUT)
    public void addGroupToUser(@RequestParam("userIdentifier") String userIdentifier,
                               @RequestParam("title") String title) {
        userService.addGroupToUser(userIdentifier, title);
    }

// destytojo
//    @Component("restAuthenticationEntryPoint")
////    Spring secuity,needed to generated 401
//    public class SecurityEntryPoint implements AuthenticationEntryPoint {
//        @Override
//        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//                throws IOException, ServletException {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cia tau negalima");
//        }
//    }

}
