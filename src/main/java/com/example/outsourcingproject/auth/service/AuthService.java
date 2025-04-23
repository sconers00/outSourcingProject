package com.example.outsourcingproject.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.common.JwtUtill;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.enums.UserRole;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtUtill jwtUtill;

	public SignupResponse signup(@Valid SignupRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());
		UserRole userRole = UserRole.of(request.getUserRole());

		User user = User.builder()
			.email(request.getEmail())
			.password(encodedPassword)
			.userRole(userRole)
			.build();
		User savedUser = userRepository.save(user);
		String token = jwtUtill.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());

		return SignupResponse.builder().token(token).build();
	}

}
