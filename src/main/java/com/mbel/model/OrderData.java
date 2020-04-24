package com.mbel.model;

public class OrderData {
	
	private int orderId;
	
	private String customer;

	private boolean fixed;
	
	private int quantity;
	
	private String proposalNo;
	
	private boolean fulfiled;
	
	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCustomer() {
		return customer;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isFulfiled() {
		return fulfiled;
	}

	public void setFulfiled(boolean fulfiled) {
		this.fulfiled = fulfiled;
	}




	

}
