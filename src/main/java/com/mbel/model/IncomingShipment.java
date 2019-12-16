package com.mbel.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="incoming_shipment")
public class IncomingShipment {	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "incoming_shipment_id")
	private int incomingShipmentId; 
	
	@NotNull(message="* Please Enter shipmentNo ")
	@Column(name = "shipment_no")
	private String shipmentNo;

	@NotNull(message="* Please Enter Arrival Date")
	@Column(name = "arrival_date")
	private LocalDateTime arrivalDate;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "incoming_shipment_incoming_shipment_id")
	private int incomingShipmentIncomingShipmentId;

	public int getIncomingShipmentId() {
		return incomingShipmentId;
	}

	public void setIncomingShipmentId(int incomingShipmentId) {
		this.incomingShipmentId = incomingShipmentId;
	}

	public String getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getIncomingShipmentIncomingShipmentId() {
		return incomingShipmentIncomingShipmentId;
	}

	public void setIncomingShipmentIncomingShipmentId(int incomingShipmentIncomingShipmentId) {
		this.incomingShipmentIncomingShipmentId = incomingShipmentIncomingShipmentId;
	}
	
	
	
	

}
