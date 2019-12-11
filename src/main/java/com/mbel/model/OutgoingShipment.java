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

	@NotNull(message="* Please Enter Product Id")
	@Column(name = "product_id")
	private int productId;

	@NotNull(message="* Please Enter Ship Date")
	@Column(name = "ship_date")
	private LocalDateTime shipDate;
	
	@NotNull(message="* Please Enter Quantity")
	@Column(name = "qty")
	private int quantity;
	
	@NotNull(message="* Please Enter Destination")
	@Column(name = "destination")
	private String destination;

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}



}
