package com.mbel.dto;

import java.util.List;

import com.mbel.model.IncomingShipment;

public class IncomingShipmentDto extends IncomingShipment{

	private List<IncomingShipmentProductDto> product;

	public List<IncomingShipmentProductDto> getProduct() {
		return product;
	}

	public void setProduct(List<IncomingShipmentProductDto> product) {
		this.product = product;
	}


}
