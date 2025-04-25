package com.example.outsourcingproject.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuDeleteResponseDto;
import com.example.outsourcingproject.menu.dto.MenuRequestDto;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.entity.Menu;
import com.example.outsourcingproject.menu.exception.MismatchException;
import com.example.outsourcingproject.menu.exception.NotFoundException;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public MenuResponseDto save(MenuRequestDto menuRequest, Long storeId, HttpServletRequest request) {//메뉴 생성기
		Store store = getStoreId(storeId);
		if (userChecker(request, store)) {
			throw new MismatchException(HttpStatus.FORBIDDEN, "본인 소유의 점포에만 매뉴를 추가할 수 있습니다.");
		}
		Menu menu = Menu.builder()
			.storeId(store)
			.menuName(menuRequest.getMenuName())
			.menuPrice(menuRequest.getMenuPrice())
			.discription(menuRequest.getDiscription())
			.build();

		menuRepository.save(menu);

		MenuResponseDto menuResponseDto = MenuResponseDto.builder()
			.menuId(menu.getMenuId())
			.menuName(menuRequest.getMenuName())
			.menuPrice(menuRequest.getMenuPrice())
			.discription(menuRequest.getDiscription())
			.build();
		return menuResponseDto;

	}

	@Transactional
	public MenuResponseDto updateMenu(MenuRequestDto request, Long storeId, Long menuId,
		HttpServletRequest ServletRequest) {//메뉴 수정기
		Store store = getStoreId(storeId);

		if (userChecker(ServletRequest, store)) {
			throw new MismatchException(HttpStatus.FORBIDDEN, "본인 소유 점포의 매뉴만 수정할 수 있습니다.");
		}

		Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "메뉴 ID가 잘못되었거나 없는 메뉴입니다."));
		if (!store.getId().equals(menu.getStoreId().getId())) {
			throw new NotFoundException(HttpStatus.BAD_REQUEST, "가게 ID와 메뉴 소유 점포 ID가 일치하지 않습니다.");
		}

		menu.updateMenu(request.getMenuName(), request.getMenuPrice(), request.getDiscription());

		MenuResponseDto menuResponseDto = MenuResponseDto.builder()
			.menuId(menu.getMenuId())
			.menuName(request.getMenuName())
			.menuPrice(request.getMenuPrice())
			.discription(request.getDiscription())
			.build();
		return menuResponseDto;
	}

	@Transactional
	public ResponseEntity<MenuDeleteResponseDto> delete(Long storeId, Long menuId, HttpServletRequest request) {
		//메뉴 삭제기, softDelete
		Store store = getStoreId(storeId);
		if (userChecker(request, store)) {
			throw new MismatchException(HttpStatus.FORBIDDEN, "본인 소유 점포의 매뉴만 삭제할 수 있습니다.");
		}

		Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "메뉴 ID가 잘못되었거나 없는 메뉴입니다."));
		if (!store.getId().equals(menu.getStoreId().getId())) {
			throw new NotFoundException(HttpStatus.BAD_REQUEST, "가게 ID와 메뉴 소유 점포 ID가 일치하지 않습니다.");
		}
		menu.deletetMenu("삭제된 메뉴입니다.", 999999L, true);
		MenuDeleteResponseDto result = new MenuDeleteResponseDto("메뉴가 삭제되었습니다.", menuId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	public boolean userChecker(HttpServletRequest request, Store store) {//점포 소유자 본인인지 확인
		return !(jwtUtil.getIdFromRequest(request) == store.getUserId().getUserId());
	}

	public Store getStoreId(Long storeId) {//가게 id 추출기
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new NotFoundException(HttpStatus.NOT_FOUND, "가게 ID가 잘못되었거나 없는 가게입니다."));
		return store;
	}

	public List<MenuResponseDto> findByStoreId(Long storeId) {
		List<Menu> menuList = menuRepository.findByStoreId(getStoreId(storeId), 999999L);
		return menuList.stream().map(MenuResponseDto::toDto).collect(Collectors.toList());
	}
}
