package com.example.outsourcingproject.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.dto.MenuUpdateRequestDto;
import com.example.outsourcingproject.menu.exception.NotFoundException;
import com.example.outsourcingproject.menu.service.MenuService;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;
	private final StoreRepository storeRepository;

	public Store getStoreId(Long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "가게 ID가 잘못되었거나 없는 가게입니다."));
		return store;
	}

	@PostMapping("/menus")
	public ResponseEntity<MenuResponseDto> addMenu(@PathVariable Long storeId,
		@Valid @RequestBody MenuRequestDto menuRequest) {
		MenuResponseDto menuResponseDto = menuService.save(menuRequest, getStoreId(storeId));
		return new ResponseEntity<>(menuResponseDto, HttpStatus.CREATED);
	}

	@PatchMapping("/menus/{menuId}")
	public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId,
		@PathVariable Long menuId, @Valid @RequestBody MenuUpdateRequestDto menuUpdateRequest) {
		MenuResponseDto menuResponseDto = menuService.updateMenu(menuUpdateRequest, getStoreId(storeId), menuId);
		return new ResponseEntity<>(menuResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/menus/{menuId}")
	public void deleteMenu(@PathVariable Long storeId, @PathVariable Long menuId) {
		menuService.delete(storeId, menuId);
	}
}
