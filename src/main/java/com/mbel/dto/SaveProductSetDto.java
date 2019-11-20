package com.mbel.dto;

import java.util.List;

import com.mbel.model.Product;

public class SaveProductSetDto extends Product {

	private List<SaveComponentDto> productset;

	public List<SaveComponentDto> getProductset() {
		return productset;
	}

	public void setProductset(List<SaveComponentDto> productset) {
		this.productset = productset;
	}



}
