package com.example.outsourcingproject.store.dto.requestDto;

import com.example.outsourcingproject.store.entity.Store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreRequestDto {

	@NotBlank(message = "가게 이름은 필수입니다.")
	private final String storeName;

	@NotBlank(message = "가게 주소는 필수입니다.")
	private final String address;

	@NotBlank(message = "가게 번호는 필수입니다.")
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 02-1234-5678)")
	private final String storeTelNumber;

	@NotBlank(message = "가게 영업 시작 시간은 필수입니다.")
	@Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간 형식은 HH:mm 형식이어야 합니다. (예: 09:00)")
	private final String openTime;

	@NotBlank(message = "가게 영업 종료 시간은 필수입니다.")
	@Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "시간 형식은 HH:mm 형식이어야 합니다. (예: 09:00)")
	private final String closeTime;

	@NotBlank(message = "최소 주문 금액은 필수입니다")
	private final int minOrderPrice;

	@Builder
	public StoreRequestDto(String storeName, String address, String storeTelNumber, String openTime, String closeTime,
		int minOrderPrice) {
		this.storeName = storeName;
		this.address = address;
		this.storeTelNumber = storeTelNumber;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderPrice = minOrderPrice;
	}

	public Store toEntity() {
		return Store.builder()
			.storeName(storeName)
			.address(address)
			.storeTelNumber(storeTelNumber)
			.openTime(openTime)
			.closeTime(closeTime)
			.minOrderPrice(minOrderPrice)
			.build();

	}
}
