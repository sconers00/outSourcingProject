package com.example.outsourcingproject.auth.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.outsourcingproject.auth.dto.SigninRequest;
import com.example.outsourcingproject.auth.dto.SigninResponse;
import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.auth.service.AuthService;
import com.example.outsourcingproject.common.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthService authService;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;
	@MockitoBean
	private HttpServletRequest httpServletRequest;

	@Test
	void signup() throws Exception {
		//given
		SignupRequest signupRequest = SignupRequest.builder()
			.email("test@mail.com")
			.password("aaa111!!!")
			.userRole("OWNER")
			.userTel("010-1111-0000")
			.build();
		SignupResponse signupResponse = SignupResponse.builder().token("someToken").build();
		given(authService.signup(any(SignupRequest.class))).willReturn(signupResponse);
		//when & then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(signupRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value(equalTo(signupResponse.getToken())))
			.andDo(print());
	}

	@Test
	void login() throws Exception {
		//given
		SigninRequest signinRequest = SigninRequest.builder()
			.email("test@mail.com")
			.password("aaa111!!!")
			.build();
		SigninResponse signinResponse = SigninResponse.builder().token("someToken").build();
		given(authService.login(any(SigninRequest.class))).willReturn(signinResponse);
		//when & then
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(signinRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value(equalTo(signinResponse.getToken())))
			.andDo(print());
	}

	@Test
	void logout() throws Exception {
		//given
		Cookie[] cookies = {new Cookie("token", "testToken")};
		given(httpServletRequest.getCookies()).willReturn(cookies);
		//when & then
		mockMvc.perform(delete("/api/auth/logout")
				.cookie(new Cookie("token", "testToken"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString("로그아웃 되었습니다.")))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Nested
	class logout {
		@Test
		@DisplayName("정상적인 토큰")
		void goodToken() throws Exception {
			//given
			Cookie[] cookies = {new Cookie("token", "testToken")};
			given(httpServletRequest.getCookies()).willReturn(cookies);
			//when & then
			mockMvc.perform(delete("/api/auth/logout")
					.cookie(new Cookie("token", "testToken"))
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString("로그아웃 되었습니다.")))
				.andExpect(status().isOk())
				.andDo(print());
		}

		@Test
		@DisplayName("빈 토큰")
		void badToken() throws Exception {
			//given & when & then
			mockMvc.perform(delete("/api/auth/logout"))
				.andExpect(status().isNotFound())
				.andDo(print());
		}
	}

}