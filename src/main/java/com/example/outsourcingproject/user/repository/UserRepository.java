package com.example.outsourcingproject.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcingproject.user.entity.User;

import jakarta.validation.constraints.NotEmpty;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);

	Optional<User> findByEmail(@NotEmpty String email);
}
