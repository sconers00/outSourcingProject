package com.example.outsourcingproject.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.order.entity.Order;
import com.example.outsourcingproject.order.repository.OrderRepository;
import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;

	@Transactional
	public void deleteAccount(HttpServletRequest request) {
		long usersId = jwtUtil.getIdFromRequest(request);
		User userFounded = userRepository.findById(usersId).orElseThrow();
		userFounded.deleteAccount(true);
	}

	public List<SearchOrderResponse> searchRequestedOrder(HttpServletRequest request, int index) {
		long usersId = jwtUtil.getIdFromRequest(request);
		User user = userRepository.findById(usersId).orElseThrow();

		PageRequest pageRequest = PageRequest.of(index - 1, 10);

		Page<Order> orderPage = orderRepository.findAllByUserOrElseThrow(user, pageRequest);
		if (orderPage.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return orderPage.stream().map(SearchOrderResponse::new).toList();
	}
}
