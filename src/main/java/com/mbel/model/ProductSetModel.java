package com.mbel.model;

import java.time.LocalDateTime;

import com.mbel.model.Component;

public class ProductSetModel {
	
	private int productSetId; 
	
	private int setId;
	
	private int quantity;
	
	private int  productComponentId;
	
	private int productId;
	
    private String productName;
    
    private String description;
    
    private double price;
    
    private int moq;
    
    private int leadTime;
    
    private String obicNo;
    
    private int qty;
    
    private boolean isSet ;
    
    private boolean active;
    
    private LocalDateTime createdAtDateTime;
    
    private LocalDateTime updatedAtDateTime;
    
    private int userId;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getProductComponentId() {
		return productComponentId;
	}

	public void setProductComponentId(int productComponentId) {
		this.productComponentId = productComponentId;
	}

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMoq() {
		return moq;
	}

	public void setMoq(int moq) {
		this.moq = moq;
	}

	public int getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(int leadTime) {
		this.leadTime = leadTime;
	}

	public String getObicNo() {
		return obicNo;
	}

	public void setObicNo(String obicNo) {
		this.obicNo = obicNo;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public boolean isSet() {
		return isSet;
	}

	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedAtDateTime() {
		return createdAtDateTime;
	}

	public void setCreatedAtDateTime(LocalDateTime createdAtDateTime) {
		this.createdAtDateTime = createdAtDateTime;
	}

	public LocalDateTime getUpdatedAtDateTime() {
		return updatedAtDateTime;
	}

	public void setUpdatedAtDateTime(LocalDateTime updatedAtDateTime) {
		this.updatedAtDateTime = updatedAtDateTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
    
	public ProductSetModel() {
		
	}
 
	
}
