package com.example.outsourcingproject.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeOrderState {

	@NotBlank
	private String orderState;

	private Long storeId;

	@Builder
	public ChangeOrderState(String orderState) {
		this.orderState = orderState;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
}
