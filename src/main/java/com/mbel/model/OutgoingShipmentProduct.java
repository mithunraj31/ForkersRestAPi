package com.mbel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="outgoing_shipment_product")
public class OutgoingShipmentProduct {	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "outgoing_shipment_product_id")
	private int outgoingShipmentProductId; 

	@NotNull(message="* Please Enter outgoingShipmentId")
	@Column(name = "outgoing_shipment__id")
	private int outgoingShipmentId;

	@NotNull(message="* Please Enter ProductId")
	@Column(name = "product_id")
	private int productId;

	@Column(name = "qty")
	private int  quantity;

	public int getOutgoingShipmentProductId() {
		return outgoingShipmentProductId;
	}

	public void setOutgoingShipmentProductId(int outgoingShipmentProductId) {
		this.outgoingShipmentProductId = outgoingShipmentProductId;
	}


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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



}
