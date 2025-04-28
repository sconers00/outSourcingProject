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

import com.example.outsourcingproject.menu.dto.MenuDeleteResponseDto;
import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.service.MenuService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stores/{storeId}")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	@PostMapping("/menus")//메뉴추가
	public ResponseEntity<MenuResponseDto> addMenu(@PathVariable Long storeId,
		@Valid @RequestBody MenuRequestDto menuRequest, HttpServletRequest request) {
		MenuResponseDto menuResponseDto = menuService.save(menuRequest, storeId, request);
		return new ResponseEntity<>(menuResponseDto, HttpStatus.CREATED);
	}

	@PatchMapping("/menus/{menuId}")//메뉴수정
	public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long storeId,
		@PathVariable Long menuId, @Valid @RequestBody MenuRequestDto menuRequest,
		HttpServletRequest request) {
		MenuResponseDto menuResponseDto = menuService.updateMenu(menuRequest, storeId, menuId,
			request);
		return new ResponseEntity<>(menuResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/menus/{menuId}")//메뉴 삭제-soft
	public ResponseEntity<MenuDeleteResponseDto> deleteMenu(@PathVariable Long storeId, @PathVariable Long menuId,
		HttpServletRequest request) {
		return menuService.delete(storeId, menuId, request);
	}

}
