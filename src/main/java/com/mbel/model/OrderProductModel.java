package com.mbel.model;

import java.util.List;

public class OrderProductModel extends Product {
	
	private List<ProductSetModel> product;
	
	private int amount;



	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public List<ProductSetModel> getProduct() {
		return product;
	}

	public void setProduct(List<ProductSetModel> product) {
		this.product = product;
	}




}