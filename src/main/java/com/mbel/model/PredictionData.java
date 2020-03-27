package com.mbel.model;

import java.time.LocalDateTime;

public class PredictionData {
	
	private LocalDateTime date;
	
	private int currentQuantity;
	
	private int quantity;
	
	ProductIncomingShipmentModel incoming;
	
	ProductOutgoingShipmentModel outgoing;



	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ProductIncomingShipmentModel getIncoming() {
		return incoming;
	}

	public void setIncoming(ProductIncomingShipmentModel incoming) {
		this.incoming = incoming;
	}

	public ProductOutgoingShipmentModel getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(ProductOutgoingShipmentModel outgoing) {
		this.outgoing = outgoing;
	}


}
