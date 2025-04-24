package com.example.outsourcingproject.menu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.menu.service.MenuService;
import com.example.outsourcingproject.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;
	private final StoreRepository storeRepository;

	/*
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

	 */
}
