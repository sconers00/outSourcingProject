package com.example.outsourcingproject.auth.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.outsourcingproject.auth.dto.SignupRequest;
import com.example.outsourcingproject.auth.dto.SignupResponse;
import com.example.outsourcingproject.auth.service.AuthService;
import com.example.outsourcingproject.common.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	AuthService authService;
	@MockitoBean
	JwtUtil jwtUtil;
	@MockitoBean
	JpaMetamodelMappingContext jpaMetamodelMappingContext;

	@Test
	void signup() throws Exception {
		SignupRequest signupRequest = SignupRequest.builder()
			.email("test@mail.com")
			.password("aaa111!!!")
			.userRole("OWNER")
			.userTel("010-1111-0000")
			.build();
		SignupResponse signupResponse = SignupResponse.builder().token("someToken").build();
		given(authService.signup(any(SignupRequest.class))).willReturn(signupResponse);

		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(signupRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value(equalTo(signupResponse.getToken())))
			.andDo(print());
	}

	@Test
	void login() {
	}

	@Test
	void logout() {
	}
}