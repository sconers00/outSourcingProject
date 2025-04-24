package com.example.outsourcingproject.order.enums;

import java.util.Arrays;

public enum OrderStatus {
	PENDING, PREPARING, ON_DELIVERY, ARRIVED;

	public static OrderStatus of(String orderStatus) {
		return Arrays.stream(OrderStatus.values())
			.filter(r -> r.name().equalsIgnoreCase(orderStatus))
			.findFirst()
			.orElseThrow();
	}
}
