package com.mbel.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Product")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private int productId;
    
    @NotEmpty(message="* Please Enter productName")
    @Column(name = "product_name")
    private String productName;
    
    @NotEmpty(message="* Please Enter description")
    @Column(name = "description")
    private String description;
    
    @NotNull(message="* Please Enter price")
    @Column(name = "price")
    private double price;
    
    @Column(name = "moq")
    private int moq;
    
    @Column(name = "lead_time")
    private int leadTime;
    
    @NotEmpty(message="* Please Enter obicNo")
    @Column(name = "obic_no")
    private String obicNo;
    
    @Column(name = "qty")
    private int quantity;
    
    @Column(name = "is_set")
    private boolean isSet ;
    
    @Column(name = "active")
    private boolean active;
    
    @Column(name = "created_at_date_time")
    private LocalDateTime createdAtDateTime;
    
    @Column(name = "updated_at_date_time")
    private LocalDateTime updatedAtDateTime;
    
    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "currency")
    private String currency;
    
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public void setCreatedAtDateTime(LocalDateTime localDateTime) {
		this.createdAtDateTime = localDateTime;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
    
}
   