package com.example.outsourcingproject.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.order.dto.OrderRequestDto;
import com.example.outsourcingproject.order.dto.OrderResponse;
import com.example.outsourcingproject.order.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequestDto dto, HttpServletRequest request) {
		return ResponseEntity.ok()
			.body(orderService.createOrder(dto, request));
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<OrderResponse> cancelOrder(HttpServletRequest request, @PathVariable Long orderId) {
		return ResponseEntity.ok()
			.body(orderService.cancelOrder(request, orderId));
	}
}
