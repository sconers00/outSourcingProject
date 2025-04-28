package com.example.outsourcingproject.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

//작업중
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("status", String.valueOf(ex.getStatusCode().value()));
		errorResponse.put("error", ex.getReason());
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}
}
