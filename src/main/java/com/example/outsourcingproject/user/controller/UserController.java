package com.example.outsourcingproject.user.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

	//유저를 찾으시려면
	//컨트롤러에서는 파라미터로 HttpServletRequest를 받아 서비스에 그대로 넘겨주세요
	//서비스에서 해당 객체를 이용해 유저 id를 추출할 수 있습니다.
	//UserService에 관련 설명이 있습니다.
	@PatchMapping
	public ResponseEntity<String> deleteAccount(HttpServletRequest request) {
		userService.deleteAccount(request);
		ResponseCookie cookie = ResponseCookie.from("token")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("Strict")
			.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok()
			.headers(headers)
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
