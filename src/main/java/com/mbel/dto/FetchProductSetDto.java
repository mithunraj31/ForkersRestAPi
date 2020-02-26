package com.mbel.dto;

import java.util.List;

import com.mbel.model.PredictionData;
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

	private List<PredictionData> data;

	public List<PredictionData> getData() {
		return data;
	}

	public void setData(List<PredictionData> data) {
		this.data = data;
	}
	
	
}
