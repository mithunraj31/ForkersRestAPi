package com.mbel.model;

import java.util.List;

public class OrderProductModel  {
	
	private List<ProductSetModel> product;
	
	private int quantity;


	public List<ProductSetModel> getProduct() {
		return product;
	}

	public void setProduct(List<ProductSetModel> product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}




}