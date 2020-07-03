package com.mbel.dto;

import java.util.ArrayList;
import java.util.List;

import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;
import com.mbel.model.UserEntity;

public class FetchProductSetDto extends Product {
	
	private List<ProductSetModel> products =new ArrayList<>();
	
	
	public void pushProduct(ProductSetModel product) {
		try {
			this.products.add(product);
			
		} catch (Exception e) {
		}
		
	}
	
	private UserEntity user;
	
	public List<ProductSetModel> getProducts() {
		return products;
	}

	public void setProducts(List<ProductSetModel> products) {
		this.products = products;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	
	
}
