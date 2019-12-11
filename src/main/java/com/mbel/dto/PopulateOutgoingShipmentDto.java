package com.mbel.dto;

import java.util.List;

import com.mbel.model.OutgoingShipmentModel;

public class PopulateOutgoingShipmentDto extends OutgoingShipmentModel {
	
	private List<FetchOrderdProducts> products;

	public List<FetchOrderdProducts> getProducts() {
		return products;
	}

	public void setProducts(List<FetchOrderdProducts> products) {
		this.products = products;
	}
	

	
}
