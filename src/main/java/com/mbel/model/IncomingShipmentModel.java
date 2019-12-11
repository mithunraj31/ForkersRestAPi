package com.mbel.model;

import java.time.LocalDateTime;

public class IncomingShipmentModel {	

	private int incomingShipmentId; 

	private LocalDateTime arrivalDate;

	private int productId;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

	private UserEntity userId;

	private int incomingShipmentIncomingShipmentId;

	public int getIncomingShipmentId() {
		return incomingShipmentId;
	}

	public void setIncomingShipmentId(int incomingShipmentId) {
		this.incomingShipmentId = incomingShipmentId;
	}

	public LocalDateTime getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDateTime arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public UserEntity getUserId() {
		return userId;
	}

	public void setUserId(UserEntity userId) {
		this.userId = userId;
	}

	public int getIncomingShipmentIncomingShipmentId() {
		return incomingShipmentIncomingShipmentId;
	}

	public void setIncomingShipmentIncomingShipmentId(int incomingShipmentIncomingShipmentId) {
		this.incomingShipmentIncomingShipmentId = incomingShipmentIncomingShipmentId;
	}

	
	
	

}
