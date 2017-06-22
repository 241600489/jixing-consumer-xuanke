package com.xuanke.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuanke.entity.User;
import com.xuanke.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
		@GetMapping(value="/test")
		public User test(){
			return userService.findUserById(1); 
		}
}
