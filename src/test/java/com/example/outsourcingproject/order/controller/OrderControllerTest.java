package com.example.outsourcingproject.order.controller;

import static org.mockito.ArgumentMatchers.*;
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

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.order.dto.OrderRequestDto;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OrderService orderService;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;
	@MockitoBean
	private HttpServletRequest httpServletRequest;

	@Test
	void createOrder() throws Exception {
		//given
		OrderRequestDto request = OrderRequestDto.builder()
			.address("testAddr")
			.menuId(1L)
			.quantity(1L)
			.storeId(1L)
			.build();
		OrderResponse response = OrderResponse.builder()
			.address(request.getAddress())
			.menuId(request.getMenuId())
			.quantity(request.getQuantity())
			.storeId(request.getStoreId())
			.orderId(1L)
			.build();
		given(orderService.createOrder(any(), any())).willReturn(response);

		//when & then
		mockMvc.perform(post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{\"orderId\":1,\"userId\":null,\"storeId\":1,\"menuId\":1,\"quantity\":1,\"address\":\"testAddr\",\"orderStatus\":null,\"orderedAt\":null}"))
			.andDo(print());
	}

	@Test
	void cancelOrder() throws Exception {
		//given
		OrderRequestDto request = OrderRequestDto.builder()
			.address("testAddr")
			.menuId(1L)
			.quantity(1L)
			.storeId(1L)
			.build();
		OrderResponse response = OrderResponse.builder()
			.address(request.getAddress())
			.menuId(request.getMenuId())
			.userId(1L)
			.quantity(request.getQuantity())
			.storeId(request.getStoreId())
			.orderStatus("CANCELED")
			.orderId(1L)
			.build();
		Cookie[] cookies = {new Cookie("token", "testToken")};
		given(orderService.cancelOrder(any(), anyLong())).willReturn(response);

		//when & then
		mockMvc.perform(patch("/api/orders/1")
				.cookie(new Cookie("token", "testToken"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{\"orderId\":1,\"userId\":1,\"storeId\":1,\"menuId\":1,\"quantity\":1,\"address\":\"testAddr\",\"orderStatus\":\"CANCELED\",\"orderedAt\":null}"))
			.andDo(print());
	}
}