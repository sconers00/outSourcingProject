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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_table")
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotEmpty
	@Column(unique = true)
	private String email;

	@NotEmpty
	private String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@NotNull
	@ColumnDefault("false")
	@Column(name = "is_deleted")
	private boolean isDeleted;

	@Builder
	public User(String email, String password, UserRole userRole) {
		this.email = email;
		this.password = password;
		this.userRole = userRole;
	}

	public void deleteAccount(boolean delete) {
		this.isDeleted = true;
	}
}
