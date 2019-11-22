package com.mbel.dto;

public class SaveOrderComponentDto {

	private int productSetId;
	private int productcomponentId;
	private int qty;


	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getProductcomponentId() {
		return productcomponentId;
	}

	public void setProductcomponentId(int productcomponentId) {
		this.productcomponentId = productcomponentId;
	}

	public int getProductSetId() {
		return productSetId;
	}

	public void setProductSetId(int productSetId) {
		this.productSetId = productSetId;
	}




}
