package com.mbel.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.config.TokenProvider;
import com.mbel.constants.Constants;

import io.jsonwebtoken.Claims;

@Service(value = "UtilityServiceImpl")
public class UtilityServiceImpl {
    @Autowired
    private TokenProvider tokenProvider;
    
    /**
	 * get user value from JWY payload
	 * @param token JWT in HTTP request headers
	 * @return String value of user.
	 */
	public int getUserIdFromToken(String token) {
		// remove text "Bearer " from header value
		token = token.replace(Constants.TOKEN_PREFIX, "");
		// get payload as Claims object
		Claims user = tokenProvider.getAllClaimsFromToken(token);
		// get value by object key
		return user.get("userId", Integer.class);
	}
	
}