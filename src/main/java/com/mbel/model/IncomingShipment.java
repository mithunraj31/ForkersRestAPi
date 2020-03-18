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

	@Column(name = "fixed_delivery_date")
	private LocalDateTime fixedDeliveryDate;

	@NotNull(message="* Please Enter ProductId")
	@Column(name = "product_id")
	private int productId;

	@NotNull(message="* Please Enter Quantity")
	@Column(name = "qty")
	private int  quantity;

	@NotNull(message="* Please Enter Price")
	@Column(name = "price")
	private double price;

	@Column(name = "currency")
	private String currency;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "arrived")
	private boolean arrived;

	@Column(name = "fixed")
	private boolean fixed;

	@Column(name = "partial")
	private boolean partial;

	@Column(name = "branch")
	private String branch;

	@Column(name = "vendor")
	private String vendor;

	@Column(name = "order_date")
	private LocalDateTime orderDate;

	@Column(name = "pending_qty")
	private int pendingQty;

	@Column(name = "desired_delivery_date")
	private LocalDateTime desiredDeliveryDate;

	@Column(name = "confirmed_qty")
	private int confirmedQty;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


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
