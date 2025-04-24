package com.example.outsourcingproject.review.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long orderId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long menuId;

	@Column(nullable = false)
	private Long storeId;

	@Column(nullable = false)
	private int rating;

	@Column(nullable = false)
	private String content;
}
