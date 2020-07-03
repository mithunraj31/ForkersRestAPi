package com.mbel.dto;

import java.util.ArrayList;
import java.util.List;

import com.mbel.model.ProductSetModel;
import com.mbel.model.UserEntity;

public class FetchProductSetDto extends ProductDto {
	
	private List<ProductSetModel> products =new ArrayList<>();
	
	
	public void pushProduct(ProductSetModel product) {
		try {
			this.products.add(product);
			
		} catch (Exception e) {
		}
		
	}
	
	
	public List<ProductSetModel> getProducts() {
		return products;
	}

	public void setProducts(List<ProductSetModel> products) {
		this.products = products;
	}
	
}
