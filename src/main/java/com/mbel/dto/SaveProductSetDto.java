package com.mbel.dto;

import com.mbel.model.Product;

public class SaveProductSetDto extends Product {
	
	private  SaveComponentDto products;
	

	public SaveComponentDto getProducts() {
		return products;
	}

	public void setProducts(SaveComponentDto products) {
		this.products = products;
	}
	
}
