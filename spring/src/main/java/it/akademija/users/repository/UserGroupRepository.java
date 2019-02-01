package it.akademija.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {

    UserGroupEntity findGroupByTitle(String title);

    void deleteGroupByTitle(String title);


    public List<UserGroupEntity> findAll();
}
