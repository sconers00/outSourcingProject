package com.example.outsourcingproject.user.enums;

import java.util.Arrays;

public enum UserRole {
	OWNER, CUSTOMER, ADMIN;

	public static UserRole of(String role) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(role))
			.findFirst()
			.orElseThrow();
	}
}