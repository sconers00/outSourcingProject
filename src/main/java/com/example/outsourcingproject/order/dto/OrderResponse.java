package com.example.outsourcingproject.order.dto;

import java.time.LocalDateTime;

import com.example.outsourcingproject.order.entity.Orders;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponse {
	private Long orderId;
	private Long userId;
	private Long storeId;
	private Long menuId;
	private Long quantity;
	private String address;
	private String orderStatus;
	private LocalDateTime orderedAt;

	@Builder
	public OrderResponse(Long orderId, Long storeId, Long menuId, Long quantity, String address, String orderStatus,
		LocalDateTime orderedAt, Long userId) {
		this.orderId = orderId;
		this.userId = userId;
		this.storeId = storeId;
		this.menuId = menuId;
		this.quantity = quantity;
		this.address = address;
		this.orderStatus = orderStatus;
		this.orderedAt = orderedAt;
	}

	public OrderResponse(Orders orders) {
		this.orderId = orders.getOrderId();
		this.userId = orders.getUser().getUserId();
		this.storeId = 1L;
		this.menuId = orders.getMenu().getMenuId();
		this.quantity = orders.getQuantity();
		this.address = orders.getAddress();
		this.orderStatus = orders.getOrderStatus().toString();
		this.orderedAt = orders.getCreatedAt();
	}
}
