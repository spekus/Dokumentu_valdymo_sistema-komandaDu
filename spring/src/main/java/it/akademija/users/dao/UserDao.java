package it.akademija.users.dao;

import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserLoginDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<UserEntity, Long> {

    @Query("select d.user from UserLoginDataEntity d where d.login=:login and d.password=:password")
    public UserEntity findUserByLogin(@Param("login")String login, @Param("password")String password);
}


