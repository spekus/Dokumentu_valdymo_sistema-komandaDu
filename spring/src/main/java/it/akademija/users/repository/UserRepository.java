package it.akademija.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findUserByUsername(String username);

    UserEntity findUserByLastname(String lastname);

    UserEntity findUserByUsernameAndPassword(String username, String password);

    UserEntity findByUsernameIgnoreCase(String username);

    UserEntity findByUsername(String username);

    @Query("select u from UserEntity u where lower(u.firstname)=:criteria OR lower(u.lastname)=:criteria " +
            "OR lower(u.username)=:criteria")
    List<UserEntity> findByUsernameOrLastname(@Param("criteria") String criteria, Pageable pageable);

    @Query("select count(id) from UserEntity u where lower(u.firstname)=:criteria OR lower(u.lastname)=:criteria " +
            "OR lower(u.username)=:criteria")
    long getUsersByCriteriaSize(@Param("criteria") String criteria);

    @Query("select u from UserEntity u")
    List<UserEntity> getAllUsers(Pageable pageAble);

    @Query("select count(ue.id) From UserEntity ue")
    long getTotalUsersCount();

    void deleteUserByUsername(String username);

    @Modifying
    @Query(value = "insert into USER_ENTITY (FIRSTNAME, LASTNAME, PASSWORD, USERNAME) VALUES (:NAME, :SURENAME, " +
            ":userInput, :userInput)", nativeQuery = true)
    void makeDummyUsers(@Param("userInput") String userInput, @Param("NAME") String name, @Param("SURENAME") String surename);
}


