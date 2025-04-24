package com.example.outsourcingproject.menu.service;

import org.springframework.stereotype.Service;

import com.example.outsourcingproject.menu.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
	private final MenuRepository menuRepository;
	/*
	@Transactional
	public MenuResponseDto save(MenuRequestDto menuRequest, Store store) {
		Menu menu = Menu.builder()
			.storeId(store.getStoreId())
			.menuName(menuRequest.getMenuName())
			.menuPrice(menuRequest.getMenuPrice())
			.menuDiscription(menuRequest.getDiscription())
			.build();

		menuRepository.save(menu);

		MenuResponseDto menuResponseDto = MenuResponseDto.builder()
			.menuName(menuRequest.getMenuName())
			.menuPrice(menuRequest.getMenuPrice())
			.discription(menuRequest.getDiscription())
			.build();
		return menuResponseDto;

	}

	@Transactional
	public MenuResponseDto updateMenu(MenuUpdateRequestDto request, Store store, Long menuId) {

		Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "메뉴 ID가 잘못되었거나 없는 메뉴입니다."));
		if (!store.getStoreId().equals(menu.getStoreId())) {
			throw new NotFoundException(HttpStatus.BAD_REQUEST, "가게ID와 메뉴 소유 점포 ID가 일치하지 않습니다.");
		}

		menu.updateMenu(request.getMenuName(), request.getMenuPrice(), request.getDiscription());

		MenuResponseDto menuResponseDto = MenuResponseDto.builder()
			.menuName(request.getMenuName())
			.menuPrice(request.getMenuPrice())
			.discription(request.getDiscription())
			.build();
		return menuResponseDto;
	}

	public ResponseEntity<MenuDeleteResponseDto> delete(Long storeId, Long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "메뉴 ID가 잘못되었거나 없는 메뉴입니다."));
		if (!store.getStoreId().equals(menu.getStoreId())) {
			throw new NotFoundException(HttpStatus.BAD_REQUEST, "가게ID와 메뉴 소유 점포 ID가 일치하지 않습니다.");
		}
		menu.deltetMenu("삭제된 메뉴입니다.", 999999L, true);
		MenuDeleteResponseDto result = new MenuDeleteResponseDto("메뉴가 삭제되었습니다.", menuId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	 */
}
