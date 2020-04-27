package com.mbel.model;

public class IncomingOrderData {
	
	private int incomingshipmentId;
	
	private String shipmentNo;

	private boolean fixed;
	
	private int quantity;
	
	private boolean fulfilled;

	public int getIncomingshipmentId() {
		return incomingshipmentId;
	}

	public String getShipmentNo() {
		return shipmentNo;
	}

	public boolean isFixed() {
		return fixed;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setIncomingshipmentId(int incomingshipmentId) {
		this.incomingshipmentId = incomingshipmentId;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	

}