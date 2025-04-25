package com.example.outsourcingproject.common;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.user.enums.UserRole;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter implements Filter {

	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		String uri = httpServletRequest.getRequestURI();

		System.out.println("is it filtering?");
		if (uri.startsWith("/api/auth")) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		if (uri.startsWith("/error")) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		if (uri.startsWith("/")) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		String jwt = getTokenFromCookie(httpServletRequest);
		if (jwt == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Cookie[] cookies = httpServletRequest.getCookies();
		String token = Arrays.stream(cookies)
			.filter(c -> c.getName().equals("token"))
			.map(Cookie::getValue)
			.findFirst()
			.orElseThrow();
		Claims claims = jwtUtil.getClaims(token);
		if (claims == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

		httpServletRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
		httpServletRequest.setAttribute("email", claims.get("email"));
		httpServletRequest.setAttribute("userRole", claims.get("userRole"));

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	public String getTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookie = request.getCookies();
		if (cookie == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return Arrays.stream(cookie)
			.map(Cookie::getValue)
			.findFirst()
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}
}
