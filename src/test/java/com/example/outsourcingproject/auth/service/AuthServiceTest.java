package com.example.outsourcingproject.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.auth.dto.SigninRequest;
import com.example.outsourcingproject.auth.dto.SigninResponse;
import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.common.PasswordEncoder;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	User mockUser;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserRepository userRepository;
	@Mock
	private JwtUtil jwtUtil;
	@InjectMocks
	private AuthService authService;

	@Nested
	class Signup {
		@Test
		void goodRequest() {
			//given
			User user = new User();
			SignupRequest request = SignupRequest.builder()
				.userTel("010-1111-0000")
				.userRole("OWNER")
				.password("aaa111!!!")
				.email("test@mail.com")
				.build();
			given(userRepository.save(any())).willReturn(user);
			given(jwtUtil.createToken(any(), any(), any())).willReturn("bearer token");
			given(jwtUtil.subStringToken(anyString())).willReturn("token");

			//when
			SignupResponse response = authService.signup(request);
			
			//then
			assertEquals("token", response.getToken());
		}

		@Test
		void sameEmail() {
			//given
			SignupRequest request = new SignupRequest();
			given(userRepository.existsByEmail(any())).willReturn(true);

			//when
			ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
				() -> authService.signup(request));

			//then
			assertEquals("400 BAD_REQUEST \"중복된 이메일입니다.\"", responseStatusException.getMessage());
		}

	}

	@Nested
	class Login {
		@Test
		void successfulLogin() {
			//given
			User user = new User();
			given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
			given(passwordEncoder.matches(any(), any())).willReturn(true);
			given(jwtUtil.createToken(any(), any(), any())).willReturn("bearerToken");
			given(jwtUtil.subStringToken(any())).willReturn("token");
			SigninRequest request = new SigninRequest();

			//when
			SigninResponse response = authService.login(request);

			//then
			assertEquals("token", response.getToken());
		}

		@Test
		void noUserFound() {
			//given
			User user = new User();
			given(userRepository.findByEmail(any())).willReturn(Optional.empty());
			SigninRequest request = new SigninRequest();

			//when
			ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
				() -> authService.login(request));

			//then
			assertEquals("404 NOT_FOUND \"존재하지 않는 유저입니다.\"", responseStatusException.getMessage());
		}

		@Test
		void deletedUser() {
			//given
			User user = new User();
			user.deleteAccount(true);
			given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
			SigninRequest request = new SigninRequest();

			//when
			ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
				() -> authService.login(request));

			//then
			assertEquals("400 BAD_REQUEST \"삭제된 유저입니다.\"", responseStatusException.getMessage());
		}

		@Test
		void wrongPassword() {
			//given
			User user = new User();
			given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
			given(passwordEncoder.matches(any(), any())).willReturn(false);
			SigninRequest request = new SigninRequest();

			//when
			ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class,
				() -> authService.login(request));

			//then
			assertEquals("401 UNAUTHORIZED \"잘못된 비밀번호입니다.\"", responseStatusException.getMessage());
		}
	}
}