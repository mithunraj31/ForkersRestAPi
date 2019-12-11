package com.mbel.model;

import java.time.LocalDateTime;

public class OutgoingShipmentModel {	

	private int outgoingShipmentId; 

	private int productId;
	
	private LocalDateTime shipDate;
	
	private int quantity;
	
	private String destination;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

	private UserEntity user;

	public int getOutgoingShipmentId() {
		return outgoingShipmentId;
	}

	public void setOutgoingShipmentId(int outgoingShipmentId) {
		this.outgoingShipmentId = outgoingShipmentId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public LocalDateTime getShipDate() {
		return shipDate;
	}

	public void setShipDate(LocalDateTime shipDate) {
		this.shipDate = shipDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
	
}
