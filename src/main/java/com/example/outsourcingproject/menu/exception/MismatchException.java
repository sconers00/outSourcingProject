package com.example.outsourcingproject.menu.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class MismatchException extends ResponseStatusException {

	public MismatchException(HttpStatusCode status, String reason) {
		super(status, reason);
	}
}
