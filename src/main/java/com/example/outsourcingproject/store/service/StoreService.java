package com.example.outsourcingproject.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.store.dto.responseDto.StoreResponseDto;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

	@Transactional
	public StoreResponseDto registerStore(StoreRequestDto storeRequestDto) {

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
