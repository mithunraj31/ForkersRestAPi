package com.mbel.dto;

import java.util.List;

import com.mbel.model.OutgoingShipment;

public class OutgoingShipmentDto extends OutgoingShipment{

	private List<OutgoingShipmentProductDto> product;

	public List<OutgoingShipmentProductDto> getProduct() {
		return product;
	}

	public void setProduct(List<OutgoingShipmentProductDto> product) {
		this.product = product;
	}



}
