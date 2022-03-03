package com.example.security.securitypractice.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN ,로 구분해서 여러개 들어감

    private String provider;
    private String providerId;
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public User( String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }

    public User() {

    }

    public List<String> getRoles(){
        if(role.length()>0) {
            return Arrays.asList(role.split(","));
        }
        return new ArrayList<>();
    }
}
