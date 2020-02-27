package com.mbel.dto;

import java.util.List;

public class ProductPredictionDto {

	private int productId;

	private String productName;

	private String description;

	private String obicNo;

	List<ProductDataDto>products;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObicNo() {
		return obicNo;
	}

	public void setObicNo(String obicNo) {
		this.obicNo = obicNo;
	}

	public List<ProductDataDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDataDto> products) {
		this.products = products;
	}
}
