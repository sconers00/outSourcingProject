package com.example.outsourcingproject.menu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuRequestDto {
	@NotBlank
	private String menuName;

	@NotNull
	@Min(100)
	@Max(900000)
	private Long menuPrice;

	private String discription;
}
