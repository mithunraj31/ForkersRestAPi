package com.mbel.dto;

import java.util.List;

import com.mbel.model.IncomingShipmentModel;

public class PopulateIncomingShipmentDto extends IncomingShipmentModel {
	
	private List<FetchOrderdProducts> products;

	public List<FetchOrderdProducts> getProducts() {
		return products;
	}

	public void setProducts(List<FetchOrderdProducts> products) {
		this.products = products;
	}
	

	
}
