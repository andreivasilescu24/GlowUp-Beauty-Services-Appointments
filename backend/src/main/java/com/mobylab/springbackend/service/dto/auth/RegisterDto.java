package com.mobylab.springbackend.service.dto.auth;

import org.springframework.stereotype.Component;

@Component
public class RegisterDto {

    private String email;
    private String username;
    private String password;
    private String role;

    public String getEmail() {
        return email;
    }

    public RegisterDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RegisterDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRole() {
        return role;
    }

    public RegisterDto setRole(String role) {
        this.role = role;
        return this;
    }
}
