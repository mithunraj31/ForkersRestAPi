package com.mbel.dto;

public class SaveComponentDto {


	private int quantity;

	private int productId; 

	private int  productComponentId;


	public int getQuantity() {
		return quantity;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getProductComponentId() {
		return productComponentId;
	}
	public void setProductComponentId(int productComponentId) {
		this.productComponentId = productComponentId;
	}
}
