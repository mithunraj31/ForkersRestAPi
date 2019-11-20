package com.mbel.dto;

public class SaveComponentDto  {


	private int productSetId; 
	
	private int setId;

	private int qty;

	private int  productComponentId;

	public int getProductSetId() {
		return productSetId;
	}

	public void setProductSetId(int productSetId) {
		this.productSetId = productSetId;
	}

	public int getSetId() {
		return setId;
	}

	public void setSetId(int setId) {
		this.setId = setId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getProductComponentId() {
		return productComponentId;
	}

	public void setProductComponentId(int productComponentId) {
		this.productComponentId = productComponentId;
	}
	
	
}
