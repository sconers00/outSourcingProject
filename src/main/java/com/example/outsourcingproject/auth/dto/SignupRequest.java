package com.example.outsourcingproject.auth.dto;

import com.example.outsourcingproject.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^[0-9A-za-z]$")
    private String password;
    @NotBlank
    private UserRole role;
}
