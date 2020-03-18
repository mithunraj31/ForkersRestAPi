package com.mbel.model;

import java.time.LocalDateTime;

/**
 * @author mithunraj
 *
 */
public class IncomingShipmentModel {	

	private int incomingShipmentId; 
	
	private String shipmentNo; 

	private LocalDateTime fixedDeliveryDate;

	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;

	private UserEntity user;
	
	private boolean arrived;
	
	private boolean fixed;
	
	private String branch;
	
	private String vendor;
	
	private LocalDateTime orderDate;
	
	private int pendingQty;
	
	private LocalDateTime desiredDeliveryDate;
	
	private int confirmedQty;
	
	private boolean partial;
	
	private String currency;


	public int getIncomingShipmentId() {
		return incomingShipmentId;
	}
	

	public void setIncomingShipmentId(int incomingShipmentId) {
		this.incomingShipmentId = incomingShipmentId;
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


	public boolean isArrived() {
		return arrived;
	}


	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}


	public LocalDateTime getFixedDeliveryDate() {
		return fixedDeliveryDate;
	}


	public void setFixedDeliveryDate(LocalDateTime fixedDeliveryDate) {
		this.fixedDeliveryDate = fixedDeliveryDate;
	}


	public boolean isFixed() {
		return fixed;
	}


	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}


	public String getBranch() {
		return branch;
	}


	public void setBranch(String branch) {
		this.branch = branch;
	}


	public String getVendor() {
		return vendor;
	}


	public void setVendor(String vendor) {
		this.vendor = vendor;
	}


	public LocalDateTime getOrderDate() {
		return orderDate;
	}


	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}


	public int getPendingQty() {
		return pendingQty;
	}


	public void setPendingQty(int pendingQty) {
		this.pendingQty = pendingQty;
	}


	public LocalDateTime getDesiredDeliveryDate() {
		return desiredDeliveryDate;
	}


	public void setDesiredDeliveryDate(LocalDateTime desiredDeliveryDate) {
		this.desiredDeliveryDate = desiredDeliveryDate;
	}


	public int getConfirmedQty() {
		return confirmedQty;
	}


	public void setConfirmedQty(int confirmedQty) {
		this.confirmedQty = confirmedQty;
	}


	public boolean isPartial() {
		return partial;
	}


	public void setPartial(boolean partial) {
		this.partial = partial;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}
	

}
