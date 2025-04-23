package com.example.outsourcingproject.common;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.auth.entity.AuthUser;
import com.example.outsourcingproject.user.entity.User;
import com.example.outsourcingproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
			.map(this::createUserDetails)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	private UserDetails createUserDetails(User user) {
		return AuthUser.builder()
			.id(user.getId())
			.username(user.getEmail())
			.password(user.getPassword())
			.userRole(user.getUserRole())
			.build();
	}
}
