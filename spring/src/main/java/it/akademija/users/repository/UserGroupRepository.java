package it.akademija.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;


public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {

    UserGroupEntity findGroupByTitle(String title);

    void deleteGroupByTitle(String title);


}
