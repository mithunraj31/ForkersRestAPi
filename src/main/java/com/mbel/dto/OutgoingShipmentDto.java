package com.mbel.dto;

import java.util.List;

import com.mbel.model.OutgoingShipment;

public class OutgoingShipmentDto extends OutgoingShipment{

	private List<OutgoingShipmentProductDto> products;

	public List<OutgoingShipmentProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<OutgoingShipmentProductDto> products) {
		this.products = products;
	}




}
