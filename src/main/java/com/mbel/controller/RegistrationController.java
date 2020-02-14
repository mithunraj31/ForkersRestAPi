package com.mbel.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.UserDto;
import com.mbel.serviceImpl.RegistrationServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class RegistrationController {
	
	@Autowired
	RegistrationServiceImpl registrationServiceImpl;
	
	@PostMapping(value="/user/register/")
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserDto user){
        return registrationServiceImpl.register(user);
    }

	 @PostMapping(value="/user/password/")
	    public ResponseEntity<Map<String, String>> updateUserPassword(@RequestBody UpdatePassword newpassword){
	        return registrationServiceImpl.updateUserPassword(newpassword.getOldPassword(),
	        		newpassword.getNewPassword(),newpassword.getConfirmPassword());
	    }

	}

	class UpdatePassword{
		
		private String oldPassword;
		
		private String newPassword;
		
		private String confirmPassword;

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
		
		
		
	}
