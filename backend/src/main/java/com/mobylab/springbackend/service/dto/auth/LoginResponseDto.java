package com.mobylab.springbackend.service.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginResponseDto {

    @JsonProperty("access_token")
    private String token;
    @JsonProperty("token_type")
    private String type = "Bearer";
    @JsonProperty("expires_in")
    @Value("${token.ttl}")
    private long expire;
    @JsonProperty("role")
    private String role;

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public long getExpire() {
        return expire;
    }

    public String getRole() {
        return role;
    }

    public LoginResponseDto setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponseDto setType(String type) {
        this.type = type;
        return this;
    }

    public LoginResponseDto setExpire(long expire) {
        this.expire = expire;
        return this;
    }

    public LoginResponseDto setRole(String role) {
        this.role = role;
        return this;
    }
}
