package com.mbel.model;

public class ProductStockCheck {
	
	private Product product;
	
	private int stockQuantity;
	
	private int orderedQuantity;
	
	private int currentQuantity;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public int getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public int getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}




}