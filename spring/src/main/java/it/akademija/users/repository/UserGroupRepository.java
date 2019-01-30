package it.akademija.users.repository;
import it.akademija.documents.repository.DocumentEntity;
import it.akademija.users.service.UserGroupServiceObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long>{

    public UserGroupEntity findGroupByTitle(String title);

    public List<UserGroupEntity> findAll();

    public void deleteGroupByTitle(String title);
}
