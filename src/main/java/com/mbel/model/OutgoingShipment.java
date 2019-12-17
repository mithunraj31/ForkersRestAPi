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
@Table(name="outgoing_shipment")
public class OutgoingShipment {	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "outgoing_shipment_id")
	private int outgoingShipmentId; 

	@NotNull(message="* Please Enter shipmentNo ")
	@Column(name = "shipment_no")
	private String shipmentNo;

	@NotNull(message="* Please Enter Shipment Date")
	@Column(name = "shipment_date")
	private LocalDateTime shipmentDate;
	
	
	@NotNull(message="* Please Enter Sales Destination")
	@Column(name = "sales_destination_id")
	private int salesDestinationId;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "user_id")
	private int userId;

	public int getOutgoingShipmentId() {
		return outgoingShipmentId;
	}

	public void setOutgoingShipmentId(int outgoingShipmentId) {
		this.outgoingShipmentId = outgoingShipmentId;
	}



	public String getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}

	public LocalDateTime getShipmentDate() {
		return shipmentDate;
	}

	public void setShipmentDate(LocalDateTime shipmentDate) {
		this.shipmentDate = shipmentDate;
	}

	public int getSalesDestinationId() {
		return salesDestinationId;
	}

	public void setSalesDestinationId(int salesDestinationId) {
		this.salesDestinationId = salesDestinationId;
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



}
