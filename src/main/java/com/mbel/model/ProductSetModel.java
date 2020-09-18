package com.mbel.model;

import java.time.LocalDateTime;

public class ProductSetModel {
	
	private Product product;
	
	private int quantity;
	
	private int currentQuantity;
	
	private int requiredQuantity;
	
	private boolean forecast;
	
	private LocalDateTime mod;

	public ProductSetModel() {

	}

	public ProductSetModel(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public int getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setRequiredQuantity(int requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}

	public boolean isForecast() {
		return forecast;
	}

	public void setForecast(boolean forecast) {
		this.forecast = forecast;
	}

	}



