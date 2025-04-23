package com.example.outsourcingproject.common;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.outsourcingproject.user.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	private static final String prefix = "Bearer ";
	private static final long tokenLife = 24 * 60 * 60 * 1000L;

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;

	@PostConstruct
	public void init() {
		byte[] keyByte = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyByte);
	}

	public String createToken(Long userId, String email, UserRole userRole) {
		Date date = new Date();

		return prefix + Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim("email", email)
			.claim("userRole", userRole)
			.setExpiration(new Date(date.getTime() + tokenLife))
			.setIssuedAt(date)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String subStringToken(String token) {
		if (!StringUtils.hasText(token) || !token.startsWith(prefix)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return token.substring(7);
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

}
