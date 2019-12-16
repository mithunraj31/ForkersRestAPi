package com.mbel.model;

import java.time.LocalDateTime;

/**
 * @author mithunraj
 *
 */
public class IncomingShipmentModel {	

	private int incomingShipmentId; 
	
	private String shipmentNo; 

	private LocalDateTime arrivalDate;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

	private UserEntity user;


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


	public String getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}

	
	
	

}
