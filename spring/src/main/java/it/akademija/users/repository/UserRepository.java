package it.akademija.users.repository;

import it.akademija.documents.DocumentState;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.statistics.repository.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findUserByUsername(String username);

    UserEntity findUserByLastname(String lastname);

    UserEntity findUserByUsernameAndPassword(String username, String password);


    UserEntity findByUsername(String username);

    @Query("select u from UserEntity u where lower(u.firstname)=:criteria OR lower(u.lastname)=:criteria OR lower(u.username)=:criteria")
    List<UserEntity> findByUsernameOrLastname(@Param("criteria") String criteria);

    void deleteUserByUsername(String username);
//    @Modifying
//    @Query(value ="insert into DOCUMENT_TYPE_ENTITY (TITLE) VALUES (:TITLE)", nativeQuery = true)
//    void putDummyDocumentTypes(@Param("TITLE") String title);
    @Modifying
    @Query(value= "insert into USER_ENTITY (FIRSTNAME, LASTNAME, PASSWORD, USERNAME) VALUES (:NAME, :SURENAME, :userInput, :userInput)", nativeQuery = true)
//    @Query(value = "select id,name,roll_no from USER_INFO_TEST where rollNo = ?1", nativeQuery = true)
    void makeDummyUsers(@Param("userInput")String userInput, @Param("NAME")String name, @Param("SURENAME")String surename);


}


