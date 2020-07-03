package com.mbel.dto;

import com.mbel.model.Product;
import com.mbel.model.UserEntity;

public class ProductDto extends Product   {
	
	private UserEntity user;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}


}
