package it.akademija.users.repository;

import it.akademija.documents.repository.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findUserByUserIdentifier(String userIdentifier);
    Set<DocumentEntity> findDocumentsByUserIdentifier(String userIdentifier);
    void deleteByUserIdentifier(String userIdentifier);
    UserEntity findUserByUsername(String username);
    UserEntity findUserByUsernameAndPassword(String username, String password);


}



