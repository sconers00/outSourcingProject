package com.example.outsourcingproject.review.dto;

import java.time.LocalDateTime;

import com.example.outsourcingproject.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

	private Long id;

	private Long orderId;

	private Long storeId;

	private String menuName;

	private int rating;

	private String content;

	private LocalDateTime createdAt;

	public static ReviewResponseDto from(Review review, String menuName) {
		return ReviewResponseDto.builder()
			.id(review.getId())
			.orderId(review.getOrderId())
			.storeId(review.getStoreId())
			.menuName(menuName)
			.rating(review.getRating())
			.content(review.getContent())
			.createdAt(review.getCreatedAt())
			.build();
	}
}
