package com.example.outsourcingproject.menu.dto;

import com.example.outsourcingproject.menu.entity.Menu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponseDto {
	private String menuName;
	private Long menuPrice;
	@Builder.Default
	private String discription = "";

	public static MenuResponseDto toDto(Menu menu) {
		return MenuResponseDto.builder()
			.menuName(menu.getMenuName())
			.menuPrice(menu.getMenuPrice())
			.discription(menu.getDiscription())
			.build();
	}
}
