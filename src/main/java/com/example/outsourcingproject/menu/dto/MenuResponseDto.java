package com.example.outsourcingproject.menu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponseDto {
	private String menuName;
	private Long menuPrice;
	@Builder.Default
	private String discription = "";
}
