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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.order.dto.ChangeOrderState;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.service.OrderService;
import com.example.outsourcingproject.store.dto.requestDto.StoreRequestDto;
import com.example.outsourcingproject.store.dto.responseDto.GetStoreResponseDto;
import com.example.outsourcingproject.store.dto.responseDto.StoreResponseDto;
import com.example.outsourcingproject.store.service.StoreService;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class StoreController {

	private final StoreService storeService;
	private final OrderService orderService;

	@PostMapping("/stores")
	public ResponseEntity<StoreResponseDto> registerStore(
		@RequestBody @Valid StoreRequestDto storeRequestDto, HttpServletRequest request) {

		StoreResponseDto storeResponseDto = storeService.registerStore(storeRequestDto, request);

		return new ResponseEntity<>(storeResponseDto, HttpStatus.CREATED);
	}

	@PatchMapping("/stores/{id}")
	public ResponseEntity<StoreResponseDto> updateStore(
		@PathVariable Long id,
		@RequestBody @Valid StoreRequestDto storeRequestDto,
		HttpServletRequest request) {

		StoreResponseDto storeResponseDto = storeService.updateById(id, storeRequestDto, request);

		return new ResponseEntity<>(storeResponseDto, HttpStatus.OK);
	}

	@GetMapping("/stores/{id}")
	public ResponseEntity<GetStoreResponseDto> findById(@PathVariable Long id) {

		GetStoreResponseDto getStoreResponseDto = storeService.findById(id);

		return new ResponseEntity<>(getStoreResponseDto, HttpStatus.OK);
	}

	@GetMapping("/stores")
	public ResponseEntity<List<StoreResponseDto>> findAll() {

		List<StoreResponseDto> findAllStore = storeService.findAll();

		return new ResponseEntity<>(findAllStore, HttpStatus.OK);
	}

	@DeleteMapping("/stores/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {

		storeService.delete(id, request);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	//주문 관련 메서드
	@PatchMapping("/stores/{storeId}/orders/{orderId}")
	public ResponseEntity<OrderResponse> changeOrderState(@PathVariable Long orderId, @PathVariable Long storeId,
		HttpServletRequest request,
		@RequestBody ChangeOrderState changeOrderState) {
		return ResponseEntity.ok()
			.body(orderService.changeOrderState(request, changeOrderState.getOrderState(), orderId, storeId));
	}

	@GetMapping("/stores/{storeId}/orders")
	public ResponseEntity<List<SearchOrderResponse>> searchdOrder(HttpServletRequest request,
		@PathVariable Long storeId,
		@RequestParam(value = "status", defaultValue = "ALL", required = false) String status,
		@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
		List<SearchOrderResponse> list = orderService.findOrderByStore(request, storeId, status, index);
		return ResponseEntity.ok()
			.body(list);
	}
}
