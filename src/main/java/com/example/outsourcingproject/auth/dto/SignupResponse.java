package com.example.outsourcingproject.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupResponse {
    private final String token;

    @Builder
    public SignupResponse(String token) {
        this.token = token;
    }
}
