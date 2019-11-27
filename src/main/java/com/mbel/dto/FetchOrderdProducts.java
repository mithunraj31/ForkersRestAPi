package com.mbel.dto;

import com.mbel.model.OrderProductModel;
import com.mbel.model.ProductSetModel;

public class FetchOrderdProducts   {
	
	
	
	private OrderProductModel products;
	
	private ProductSetModel product;
	


	public ProductSetModel getProduct() {
		return product;
	}

	public void setProduct(ProductSetModel product) {
		this.product = product;
	}

	public OrderProductModel getProducts() {
		return products;
	}

	public void setProducts(OrderProductModel products) {
		this.products = products;
	}

	
	
	
}
