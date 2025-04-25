package com.example.outsourcingproject.user.entity;

import org.hibernate.annotations.ColumnDefault;

import com.example.outsourcingproject.common.BaseEntity;
import com.example.outsourcingproject.user.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$")
	@Column(unique = true)
	private String email;

	@NotEmpty
	private String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@NotNull
	@Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
	private String userTel;

	@NotNull
	@ColumnDefault("false")
	@Column(name = "is_deleted")
	private boolean isDeleted;

	@Builder
	public User(String email, String password, UserRole userRole, String userTel) {
		this.email = email;
		this.userTel = userTel;
		this.password = password;
		this.userRole = userRole;
	}

	public void deleteAccount(boolean delete) {
		this.isDeleted = true;
	}
}
