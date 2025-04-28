package com.example.outsourcingproject.store.dto.responseDto;

import com.example.outsourcingproject.store.entity.Store;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreResponseDto {

	private final Long id;

	private final String storeName;

	private final String address;

	private final String storeTelNumber;

	private final String openTime;

	private final String closeTime;

	private final Long minOrderPrice;

	@Builder
	public StoreResponseDto(Long id, String storeName, String address, String storeTelNumber, String openTime,
		String closeTime, Long minOrderPrice) {
		this.id = id;
		this.storeName = storeName;
		this.address = address;
		this.storeTelNumber = storeTelNumber;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderPrice = minOrderPrice;
	}

	public static StoreResponseDto from(Store store) {
		return StoreResponseDto.builder()
			.id(store.getId())
			.storeName(store.getStoreName())
			.address(store.getAddress())
			.storeTelNumber(store.getStoreTelNumber())
			.openTime(store.getOpenTime())
			.closeTime(store.getCloseTime())
			.minOrderPrice(store.getMinOrderPrice())
			.build();
	}
}
