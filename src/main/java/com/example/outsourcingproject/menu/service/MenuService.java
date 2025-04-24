package com.example.outsourcingproject.menu.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuDeleteResponseDto;
import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.dto.MenuUpdateRequestDto;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.menu.exception.MismatchException;
import com.example.outsourcingproject.menu.exception.NotFoundException;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.store.entity.Store;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {
	private final MenuRepository menuRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public MenuResponseDto save(MenuRequestDto menuRequest, Store store, HttpServletRequest request) {
		if (userchecker(request)) {
			throw new MismatchException(HttpStatus.FORBIDDEN, "본인 소유의 점포에만 매뉴를 추가할 수 있습니다.");
		}
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
	public MenuResponseDto updateMenu(MenuUpdateRequestDto request, Store store, Long menuId,
		HttpServletRequest Servletrequest) {

		if (userchecker(Servletrequest)) {
			throw new MismatchException(HttpStatus.FORBIDDEN, "본인 소유 점포의 매뉴만 수정할 수 있습니다.");
		}

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

	public ResponseEntity<MenuDeleteResponseDto> delete(Long storeId, Long menuId, HttpServletRequest request) {

		if (userchecker(request)) {
			throw new MismatchException(HttpStatus.FORBIDDEN, "본인 소유 점포의 매뉴만 삭제할 수 있습니다.");
		}

		Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "메뉴 ID가 잘못되었거나 없는 메뉴입니다."));
		if (!store.getStoreId().equals(menu.getStoreId())) {
			throw new NotFoundException(HttpStatus.BAD_REQUEST, "가게ID와 메뉴 소유 점포 ID가 일치하지 않습니다.");
		}
		menu.deltetMenu("삭제된 메뉴입니다.", 999999L, true);
		MenuDeleteResponseDto result = new MenuDeleteResponseDto("메뉴가 삭제되었습니다.", menuId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public boolean userchecker(HttpServletRequest request) {
		boolean check = (jwtUtil.getIdFromRequest(request) != store.getStoreId());
		return check;
	}
}
