package com.mbel.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.model.UserEntity;
import com.mbel.serviceImpl.UserServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  UserController{
	@Autowired
	private UserServiceImpl userServiceImpl;   
	


	@GetMapping("/user/")
	public List<UserEntity> allUsers() {
		return userServiceImpl.findAll();
	}

	@GetMapping("/user/{userId}")
	public Optional<UserEntity> userById(@PathVariable (value="userId")int userId) {
		return userServiceImpl.findById(userId);

	}

	@PutMapping("/user/{userId}")
	public UserEntity updateUserById(@PathVariable (value="userId")int userId,
			@Valid @RequestBody UserEntity userEntity) {
		return userServiceImpl.getupdateUserById(userId,userEntity);


	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable (value="userId")int userId) {
		return userServiceImpl.deleteUserById(userId);

	}
	

}



