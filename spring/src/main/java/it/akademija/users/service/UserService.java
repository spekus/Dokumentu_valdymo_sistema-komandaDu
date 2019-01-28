package it.akademija.users.service;

import it.akademija.users.dao.UserDao;
import it.akademija.users.dao.UserLoginDao;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserLoginDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserLoginDao userLoginDao;

    public UserService(UserDao userDao, UserLoginDao userLoginDao) {
        this.userDao = userDao;
        this.userLoginDao = userLoginDao;
    }

    @Transactional
    public UserServiceObject getUser(String login, String password) {
        UserEntity userEntity = userDao.findUserByLogin(login, password);
        if (userEntity!= null) {
            UserServiceObject userServiceObject=new UserServiceObject(userEntity.getName(),userEntity.getSurname());
            userServiceObject.setUserGroups(userEntity.getUserGroups());
            return userServiceObject;
        }
        return null;
    }

    @Transactional
    public void addNewUser(UserServiceObject userServiceObject) {
        UserEntity userEntity=new UserEntity(userServiceObject.getName(),userServiceObject.getSurname());
        userEntity.setUserGroups(userServiceObject.getUserGroups());
        userDao.save(userEntity);
    }
}


