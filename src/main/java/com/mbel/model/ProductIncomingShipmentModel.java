package com.mbel.model;

import java.util.List;

public class ProductIncomingShipmentModel{
	
    private int quantity;
	
	private Boolean fixed;
	
	private int fulfilled;
	
	private String incomingColor;

	private List<IncomingOrderData> incomingOrders;
	
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


	public List<IncomingOrderData> getIncomingOrders() {
		return incomingOrders;
	}

	public void setIncomingOrders(List<IncomingOrderData> incomingOrders) {
		this.incomingOrders = incomingOrders;
	}

	public int getFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(int fulfilled) {
		this.fulfilled = fulfilled;
	}

	public String getIncomingColor() {
		return incomingColor;
	}

	public void setIncomingColor(String incomingColor) {
		this.incomingColor = incomingColor;
	}

}

