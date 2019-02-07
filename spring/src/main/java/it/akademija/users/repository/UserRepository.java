package it.akademija.users.repository;

import it.akademija.documents.repository.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findUserByUserIdentifier(String userIdentifier);


    public Set<DocumentEntity> findDocumentsByUserIdentifier(String userIdentifier);

    public void deleteByUserIdentifier(String userIdentifier);

    public UserEntity findUserByUsername(String username);

    public UserEntity findUserByLastname(String lastname);

    public UserEntity findUserByUsernameAndPassword(String username, String password);

    public UserEntity findByUsername(String username);

    @Query("select u from UserEntity u where u.firstname=:criteria OR u.lastname=:criteria OR u.userIdentifier=:criteria")
    public List<UserEntity> findByUsernameOrLastnameOrId(@Param("criteria")String criteria);
}




