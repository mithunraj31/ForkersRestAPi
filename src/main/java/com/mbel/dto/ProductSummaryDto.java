package com.mbel.dto;

public class ProductSummaryDto {

	private int productId;

	private String productName;

	private String description;

	private String obicNo;
	
	private String color;
	
	private int currentQty;
	
	private int totalIncomingQty;
	
	private int totalOutgoingQty;

	

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

	public int getCurrentQty() {
		return currentQty;
	}

	public int getTotalIncomingQty() {
		return totalIncomingQty;
	}

	public int getTotalOutgoingQty() {
		return totalOutgoingQty;
	}

	public void setCurrentQty(int currentQty) {
		this.currentQty = currentQty;
	}

	public void setTotalIncomingQty(int totalIncomingQty) {
		this.totalIncomingQty = totalIncomingQty;
	}

	public void setTotalOutgoingQty(int totalOutgoingQty) {
		this.totalOutgoingQty = totalOutgoingQty;
	}
}
