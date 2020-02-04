package com.mbel.dto;

import java.util.List;

import com.mbel.model.ForecastModel;

public class ForecastProductDto extends ForecastModel{
	
	List<FetchProductSetDto> orderedProducts;

	public List<FetchProductSetDto> getOrderedProducts() {
		return orderedProducts;
	}

	public void setOrderedProducts(List<FetchProductSetDto> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}


	
}