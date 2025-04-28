package com.example.outsourcingproject.review.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.outsourcingproject.common.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
	private final ReviewService reviewService;
	private final JwtUtil jwtUtil;
	@PostMapping("/orders/{orderId}/reviews")
	public ResponseEntity<ReviewResponseDto> createReview(
		@PathVariable Long orderId,
		@RequestBody ReviewRequestDto reviewRequestDto,
		HttpServletRequest request
	) {
		Long userId = jwtUtil.getIdFromRequest(request);
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
}