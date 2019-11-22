package com.mbel.dto;

import java.util.List;

import com.mbel.model.Order;

public class SaveOrderSetDto extends Order {

	private List<SaveOrderComponentDto> productset;

	public List<SaveOrderComponentDto> getProductset() {
		return productset;
	}

	public void setProductset(List<SaveOrderComponentDto> productset) {
		this.productset = productset;
	}



}
