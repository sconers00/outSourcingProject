package com.example.outsourcingproject.auth.entity;

import com.example.outsourcingproject.user.enums.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthUser {

	private Long id;
	private String password;
	private UserRole userRole;
	private String username;

	@Builder
	public AuthUser(Long id, String password, UserRole userRole, String username) {
		this.id = id;
		this.password = password;
		this.userRole = userRole;
		this.username = username;
	}
}
