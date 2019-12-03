package com.mbel.dto;

import java.util.List;

import com.mbel.model.Product;

public class SaveProductSetDto extends Product {

	private List<SaveComponentDto> products;

	public List<SaveComponentDto> getProducts() {
		return products;
	}

	public void setProducts(List<SaveComponentDto> products) {
		this.products = products;
	}




}
