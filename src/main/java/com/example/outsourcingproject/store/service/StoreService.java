package com.example.outsourcingproject.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.dto.MenuResponseDto;
import com.example.outsourcingproject.menu.service.MenuService;
import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.store.dto.responseDto.StoreResponseDto;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.enums.UserRole;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	// private final User user;
	private final MenuService menuService;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public StoreResponseDto registerStore(Long userId, StoreRequestDto storeRequestDto, HttpServletRequest request) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


		Long storeCount = storeRepository.countStoresByUser(user);

		if (!user.getUserRole().equals(UserRole.OWNER)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "사장님만이 가게를 등록할 수 있습니다.");
		}


		if (storeCount > 3) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가게를 세 개 초과해서 등록할 수 없습니다.");
		}

		if (storeRepository.findByStoreName(storeRequestDto.getStoreName()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 가게 이름입니다.");
		}

		if (storeRepository.findByStoreTelNumber(storeRequestDto.getStoreTelNumber()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 가게 전화번호입니다.");
		}


		Store store = Store.builder()
			.storeName(storeRequestDto.getStoreName())
			.storeTelNumber(storeRequestDto.getStoreTelNumber())
			.address(storeRequestDto.getAddress())
			.openTime(storeRequestDto.getOpenTime())
			.closeTime(storeRequestDto.getCloseTime())
			.minOrderPrice(storeRequestDto.getMinOrderPrice())
			.user(user)
			.build();

		Store savedStore = storeRepository.save(store);

		return StoreResponseDto.from(savedStore);
	}

	@Transactional
	public StoreResponseDto updateById(Long id, StoreRequestDto storeRequestDto, HttpServletRequest request) {

		Store findStore = storeRepository.findByIdOrElseThrow(id);

		findStore.updateStore(storeRequestDto);

		return StoreResponseDto.from(findStore);
	}

	public StoreResponseDto findById(Long id) {

		Store findStore = storeRepository.findByIdOrElseThrow(id);

		List<MenuResponseDto> menuList = menuService.findByStoreId(findStore.getId());

		return StoreResponseDto.fromMenu(findStore, menuList);
	}

	public List<StoreResponseDto> findAll() {

		List<Store> store = storeRepository.findAll();

		return store
			.stream()
			.map(StoreResponseDto::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public void delete(Long userId, Long id) {

		// 유저 일치 예외처리 만들기

		Store findStore = storeRepository.findByIdOrElseThrow(id);

		if(!findStore.getUser().getUserId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
		}

		findStore.deleteStore();


	}
}
