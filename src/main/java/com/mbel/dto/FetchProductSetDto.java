package com.mbel.dto;

import java.util.ArrayList;
import java.util.List;

import com.mbel.model.ProductSetModel;

public class FetchProductSetDto extends ProductDto {
	
	private List<ProductSetModel> products =new ArrayList<>();
	
	
	public FetchProductSetDto() {

	}

	public FetchProductSetDto(List<ProductSetModel> products) {
		this.products = products;
	}

	public FetchProductSetDto(List<ProductSetModel> products, boolean isDisplay) {
		this.products = products;
		this.setDisplay(isDisplay);
	}

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
