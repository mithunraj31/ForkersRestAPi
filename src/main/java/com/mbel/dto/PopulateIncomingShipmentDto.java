package com.mbel.dto;

import java.util.List;

import com.mbel.model.IncomingShipmentModel;

public class PopulateIncomingShipmentDto extends IncomingShipmentModel {
	
	private List<FetchIncomingOrderdProducts> products;

	public List<FetchIncomingOrderdProducts> getProducts() {
		return products;
	}

	public void setProducts(List<FetchIncomingOrderdProducts> products) {
		this.products = products;
	}
	

	
}
