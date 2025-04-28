package com.example.outsourcingproject.auth.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.auth.dto.SigninRequest;
import com.example.outsourcingproject.auth.dto.SigninResponse;
import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.auth.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
		SignupResponse response = authService.signup(request);
		String token = response.getToken();

		ResponseCookie cookie = ResponseCookie.from("token", token)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(Duration.ofDays(1))
			.sameSite("Strict")
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok()
			.headers(headers)
			.body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<SigninResponse> login(@Valid @RequestBody SigninRequest request) {
		SigninResponse response = authService.login(request);
		String token = response.getToken();

		ResponseCookie cookie = ResponseCookie.from("token", token)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(Duration.ofDays(1))
			.sameSite("Strict")
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok()
			.headers(headers)
			.body(response);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		if (request.getCookies() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "쿠키가 존재하지 않습니다.");
		}
		ResponseCookie cookie = ResponseCookie.from("token")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("Strict")
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok()
			.headers(headers)
			.body("로그아웃 되었습니다.");
	}
}
