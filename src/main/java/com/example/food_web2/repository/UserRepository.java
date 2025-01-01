package com.example.food_web2.repository;

import com.example.food_web2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByName(String name);
    boolean existsByEmail(String Email);

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(String role);
}
