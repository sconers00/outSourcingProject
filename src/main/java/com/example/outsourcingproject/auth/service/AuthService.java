package com.example.outsourcingproject.auth.service;

import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private
    private final UserRepository userRepository;

    public SignupResponse signup(@Valid SignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){throw new ResponseStatusException(HttpStatus.BAD_REQUEST);}

        String encodedPassword =
    }
}
