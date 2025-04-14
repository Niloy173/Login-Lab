package com.example.loginLab.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(generator="ap_user_seq")
    @SequenceGenerator(name="ap_user_seq",
            sequenceName="ap_user_seq", allocationSize=1)
    @Column(name = "user_id")
    private Long userid;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = Timestamp.from(Instant.now());
        }
    }

}
