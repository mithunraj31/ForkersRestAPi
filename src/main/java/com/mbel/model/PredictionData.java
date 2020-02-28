package com.mbel.model;

import java.time.LocalDateTime;

public class PredictionData {
	
	private LocalDateTime date;
	
	private int currentQuantity;
	
	private int requiredQuantity;
	
	private int incomingQuantity;
	
	private int quantity;

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public int getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setRequiredQuantity(int requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}

	public int getIncomingQuantity() {
		return incomingQuantity;
	}

	public void setIncomingQuantity(int incomingQuantity) {
		this.incomingQuantity = incomingQuantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	

}
