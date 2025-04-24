package com.example.outsourcingproject.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

	default Store findByIdOrElseThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
	}

}
