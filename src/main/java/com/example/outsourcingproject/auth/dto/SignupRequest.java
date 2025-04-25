package com.example.outsourcingproject.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$")
	@NotBlank
	private String email;
	@NotBlank
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{4,}$")
	private String password;
	@NotBlank
	private String userRole;
	@NotNull
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
	private String userTel;

	@Builder
	public SignupRequest(String email, String password, String userRole, String userTel) {
		this.email = email;
		this.password = password;
		this.userRole = userRole;
		this.userTel = userTel;
	}
}
