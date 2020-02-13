package com.mbel.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
import com.mbel.dao.UserDao;
import com.mbel.dto.UserDto;
import com.mbel.model.UserEntity;

@Service("RegistrationServiceImpl")
public class RegistrationServiceImpl {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	JwtAuthenticationFilter jwt;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;
	
    public UserEntity register(UserDto user) {
		UserEntity newUser = new UserEntity();
	    newUser.setFirstName(user.getFirstName());
	    newUser.setLastName(user.getLastName());
	    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setRoleId(user.getRoleId());
        return userDao.save(newUser);
    }
	
	public ResponseEntity<Map<String, String>> updateUserPassword(String oldPassword, String newPassword, String confirmPassword) {
		Map<String, String> response = new HashMap<>();
		UserEntity newUser = jwt.getUserdetails();
		if(newPassword.equals(confirmPassword)&&!newPassword.equals(oldPassword)) {
			try {
				authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
						newUser.getEmail(),
						oldPassword
						));

				newUser.setPassword(bcryptEncoder.encode(newPassword));
				userDao.save(newUser);
				response.put(Constants.MESSAGE, "New Password has updated");
				response.put(Constants.USER_NAME, newUser.getFirstName());
				return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
			}catch (Exception e){
				response.put(Constants.MESSAGE, "Entered Old Password is wrong.");
				response.put(Constants.USER_NAME, newUser.getFirstName());

				return new ResponseEntity<Map<String,String>>(response, HttpStatus.UNAUTHORIZED);
			}
		}else if(newPassword.equals(oldPassword)){
			response.put(Constants.MESSAGE, "New Password cannot be same as Old Password.");
			response.put(Constants.USER_NAME, newUser.getFirstName());

			return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
			
		}
		else {
			response.put(Constants.MESSAGE, "New Password and Confirm Password doesnt match.");
			response.put(Constants.USER_NAME, newUser.getFirstName());

			return new ResponseEntity<Map<String,String>>(response, null);

		}

	}



}
