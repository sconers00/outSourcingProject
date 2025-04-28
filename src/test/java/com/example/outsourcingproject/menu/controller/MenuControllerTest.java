package com.example.outsourcingproject.menu.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuDeleteResponseDto;
import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.service.MenuService;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(MenuController.class)
class MenuControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private StoreRepository storeRepository;
	@MockitoBean
	private MenuService menuService;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;
	@MockitoBean
	private HttpServletRequest httpServletRequest;

	@Test
	void addMenu() throws Exception {
		MenuRequestDto requestDto = new MenuRequestDto("testMenu", 1234L, "test");
		MenuResponseDto responseDto = MenuResponseDto.builder()
			.menuName(requestDto.getMenuName())
			.discription(requestDto.getDiscription())
			.menuId(1L)
			.menuPrice(requestDto.getMenuPrice())
			.build();
		given(menuService.save(any(), anyLong(), any())).willReturn(responseDto);
		mockMvc.perform(post("/api/stores/1/menus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isCreated())
			.andExpect(
				content().json("{\"menuId\":1,\"menuName\":\"testMenu\",\"menuPrice\":1234,\"discription\":\"test\"}"))
			.andDo(print());

		ArgumentCaptor<MenuRequestDto> captor = ArgumentCaptor.forClass(MenuRequestDto.class);
		verify(menuService).save(captor.capture(), anyLong(), any());
		MenuRequestDto capturedRequest = captor.getValue();
		assertEquals("testMenu", capturedRequest.getMenuName());
	}

	@Test
	void updateMenu() throws Exception {
		MenuRequestDto requestDto = new MenuRequestDto("testMenu", 1234L, "test");
		MenuResponseDto responseDto = MenuResponseDto.builder()
			.menuName("updateName")
			.discription("update")
			.menuId(1L)
			.menuPrice(2345L)
			.build();
		given(menuService.updateMenu(any(), anyLong(), anyLong(), any())).willReturn(responseDto);
		mockMvc.perform(patch("/api/stores/1/menus/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andExpect(
				content().json(
					"{\"menuId\":1,\"menuName\":\"updateName\",\"menuPrice\":2345,\"discription\":\"update\"}"))
			.andDo(print());

		ArgumentCaptor<MenuRequestDto> captor = ArgumentCaptor.forClass(MenuRequestDto.class);
		verify(menuService).updateMenu(captor.capture(), anyLong(), anyLong(), any());
		MenuRequestDto capturedRequest = captor.getValue();
		assertEquals("testMenu", capturedRequest.getMenuName());
	}

	@Test
	void deleteMenu() throws Exception {
		MenuDeleteResponseDto responseDto = new MenuDeleteResponseDto("메뉴가 삭제되었습니다.", 1L);
		ResponseEntity<MenuDeleteResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);
		given(menuService.delete(anyLong(), anyLong(), any())).willReturn(responseEntity);
		mockMvc.perform(delete("/api/stores/1/menus/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(
				content().json(
					"{\"message\":\"메뉴가 삭제되었습니다.\",\"menuId\":1}"))
			.andDo(print());
	}
}
