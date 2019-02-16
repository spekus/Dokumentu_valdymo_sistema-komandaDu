package it.akademija.users.repository;


import it.akademija.auth.AppRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {

    UserGroupEntity findGroupByTitle(String title);

    UserGroupEntity findGroupByRole(AppRoleEnum role);

    void deleteGroupByTitle(String title);

    List<UserGroupEntity> findAll();


}
