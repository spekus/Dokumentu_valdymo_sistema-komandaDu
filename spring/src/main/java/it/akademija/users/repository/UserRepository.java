package it.akademija.users.repository;

import it.akademija.documents.repository.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findUserByUserIdentifier(String userIdentifier);


    public Set<DocumentEntity> findDocumentsByUserIdentifier(String userIdentifier);
    public void deleteByUserIdentifier(String userIdentifier);
    public UserEntity findUserByUsername(String username);
    public UserEntity findUserByUsernameAndPassword(String username, String password);


}



