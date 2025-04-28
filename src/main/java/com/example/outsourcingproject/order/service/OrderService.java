package com.example.outsourcingproject.order.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.order.dto.OrderRequestDto;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.entity.Orders;
import com.example.outsourcingproject.order.enums.OrderStatus;
import com.example.outsourcingproject.order.repository.OrderRepository;
import com.example.outsourcingproject.store.entity.Store;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.enums.UserRole;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final JwtUtil jwtUtil;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;

	public OrderResponse createOrder(OrderRequestDto dto, HttpServletRequest request) {
		Long userId = jwtUtil.getIdFromRequest(request);
		User userFounded = userRepository.findById(userId).orElseThrow();
		if (!userFounded.getUserRole().equals(UserRole.CUSTOMER)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "손님이 아닌 유저는 주문할 수 없습니다.");
		}
		Orders orders = Orders.builder()
			.user(userFounded)
			.store(storeRepository.findById(dto.getStoreId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
			.menu(menuRepository.findById(dto.getMenuId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
			.address(dto.getAddress())
			.quantity(dto.getQuantity())
			.orderStatus(OrderStatus.PENDING)
			.build();

		Orders savedOrders = orderRepository.save(orders);

		return new OrderResponse(savedOrders);
	}

	@Transactional
	public OrderResponse cancelOrder(HttpServletRequest request, Long id) {
		Long userId = jwtUtil.getIdFromRequest(request);
		User userFounded = userRepository.findById(userId).orElseThrow();

		if (!userFounded.getUserRole().equals(UserRole.CUSTOMER)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "손님이 아니면 접근할 수 없습니다.");
		}
		
		Orders orders = orderRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

		if (!orders.getUser().equals(userFounded)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "주문한 유저가 아닙니다.");
		}
		orders.chageStatus("CANCELED");
		return new OrderResponse(orders);
	}

	@Transactional
	public OrderResponse changeOrderState(HttpServletRequest request, String orderState, Long... id) {
		Long userId = jwtUtil.getIdFromRequest(request);
		User userFounded = userRepository.findById(userId).orElseThrow();
		if (userFounded.getUserRole().equals(UserRole.CUSTOMER)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "손님은 접근할 수 없습니다.");
		}

		Orders orders = orderRepository.findById(id[0])
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

		Store store = storeRepository.findById(id[1])
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!store.getUser().equals(userFounded)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 가게의 주인이 아닙니다.");
		}
		if (!orders.getStore().equals(store)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 가게의 주문이 아닙니다.");
		}

		orders.chageStatus(orderState);
		return new OrderResponse(orders);
	}

	public List<SearchOrderResponse> findOrderByStore(HttpServletRequest request, Long storeId, String status,
		int index) {
		Long userId = jwtUtil.getIdFromRequest(request);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!store.getUser().equals(user)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 가게의 주인이 아닙니다.");
		}

		PageRequest pageRequest = PageRequest.of(index - 1, 10);

		if (status.equals("ALL")) {
			return orderRepository.findAllByStoreOrElseThrow(store, pageRequest)
				.stream()
				.map(SearchOrderResponse::new)
				.toList();
		}

		return orderRepository.findAllByOrderStatusAndStoreOrElseThrow(OrderStatus.of(status), store, pageRequest)
			.stream()
			.map(SearchOrderResponse::new)
			.toList();
	}

	// 타 service 에서 접근하는 메서드
	public List<Orders> findOrderByUser(User user, PageRequest pageRequest) {
		return orderRepository.findAllByUserOrElseThrow(user, pageRequest).stream().toList();
	}
}
