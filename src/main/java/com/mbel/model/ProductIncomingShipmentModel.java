package com.mbel.model;

public class ProductIncomingShipmentModel{
	
    private int quantity;
	
	private Boolean fixed;


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
}

