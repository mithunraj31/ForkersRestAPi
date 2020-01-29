package com.mbel.model;

import java.time.LocalDateTime;

public class ProductStockCheck {
	
	private Product product;
	
	private int quantity;
	
	private int currentQuantity;
	
	private LocalDateTime mod;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public LocalDateTime getMod() {
		return mod;
	}

	public void setMod(LocalDateTime mod) {
		this.mod = mod;
	}





}