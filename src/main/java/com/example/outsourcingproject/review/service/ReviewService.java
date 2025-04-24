package com.example.outsourcingproject.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.review.dto.ReviewRequestDto;
import com.example.outsourcingproject.review.dto.ReviewResponseDto;
import com.example.outsourcingproject.review.entity.Review;
import com.example.outsourcingproject.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	// 실제 구현체는 DI로 주입되어야 함
	private final OrderService orderService;
	private final MenuService menuService;

	@Transactional
	public ReviewResponseDto createReview(Long orderId, ReviewRequestDto reviewRequestDto, Long userId) {
		if (reviewRepository.existsByOrderId(orderId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 리뷰가 작성된 주문입니다.");
		}

		OrderDto orderDto = orderService.findOrderById(orderId);

		if (!orderDto.getUserId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 주문에 대해서만 리뷰를 작성할 수 있습니다.");
		}

		if (!orderDto.isDeliveryCompleted()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "배달이 완료된 주문에 대해서만 리뷰를 작성할 수 있습니다.");
		}

		if (reviewRequestDto.getRating() < 1 || reviewRequestDto.getRating() > 5) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "별점은 1점에서 5점 사이여야 합니다.");
		}

		String menuName = menuService.getMenuNameById(orderDto.getMenuId());

		Review review = Review.builder()
			.orderId(orderId)
			.userId(userId)
			.menuId(orderDto.getMenuId())
			.storeId(orderDto.getStoreId())
			.rating(reviewRequestDto.getRating())
			.content(reviewRequestDto.getContent())
			.build();

		Review savedReview = reviewRepository.save(review);

		return ReviewResponseDto.from(savedReview, menuName);
	}

	@Transactional(readOnly = true)
	public Page<ReviewResponseDto> getStoreReviews(Long storeId, Integer minRating, Integer maxRating, Pageable pageable) {
		if (!reviewRepository.existsByStoreId(storeId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다.");
		}

		int min = minRating != null ? minRating : 1;
		int max = maxRating != null ? maxRating : 5;

		if (min < 1 || max > 5 || min > max) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 별점 범위입니다.");
		}

		Page<Review> reviews = reviewRepository.findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(storeId, min, max, pageable);

		return reviews.map(review -> {
			String menuName = menuService.getMenuNameById(review.getMenuId());
			return ReviewResponseDto.from(review, menuName);
		});
	}

	// =================== 내부 인터페이스 정의 ===================

	public interface OrderService {
		OrderDto findOrderById(Long orderId);
	}

	public interface OrderDto {
		Long getUserId();
		Long getMenuId();
		Long getStoreId();
		boolean isDeliveryCompleted();
	}

	public interface MenuService {
		String getMenuNameById(Long menuId);
	}
}
