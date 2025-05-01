package com.example.loginLab.demo.repository;

import com.example.loginLab.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findUserByUserid(Long userid);

    Optional<User> findUserByUsername(String username);


    Optional<User> findUserByEmail(String email);


    @Query(value = "SELECT ap_user_seq.NEXTVAL FROM dual", nativeQuery = true)
    Long getNextUserId();
}
