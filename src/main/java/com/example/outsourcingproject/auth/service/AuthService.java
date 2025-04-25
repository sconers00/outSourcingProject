package com.example.outsourcingproject.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.auth.dto.SigninRequest;
import com.example.outsourcingproject.auth.dto.SigninResponse;
import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.common.PasswordEncoder;
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
	private final JwtUtil jwtUtil;

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
			.userTel(request.getUserTel())
			.build();
		User savedUser = userRepository.save(user);
		String bearertoken = jwtUtil.createToken(savedUser.getUserId(), savedUser.getEmail(), savedUser.getUserRole());
		String token = jwtUtil.subStringToken(bearertoken);

		return SignupResponse.builder().token(token).build();
	}

	@Transactional
	public SigninResponse login(@Valid SigninRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (user.isDeleted()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		String token = jwtUtil.createToken(user.getUserId(), user.getEmail(), user.getUserRole());

		return new SigninResponse(jwtUtil.subStringToken(token));
	}
}
