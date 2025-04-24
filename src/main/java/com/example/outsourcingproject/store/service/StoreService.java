package com.example.outsourcingproject.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
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

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public StoreResponseDto registerStore(Long userId, StoreRequestDto storeRequestDto,  HttpServletRequest request) {

	//	getUserId(userId);
	// 	if(User.userRole().equals(UserRole.OWNER) {
	// 		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "사장님만이 가게를 등록할 수 있습니다.");
	// }



		Store store = storeRequestDto.toEntity();

		Store savedStore = storeRepository.save(store);

		return StoreResponseDto.from(savedStore);
	}

	@Transactional
	public StoreResponseDto updateById(Long id, StoreRequestDto storeRequestDto) {

		Store findStore = storeRepository.findByIdOrElseThrow(id);

		findStore.updateStore(storeRequestDto);

		return StoreResponseDto.from(findStore);
	}

	public StoreResponseDto findById(Long id) {

		Store findStore = storeRepository.findByIdOrElseThrow(id);

		return StoreResponseDto.from(findStore);
	}

	public List<StoreResponseDto> findAll() {

		List<Store> store = storeRepository.findAll();

		return store
			.stream()
			.map(StoreResponseDto::from)
			.collect(Collectors.toList());
	}

	public void delete(Long id) {

		Store findStore = storeRepository.findByIdOrElseThrow(id);

		storeRepository.delete(findStore);
	}
}
