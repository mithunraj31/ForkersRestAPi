package com.mbel.dto;

import java.util.List;

import com.mbel.model.ForecastModel;
import com.mbel.model.ProductStockCheck;

public class ForecastProductDto extends ForecastModel{
	
	List<ProductStockCheck> orderedProducts;

	public List<ProductStockCheck> getOrderedProducts() {
		return orderedProducts;
	}

	public void setOrderedProducts(List<ProductStockCheck> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}
	
}