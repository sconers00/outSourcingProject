package com.example.outsourcingproject.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

	private int rating;

	private String content;

	private Long menuId;

	private Long storeId;
}
