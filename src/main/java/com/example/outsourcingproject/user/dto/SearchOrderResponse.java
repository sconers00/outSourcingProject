package com.example.outsourcingproject.user.dto;

import java.time.LocalDateTime;

import com.example.outsourcingproject.order.entity.Orders;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchOrderResponse {
	private Long orderId;
	private Long storeId;
	private Long menuId;
	private Long quantity;
	private String address;
	private String orderStatus;
	private LocalDateTime orderedAt;

	@Builder
	public SearchOrderResponse(Long orderId, Long storeId, Long menuId, Long quantity, String address,
		String orderStatus,
		LocalDateTime orderedAt) {
		this.orderId = orderId;
		this.storeId = storeId;
		this.menuId = menuId;
		this.quantity = quantity;
		this.address = address;
		this.orderStatus = orderStatus;
		this.orderedAt = orderedAt;
	}

	public SearchOrderResponse(Orders orders) {
		this.orderId = orders.getOrderId();
		this.storeId = orders.getStore().getId();
		this.menuId = orders.getMenu().getMenuId();
		this.quantity = orders.getQuantity();
		this.address = orders.getAddress();
		this.orderStatus = orders.getOrderStatus().toString();
		this.orderedAt = orders.getCreatedAt();
	}
}
