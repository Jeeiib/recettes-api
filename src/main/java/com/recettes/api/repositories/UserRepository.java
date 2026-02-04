package com.recettes.api.repositories;

import com.recettes.api.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMail(String mail);

    boolean existsByMail(String mail);
}
