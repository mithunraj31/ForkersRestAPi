package com.mbel.dto;

import com.mbel.model.IncomingShipmentModel;

public class FetchIncomingOrderdProducts extends IncomingShipmentModel  {
	
	private FetchProductSetDto product;
	
	private int quantity;
	
	private double price;



	public FetchProductSetDto getProduct() {
		return product;
	}

	public void setProduct(FetchProductSetDto product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}




	
	
	
}
