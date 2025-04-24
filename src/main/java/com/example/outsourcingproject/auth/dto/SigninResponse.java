package com.example.outsourcingproject.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SigninResponse {
	private final String token;

	@Builder
	public SigninResponse(String token) {
		this.token = token;
	}
}
