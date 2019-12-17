package com.mbel.model;

import java.time.LocalDateTime;

public class OutgoingShipmentModel {	

	private int outgoingShipmentId; 

	private String shipmentNo;
	
	private LocalDateTime shipmentDate;
	
	private Customer salesDestination;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

	private UserEntity user;

	public int getOutgoingShipmentId() {
		return outgoingShipmentId;
	}

	public void setOutgoingShipmentId(int outgoingShipmentId) {
		this.outgoingShipmentId = outgoingShipmentId;
	}


	public Customer getSalesDestination() {
		return salesDestination;
	}

	public void setSalesDestination(Customer salesDestination) {
		this.salesDestination = salesDestination;
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

	public LocalDateTime getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(LocalDateTime shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public String getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
	
	
	
}
