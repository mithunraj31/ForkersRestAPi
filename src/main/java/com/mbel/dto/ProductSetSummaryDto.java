package com.mbel.dto;

import java.util.List;

public class ProductSetSummaryDto {

	private int productId;

	private String productName;

	private String description;

	private String obicNo;
	
	private String color;
	
	private int totalOutgoingQty;
	
	private List<ProductSummaryDto> product;

	

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTotalOutgoingQty() {
		return totalOutgoingQty;
	}

	public void setTotalOutgoingQty(int totalOutgoingQty) {
		this.totalOutgoingQty = totalOutgoingQty;
	}

	public List<ProductSummaryDto> getProduct() {
		return product;
	}

	public void setProduct(List<ProductSummaryDto> product) {
		this.product = product;
	}


}
