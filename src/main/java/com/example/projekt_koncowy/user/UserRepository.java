package com.example.projekt_koncowy.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository <User,Integer> {

    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Boolean existsByLogin(String login);
    Boolean existsByEmail(String email);
}
