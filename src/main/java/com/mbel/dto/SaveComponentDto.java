package com.mbel.dto;

import java.util.List;

import com.mbel.model.Product;

public class SaveComponentDto extends Product {


	private int productSetId; 
	
	private int setId;

	private int qty;

	private int  productComponentId;
	
	private List<Product> product; 

	public int getProductSetId() {
		return productSetId;
	}

	public void setProductSetId(int productSetId) {
		this.productSetId = productSetId;
	}

	public int getSetId() {
		return setId;
	}

	public void setSetId(int setId) {
		this.setId = setId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getProductComponentId() {
		return productComponentId;
	}

	public void setProductComponentId(int productComponentId) {
		this.productComponentId = productComponentId;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}


}
