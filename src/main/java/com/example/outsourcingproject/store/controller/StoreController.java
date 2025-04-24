package com.example.outsourcingproject.store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.store.dto.responseDto.StoreResponseDto;
import com.example.outsourcingproject.store.service.StoreService;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/{userId}/stores")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<StoreResponseDto> registerStore(@PathVariable Long userId,
		@RequestBody @Valid StoreRequestDto storeRequestDto, HttpServletRequest request) {

		StoreResponseDto storeResponseDto = storeService.registerStore(userId, storeRequestDto, request);

		return new ResponseEntity<>(storeResponseDto, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<StoreResponseDto> updateStore(
		@PathVariable Long id,
		@RequestBody @Valid StoreRequestDto storeRequestDto) {

		StoreResponseDto storeResponseDto = storeService.updateById(id, storeRequestDto);

		return new ResponseEntity<>(storeResponseDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StoreResponseDto> findById(@PathVariable Long id) {

		StoreResponseDto storeResponseDto = storeService.findById(id);

		return new ResponseEntity<>(storeResponseDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<StoreResponseDto>> findAll() {

		List<StoreResponseDto> findAllStore = storeService.findAll();

		return new ResponseEntity<>(findAllStore, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {

		storeService.delete(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
