package com.example.outsourcingproject.store.entity;

import com.example.outsourcingproject.common.entity.BaseEntity;
import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String storeName;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String storeTelNumber;

	@Column(nullable = false)
	private String openTime;

	@Column(nullable = false)
	private String closeTime;

	@Column(nullable = false)
	private Long minOrderPrice;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

	private boolean isDeleted = false;

	@Builder
	public Store(String storeName, String address, String storeTelNumber, String openTime, String closeTime,
		Long minOrderPrice, User userId) {
		this.storeName = storeName;
		this.address = address;
		this.storeTelNumber = storeTelNumber;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderPrice = minOrderPrice;
		this.userId = userId;
	}

	public void updateStore(StoreRequestDto storeRequestDto) {
		this.storeName = storeRequestDto.getStoreName();
		this.address = storeRequestDto.getAddress();
		this.storeTelNumber = storeRequestDto.getStoreTelNumber();
		this.openTime = storeRequestDto.getOpenTime();
		this.closeTime = storeRequestDto.getCloseTime();
	}

	public void deleteStore(String storeName, boolean isDeleted) {
		this.storeName = storeName;
		this.isDeleted = isDeleted;
	}
}
