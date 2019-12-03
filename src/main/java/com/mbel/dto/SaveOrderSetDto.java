package com.mbel.dto;

import java.util.List;

import com.mbel.model.Order;

public class SaveOrderSetDto extends Order {

	private List<SaveOrderComponentDto> orderedProducts;

	public List<SaveOrderComponentDto> getOrderedProducts() {
		return orderedProducts;
	}

	public void setOrderedProducts(List<SaveOrderComponentDto> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}




}
