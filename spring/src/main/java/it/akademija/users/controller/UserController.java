package it.akademija.users.controller;

import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserLoginDataEntity;
import it.akademija.users.service.UserService;
import it.akademija.users.service.UserServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
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


    @RequestMapping(value = "/get/login", method = RequestMethod.GET, produces = "application/json")
    public UserServiceObject getUser(@RequestParam("login") String login,
                                     @RequestParam("password") String password) {
        return userService.getUser(login, password);
    }


    @RequestMapping(value="/add", method=RequestMethod.POST)
    public void addNewUser(@RequestBody UserServiceObject userServiceObject){
        userService.addNewUser(userServiceObject);
    }





//    @RequestMapping(value="/update/{title}", method=RequestMethod.PUT)
//   public void updateBook(@PathVariable("title")String title,@RequestBody BookDto bookDto){
//        bookService.updateBookByTitle(title, bookDto);
//   }
//
//    @RequestMapping(value="/delete/{title}", method=RequestMethod.DELETE)
//   public void deleteBook(@PathVariable("title")String title){
//       bookService.deleteBookByTitle(title);
//   }


}
