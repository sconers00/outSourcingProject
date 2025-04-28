package com.example.outsourcingproject.review.controller;

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
import com.example.outsourcingproject.review.dto.ReviewRequestDto;
import com.example.outsourcingproject.review.dto.ReviewResponseDto;
import com.example.outsourcingproject.review.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ReviewService reviewService;
	@MockitoBean
	private JwtUtil jwtUtil;
	@MockitoBean
	private JpaMetamodelMappingContext jpaMetamodelMappingContext;
	@MockitoBean
	private HttpServletRequest httpServletRequest;

	@Test
	void createReview() throws Exception {
		ReviewRequestDto requestDto = ReviewRequestDto.builder()
			.content("test")
			.menuId(1L)
			.rating(1)
			.storeId(1L)
			.build();
		ReviewResponseDto responseDto = ReviewResponseDto.builder()
			.storeId(1L)
			.content(requestDto.getContent())
			.rating(requestDto.getRating())
			.storeId(requestDto.getStoreId())
			.menuName("testMenu")
			.build();
		given(jwtUtil.getIdFromRequest(any())).willReturn(1L);
		given(reviewService.createReview(anyLong(), any(), anyLong())).willReturn(responseDto);
		mockMvc.perform(post("/api/orders/1/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isCreated())
			.andExpect(content().json(
				"{\"id\":null,\"orderId\":null,\"storeId\":1,\"menuName\":\"testMenu\",\"rating\":1,\"content\":\"test\",\"createdAt\":null}"))
			.andDo(print());
	}
}