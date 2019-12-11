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

	@NotNull(message="* Please Enter incomingShipmentId")
	@Column(name = "outgoing_shipment_outgoing_shipment_product_id")
	private int outgoingShipmentOutgoingShipmentProductId;

	@NotNull(message="* Please Enter ProductId")
	@Column(name = "product_product_id")
	private int productProductId;

	@Column(name = "qty")
	private int  quantity;

	public int getOutgoingShipmentProductId() {
		return outgoingShipmentProductId;
	}

	public void setOutgoingShipmentProductId(int outgoingShipmentProductId) {
		this.outgoingShipmentProductId = outgoingShipmentProductId;
	}

	public int getOutgoingShipmentOutgoingShipmentProductId() {
		return outgoingShipmentOutgoingShipmentProductId;
	}

	public void setOutgoingShipmentOutgoingShipmentProductId(int outgoingShipmentOutgoingShipmentProductId) {
		this.outgoingShipmentOutgoingShipmentProductId = outgoingShipmentOutgoingShipmentProductId;
	}

	public int getProductProductId() {
		return productProductId;
	}

	public void setProductProductId(int productProductId) {
		this.productProductId = productProductId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



}
