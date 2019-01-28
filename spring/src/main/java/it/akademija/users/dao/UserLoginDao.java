package it.akademija.users.dao;

import it.akademija.users.repository.UserEntity;
import it.akademija.users.repository.UserLoginDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginDao extends JpaRepository<UserLoginDataEntity, Long> {

    public UserLoginDataEntity findUserByLogin(String login);
}
