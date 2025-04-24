package com.example.outsourcingproject.order.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.menu.repository.MenuRepository;
import com.example.outsourcingproject.order.dto.OrderRequestDto;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.entity.Order;
import com.example.outsourcingproject.order.enums.OrderStatus;
import com.example.outsourcingproject.order.repository.OrderRepository;
import com.example.outsourcingproject.store.repository.StoreRepository;
import com.example.outsourcingproject.user.entity.User;
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
		Order order = Order.builder()
			.store(storeRepository.findById(dto.getStoreId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
			.menu(menuRepository.findById(dto.getMenuId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
			.address(dto.getAddress())
			.quantity(dto.getQuantity())
			.orderStatus(OrderStatus.PENDING)
			.build();

		Order savedOrder = orderRepository.save(order);

		return new OrderResponse(savedOrder);
	}

	@Transactional
	public OrderResponse cancelOrder(HttpServletRequest request, Long orderID) {
		Long userId = jwtUtil.getIdFromRequest(request);
		User userFounded = userRepository.findById(userId).orElseThrow();

		Order order = orderRepository.findById(orderID)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!order.getUser().equals(userFounded)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		order.chageStatus("CANCELED");

		return new OrderResponse(order);
	}
}
