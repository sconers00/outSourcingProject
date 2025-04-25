package com.example.outsourcingproject.store.dto.responseDto;

import java.util.List;

import com.example.outsourcingproject.menu.dto.MenuResponseDto;
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

	private final int minOrderPrice;

	private final List<MenuResponseDto> menuList;

	@Builder
	public StoreResponseDto(Long id, String storeName, String address, String storeTelNumber, String openTime,
		String closeTime, int minOrderPrice, List<MenuResponseDto> menuList) {
		this.id = id;
		this.storeName = storeName;
		this.address = address;
		this.storeTelNumber = storeTelNumber;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderPrice = minOrderPrice;
		this.menuList = menuList;
	}


	public static StoreResponseDto fromMenu(Store store, List<MenuResponseDto> menuList) {
		return StoreResponseDto.builder()
			.id(store.getId())
			.storeName(store.getStoreName())
			.address(store.getAddress())
			.storeTelNumber(store.getStoreTelNumber())
			.openTime(store.getOpenTime())
			.closeTime(store.getCloseTime())
			.minOrderPrice(store.getMinOrderPrice())
			.menuList(menuList)
			.build();
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
