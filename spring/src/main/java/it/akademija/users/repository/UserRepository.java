package it.akademija.users.repository;

import it.akademija.documents.repository.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public List<DocumentEntity> findDocumentsByUserIdentifier(String userIdentifier);

    public UserEntity findUserByUserIdentifier(String userIdentifier);

    public UserEntity findUserByUsername(String username);

    public List<UserEntity> findAll();

    public void deleteByUserIdentifier(String userIdentifier);

    public UserEntity findUserByUsernameAndPassword(String username, String password);

}



