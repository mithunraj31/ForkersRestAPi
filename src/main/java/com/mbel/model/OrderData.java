package com.mbel.model;

public class OrderData {
	
	private int orderId;
	
	private String customer;

	private boolean fixed;
	
	private boolean delayed;
	
	private int quantity;
	
	private String proposalNo;
	
	private boolean fulfilled;
	
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

	public boolean isFulfilled() {
		return fulfilled;
	}

	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	public boolean isDelayed() {
		return delayed;
	}

	public void setDelayed(boolean delayed) {
		this.delayed = delayed;
	}





	

}
