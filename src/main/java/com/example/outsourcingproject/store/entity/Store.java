package com.example.outsourcingproject.store.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "store")
public class Store extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String storeName;

	private String address;

	private String storeTelNumber;

	private String openTime;

	private String closeTime;

	public Store() {
	}

	@Builder
	public Store(String storeName, String address, String storeTelNumber, String openTime, String closeTime
	) {
		this.storeName = storeName;
		this.address = address;
		this.storeTelNumber = storeTelNumber;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

	public void updateStore(StoreRequestDto storeRequestDto) {
		this.storeName = storeRequestDto.getStoreName();
		this.address = storeRequestDto.getAddress();
		this.storeTelNumber = storeRequestDto.getStoreTelNumber();
		this.openTime = storeRequestDto.getOpenTime();
		this.closeTime = storeRequestDto.getCloseTime();
	}
}
