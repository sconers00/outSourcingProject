package com.example.outsourcingproject.order.controller;

import org.springframework.http.ResponseEntity;
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
		orderService.createOrder(dto, request);
		return null;
	}

}
