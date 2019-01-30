package it.akademija.users.controller;


import it.akademija.users.service.UserGroupService;
import it.akademija.users.service.UserGroupServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/userGroups")
public class UserGroupController {
    @Autowired
    private UserGroupService userGroupService;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @RequestMapping(value = "/addNewGroup", method = RequestMethod.POST)
    public void addNewUserGroup(@RequestBody UserGroupServiceObject userGroupServiceObject) {
        userGroupService.addNewUserGroup(userGroupServiceObject);
    }

    @RequestMapping(value = "/getGroup", method = RequestMethod.GET, produces = "application/json")
    public UserGroupServiceObject getUser(@RequestParam("title") String title) {
        return userGroupService.getGroupByTitle(title);
    }

    @RequestMapping(value = "/showAllGroups", method = RequestMethod.GET)
    public Collection<UserGroupServiceObject> getAllGroups() {
        return userGroupService.getAllGroups();
    }


    @RequestMapping(value = "/deleteGroup", method = RequestMethod.DELETE)
    public void deleteGroup(@RequestParam("title") String title) {
        userGroupService.deleteGroupByTitle(title);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateUserPassword(@RequestParam("title") String title) {
        userGroupService.updateGroupByTitle(title);
    }





    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }
}
