package com.example.outsourcingproject.order.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponse {
	private Long orderId;
	private Long storeId;
	private Long menuId;
	private Long quantity;
	private String address;
	private String orderStatus;
	private LocalDateTime orderedAt;

	@Builder
	public OrderResponse(Long orderId, Long storeId, Long menuId, Long quantity, String address, String orderStatus,
		LocalDateTime orderedAt) {
		this.orderId = orderId;
		this.storeId = storeId;
		this.menuId = menuId;
		this.quantity = quantity;
		this.address = address;
		this.orderStatus = orderStatus;
		this.orderedAt = orderedAt;
	}
}
