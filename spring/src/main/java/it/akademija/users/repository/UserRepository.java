package it.akademija.users.repository;

import it.akademija.documents.repository.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUserIdentifier(String userIdentifier);
    List<Document> findDocumentsByUserIdentifier(String userIdentifier);

}
