package com.example.outsourcingproject.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
	private Long storeId;
	private Long menuId;
	private Long quantity;
	private String address;

	@Builder
	public OrderRequestDto(Long storeId, Long menuId, Long quantity, String address) {
		this.storeId = storeId;
		this.menuId = menuId;
		this.quantity = quantity;
		this.address = address;
	}
}
