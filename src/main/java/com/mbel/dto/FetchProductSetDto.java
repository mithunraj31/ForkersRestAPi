package com.mbel.dto;

import java.util.List;

import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;

public class FetchProductSetDto extends Product {
	
	private List<ProductSetModel> products;

	public List<ProductSetModel> getProducts() {
		return products;
	}

	public void setProducts(List<ProductSetModel> products) {
		this.products = products;
	}

	
	
	
}
