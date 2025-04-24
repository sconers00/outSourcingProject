package com.example.outsourcingproject.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuDeleteResponseDto {

	private String message;

	@NotNull
	private Long menuId;
}
