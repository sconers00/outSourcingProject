package com.example.outsourcingproject.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.review.dto.ReviewRequestDto;
import com.example.outsourcingproject.review.dto.ReviewResponseDto;
import com.example.outsourcingproject.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping("/orders/{orderId}/reviews")
	public ResponseEntity<ReviewResponseDto> createReview(
		@PathVariable Long orderId,
		@RequestBody ReviewRequestDto reviewRequestDto
		//@AuthenticationPrincipal UserDetails userDetails
	) {
		//Long userId = extractUserId(userDetails);
		Long userId = 1L;
		ReviewResponseDto responseDto = reviewService.createReview(orderId, reviewRequestDto, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	@GetMapping("/stores/{storeId}/reviews")
	public ResponseEntity<Page<ReviewResponseDto>> getStoreReviews(
		@PathVariable Long storeId,
		@RequestParam(required = false) Integer minRating,
		@RequestParam(required = false) Integer maxRating,
		Pageable pageable
	) {
		Page<ReviewResponseDto> reviews = reviewService.getStoreReviews(storeId, minRating, maxRating, pageable);
		return ResponseEntity.ok(reviews);
	}

	/**
	 * UserDetails에서 사용자 ID 추출
	 * 프로젝트의 UserDetails 구현체에 따라 변경 필요
	 */
	private Long extractUserId(UserDetails userDetails) {
		// 실제 구현에 맞게 캐스팅 및 추출 //
		// 예: return ((CustomUserDetails) userDetails).getUserId();
		return 1L; // TODO: 실제 구현체로 교체 필요
	}
}
