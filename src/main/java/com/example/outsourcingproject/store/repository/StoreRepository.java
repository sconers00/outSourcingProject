package com.example.outsourcingproject.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.user.entity.User;

import jakarta.validation.constraints.NotBlank;

public interface StoreRepository extends JpaRepository<Store, Long> {

	default Store findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
	}

	List<Store> id(Long id);

	Optional<Store> findByStoreName(String storeName);

	Optional<Store> findByStoreTelNumber(String storeName);

	Long countStoresByUser(User user);
}
