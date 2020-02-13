package com.mbel.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
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
	

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user/")
	public List<UserEntity> allUsers() {
		return userServiceImpl.findAll();
	}

	@GetMapping("/user/{userId}")
	public Optional<UserEntity> userById(@PathVariable (value="userId")@NonNull int userId) {
		return userServiceImpl.findById(userId);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/user/{userId}")
	public UserEntity updateUserById(@PathVariable (value="userId")@NonNull int userId,
			@Valid @RequestBody UserEntity userEntity) {
		return userServiceImpl.getupdateUserById(userId,userEntity);


	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable (value="userId")@NonNull int userId) {
		return userServiceImpl.deleteUserById(userId);

	}
	

}



