package com.mbel.model;

import java.util.List;

public class ProductOutgoingShipmentModel{
	
    private int quantity;
	
	private Boolean fixed;
	
	private List<OrderData> orders;


	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<OrderData> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderData> orders) {
		this.orders = orders;
	}

	
	

}

