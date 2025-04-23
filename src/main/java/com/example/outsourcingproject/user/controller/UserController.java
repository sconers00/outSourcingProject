package com.example.outsourcingproject.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcingproject.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

}
