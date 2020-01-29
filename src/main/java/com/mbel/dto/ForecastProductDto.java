package com.mbel.dto;

import java.util.List;

import com.mbel.model.ForecastModel;
import com.mbel.model.ProductSetModel;
import com.mbel.model.ProductStockCheck;

public class ForecastProductDto extends ForecastModel{
	
	List<FetchProductSetDto> orderedProducts;

	public List<FetchProductSetDto> getOrderedProducts() {
		return orderedProducts;
	}

	public void setOrderedProducts(List<FetchProductSetDto> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}


	
}