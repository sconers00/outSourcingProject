package com.example.outsourcingproject.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.user.dto.SearchOrderResponse;
import com.example.outsourcingproject.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PatchMapping
	public ResponseEntity<String> deleteAccount(HttpServletRequest request) {
		userService.deleteAccount(request);
		return ResponseEntity.ok()
			.body("탈퇴 되었습니다.");
	}

	@GetMapping("/me/orders")
	public ResponseEntity<List<SearchOrderResponse>> searchRequestedOrder(HttpServletRequest request,
		@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
		List<SearchOrderResponse> list = userService.searchRequestedOrder(request, index);
		return ResponseEntity.ok()
			.body(list);
	}
}
