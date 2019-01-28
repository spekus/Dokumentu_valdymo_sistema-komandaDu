package it.akademija.users;

import it.akademija.users.dao.UserDao;
import it.akademija.users.dao.UserLoginDao;
import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserLoginDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



//Nekreipkite demesio sita klase naudojama kaip ikrovimas i duombaze ENTITY tam kad kazka tai pabandyt/istestuot


@Component
public class UserLoaderToDb implements CommandLineRunner {
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final UserLoginDao userLoginDao;

    public UserLoaderToDb(UserDao userDao, UserLoginDao userLoginDao) {
        this.userDao = userDao;
        this.userLoginDao = userLoginDao;
    }

    @Override
    public void run(String... args) throws Exception {

//        UserEntity userEntity1=new UserEntity("Jonas", "Jonaitis");
//        UserEntity userEntity2=new UserEntity("Petras", "Petraitis");
//        UserLoginDataEntity userLoginDataEntity1=new UserLoginDataEntity("jonas","jonas");
//        UserLoginDataEntity userLoginDataEntity2=new UserLoginDataEntity("petras","petraitis");
//        userLoginDataEntity1.setUser(userEntity1);
//        userLoginDataEntity2.setUser(userEntity2);
//        userDao.save(userEntity1);
//        userDao.save(userEntity2);
//        userLoginDao.save(userLoginDataEntity1);
//        userLoginDao.save(userLoginDataEntity2);

   }
}


