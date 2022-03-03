package com.example.security.securitypractice.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN

    @Column
    @Enumerated(EnumType.STRING)
    private ProviderType provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;

    private LocalDateTime lastLoginTime;

    @Builder
    public User(String username, String password, String email, String role, ProviderType provider, String providerId, Timestamp createDate, LocalDateTime lastLoginTime) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
        this.lastLoginTime = lastLoginTime;
    }

    public User() {

    }
}
