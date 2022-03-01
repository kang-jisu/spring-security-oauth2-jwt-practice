package com.example.security.securitypractice.repository;

import com.example.security.securitypractice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 없어도 되긴함
public interface UserRepository extends JpaRepository<User, Long> {
    // select * from user where username = ?
    public User findByUsername(String username);
}
