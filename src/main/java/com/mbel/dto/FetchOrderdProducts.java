package com.mbel.dto;

import java.time.LocalDateTime;

public class FetchOrderdProducts   {
	
	private FetchProductSetDto product;
	
	private int quantity;
	

	private int currentQuantity;
	
    private int requiredQuantity;
	
	private boolean forecast;
	
	private LocalDateTime mod;



	public FetchProductSetDto getProduct() {
		return product;
	}

	public void setProduct(FetchProductSetDto product) {
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

	public LocalDateTime getMod() {
		return mod;
	}

	public void setMod(LocalDateTime mod) {
		this.mod = mod;
	}




	
	
	
}
