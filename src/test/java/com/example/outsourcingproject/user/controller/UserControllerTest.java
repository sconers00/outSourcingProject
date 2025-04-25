package com.example.outsourcingproject.user.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.example.outsourcingproject.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockitoBean
	UserService userService;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;
	@MockitoBean
	private HttpServletRequest httpServletRequest;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void deleteAccount() throws Exception {

		mockMvc.perform(patch("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString("탈퇴 되었습니다.")))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void searchRequestedOrder() throws Exception {
		List<SearchOrderResponse> list = new ArrayList<>();
		SearchOrderResponse searchOrderResponse = SearchOrderResponse.builder()
			.address("somewhere")
			.menuId(1L)
			.storeId(1L)
			.quantity(1L)
			.build();
		list.add(searchOrderResponse);
		given(userService.searchRequestedOrder(any(), anyInt())).willReturn(list);
		mockMvc.perform(get("/api/users/me/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(list)))
			.andExpect(status().isOk())
			.andDo(print());
	}
}