package com.mbel.dto;

import com.mbel.model.Product;
import com.mbel.model.UserEntity;

public class ProductDto   {
	
	private Product product;
	
	private UserEntity user;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
