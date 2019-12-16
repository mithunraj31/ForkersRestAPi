package com.mbel.dto;

import java.util.List;

import com.mbel.model.IncomingShipment;

public class IncomingShipmentDto extends IncomingShipment{

	private List<IncomingShipmentProductDto> products;

	public List<IncomingShipmentProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<IncomingShipmentProductDto> products) {
		this.products = products;
	}



}
