package it.akademija.users.service;

import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;

public class UserDetails implements UserDetailsService {
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

//    @Autowired
//    UserRepository userRepository;
//
//
//    @Override
//
//    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity user = userRepository.findUserByUsernameAndPassword(username, password);
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.setPassword());
//    }

//    @Transactional
//    public UserServiceObject getUserForLogin(String username, String password) throws UsernameNotFoundException{
//        UserEntity userEntity = userRepository.findUserByUsernameAndPassword(username, password);
//        if (userEntity != null) {
//
//            UserServiceObject userServiceObject = new UserServiceObject(userEntity.getUserIdentifier(), userEntity.getFirstname(),
//                    userEntity.getLastname(), userEntity.getUsername());
//            return userServiceObject;
//        }
//        throw new UsernameNotFoundException(username + "Not found");
//
//    }
}
