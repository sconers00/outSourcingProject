package com.example.outsourcingproject.store.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.outsourcingproject.auth.service.AuthService;
import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.order.dto.ChangeOrderState;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.service.OrderService;
import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.store.dto.responseDto.GetStoreResponseDto;
import com.example.outsourcingproject.store.dto.responseDto.StoreResponseDto;
import com.example.outsourcingproject.store.service.StoreService;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthService authService;
	@MockitoBean
	private OrderService orderService;
	@MockitoBean
	private StoreService storeService;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;
	@MockitoBean
	private HttpServletRequest httpServletRequest;

	@Test
	void registerStore() throws Exception {
		StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
			1234L);
		StoreResponseDto responseDto = StoreResponseDto.builder()
			.id(1L)
			.address(requestDto.getAddress())
			.closeTime(requestDto.getCloseTime())
			.minOrderPrice(requestDto.getMinOrderPrice())
			.openTime(requestDto.getOpenTime())
			.storeName(requestDto.getStoreName())
			.storeTelNumber(requestDto.getStoreTelNumber())
			.build();
		given(storeService.registerStore(any(), any())).willReturn(responseDto);
		mockMvc.perform(post("/api/users/stores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isCreated())
			.andExpect(content().json(
				"{\"id\":1,\"storeName\":\"testStore\",\"address\":\"testAddr\",\"storeTelNumber\":\"123-4567-8900\",\"openTime\":\"12:34\",\"closeTime\":\"12:34\",\"minOrderPrice\":1234,\"menuList\":null}"))
			.andDo(print());

		ArgumentCaptor<StoreRequestDto> captor = ArgumentCaptor.forClass(StoreRequestDto.class);
		verify(storeService).registerStore(captor.capture(), any());
		StoreRequestDto capturedRequest = captor.getValue();
		assertEquals("testStore", capturedRequest.getStoreName());
		assertEquals("testAddr", capturedRequest.getAddress());
		assertEquals("123-4567-8900", capturedRequest.getStoreTelNumber());
		assertEquals("12:34", capturedRequest.getOpenTime());
		assertEquals("12:34", capturedRequest.getCloseTime());
		assertEquals(1234L, capturedRequest.getMinOrderPrice());

	}

	@Test
	void updateStore() throws Exception {
		StoreRequestDto requestDto = new StoreRequestDto("testStore", "testAddr", "123-4567-8900", "12:34", "12:34",
			1234L);
		StoreResponseDto responseDto = StoreResponseDto.builder()
			.id(1L)
			.address("changedAddr")
			.closeTime("12:12")
			.minOrderPrice(2345L)
			.openTime("12:12")
			.storeName("changedStore")
			.storeTelNumber("098-7654-3211")
			.build();
		given(storeService.updateById(anyLong(), any(), any())).willReturn(responseDto);
		mockMvc.perform(patch("/api/users/stores/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{\"id\":1,\"storeName\":\"changedStore\",\"address\":\"changedAddr\",\"storeTelNumber\":\"098-7654-3211\",\"openTime\":\"12:12\",\"closeTime\":\"12:12\",\"minOrderPrice\":2345,\"menuList\":null}"))
			.andDo(print());

		ArgumentCaptor<StoreRequestDto> captor = ArgumentCaptor.forClass(StoreRequestDto.class);
		verify(storeService).updateById(anyLong(), captor.capture(), any());
		StoreRequestDto capturedRequest = captor.getValue();
		assertEquals("testStore", capturedRequest.getStoreName());
		assertEquals("testAddr", capturedRequest.getAddress());
		assertEquals("123-4567-8900", capturedRequest.getStoreTelNumber());
		assertEquals("12:34", capturedRequest.getOpenTime());
		assertEquals("12:34", capturedRequest.getCloseTime());
		assertEquals(1234L, capturedRequest.getMinOrderPrice());
	}

	@Test
	void findById() throws Exception {
		GetStoreResponseDto responseDto = GetStoreResponseDto.builder()
			.id(1L)
			.address("changedAddr")
			.closeTime("12:12")
			.minOrderPrice(2345L)
			.openTime("12:12")
			.storeName("changedStore")
			.storeTelNumber("098-7654-3211")
			.build();
		given(storeService.findById(anyLong())).willReturn(responseDto);
		mockMvc.perform(get("/api/users/stores/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{\"id\":1,\"storeName\":\"changedStore\",\"address\":\"changedAddr\",\"storeTelNumber\":\"098-7654-3211\",\"openTime\":\"12:12\",\"closeTime\":\"12:12\",\"minOrderPrice\":2345,\"menuList\":null}"))
			.andDo(print());
	}

	@Test
	void findAll() throws Exception {
		List<StoreResponseDto> list = new ArrayList<>();
		StoreResponseDto responseDto = StoreResponseDto.builder()
			.id(1L)
			.address("changedAddr")
			.closeTime("12:12")
			.minOrderPrice(2345L)
			.openTime("12:12")
			.storeName("changedStore")
			.storeTelNumber("098-7654-3211")
			.build();
		list.add(responseDto);
		given(storeService.findAll()).willReturn(list);
		mockMvc.perform(get("/api/users/stores")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"[{\"id\":1,\"storeName\":\"changedStore\",\"address\":\"changedAddr\",\"storeTelNumber\":\"098-7654-3211\",\"openTime\":\"12:12\",\"closeTime\":\"12:12\",\"minOrderPrice\":2345,\"menuList\":null}]"))
			.andDo(print());
	}

	@Test
	void deleteStore() throws Exception {
		mockMvc.perform(delete("/api/users/stores/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void changeOrderState() throws Exception {
		ChangeOrderState changeOrderState = ChangeOrderState.builder()
			.orderState("ARRIVED")
			.build();
		OrderResponse responseDto = OrderResponse.builder()
			.orderStatus("ARRIVED")
			.build();
		given(orderService.changeOrderState(any(), any(), anyLong(), anyLong())).willReturn(responseDto);
		mockMvc.perform(patch("/api/users/stores/1/orders/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(changeOrderState)))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"{\"orderId\":null,\"userId\":null,\"storeId\":null,\"menuId\":null,\"quantity\":null,\"address\":null,\"orderStatus\":\"ARRIVED\",\"orderedAt\":null}"))
			.andDo(print());
	}

	@Test
	void searchdOrder() throws Exception {
		List<SearchOrderResponse> list = new ArrayList<>();
		SearchOrderResponse response = SearchOrderResponse.builder()
			.quantity(1L)
			.address("testAddr")
			.storeId(1L)
			.menuId(1L)
			.orderStatus("ARRIVED")
			.orderId(1L)
			.build();
		list.add(response);
		given(orderService.findOrderByStore(any(), anyLong(), anyString(), anyInt())).willReturn(list);
		mockMvc.perform(get("/api/users/stores/1/orders")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(
				"[{\"orderId\":1,\"storeId\":1,\"menuId\":1,\"quantity\":1,\"address\":\"testAddr\",\"orderStatus\":\"ARRIVED\",\"orderedAt\":null}]"))
			.andDo(print());
	}
}