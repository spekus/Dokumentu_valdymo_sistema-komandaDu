package it.akademija.users.repository;

import it.akademija.documents.DocumentState;
import it.akademija.statistics.repository.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("select u from UserEntity u where u.firstname=:criteria OR u.lastname=:criteria OR u.username=:criteria")
    List<UserEntity> findByUsernameOrLastname(@Param("criteria") String criteria);

    void deleteUserByUsername(String username);

}


