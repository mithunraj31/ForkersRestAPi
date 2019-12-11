package com.mbel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="incoming_shipment_product")
public class IncomingShipmentProduct {	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "incoming_shipment_product_id")
	private int incomingShipmentProductId; 

	@NotNull(message="* Please Enter incomingShipmentId")
	@Column(name = "incoming_shipment_id")
	private int incomingShipmentId;

	@NotNull(message="* Please Enter ProductId")
	@Column(name = "product_id")
	private int productId;

	@NotNull(message="* Please Enter Quantity")
	@Column(name = "qty")
	private int  quantity;
	
	@NotNull(message="* Please Enter Price")
	@Column(name = "price")
	private double price;

	public int getIncomingShipmentProductId() {
		return incomingShipmentProductId;
	}

	public void setIncomingShipmentProductId(int incomingShipmentProductId) {
		this.incomingShipmentProductId = incomingShipmentProductId;
	}

	public int getIncomingShipmentId() {
		return incomingShipmentId;
	}

	public void setIncomingShipmentId(int incomingShipmentId) {
		this.incomingShipmentId = incomingShipmentId;
	}

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
	
	
	
	


}
