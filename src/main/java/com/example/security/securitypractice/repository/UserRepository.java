package com.example.security.securitypractice.repository;

import com.example.security.securitypractice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// @Repository 없어도 되긴함
public interface UserRepository extends JpaRepository<User, Long> {
    // select * from user where username = ?
    public User findByUsername(String username);

    Optional<User> findByEmail(String email);
}
