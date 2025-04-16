package com.example.loginLab.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userid;
    private String username;
    private String email;
    private String password;
    private String role;
    private String token;
    private Timestamp createdDate;

    public String getName() {
        return this.username;
    }

}
