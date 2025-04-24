package com.example.outsourcingproject.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcingproject.common.JwtUtil;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Transactional
	public void deleteAccount(HttpServletRequest request) {
		long usersId = jwtUtil.getIdFromRequest(request);
		User userFounded = userRepository.findById(usersId).orElseThrow();
		userFounded.deleteAccount(true);
	}
}
