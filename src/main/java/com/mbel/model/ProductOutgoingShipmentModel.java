package com.mbel.model;

import java.util.List;

public class ProductOutgoingShipmentModel{
	
    private int quantity;
	
	private Boolean fixed;
	
	private int fulfilled;
	
	private Boolean delayed;
	
	private ColourData contains;
	
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

	public int getFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(int fulfilled) {
		this.fulfilled = fulfilled;
	}

	public Boolean getDelayed() {
		return delayed;
	}

	public void setDelayed(Boolean delayed) {
		this.delayed = delayed;
	}

	public ColourData getContains() {
		return contains;
	}

	public void setContains(ColourData contains) {
		this.contains = contains;
	}



}

