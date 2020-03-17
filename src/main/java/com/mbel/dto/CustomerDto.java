package com.mbel.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerDto {
	
	private int customerId; 

	private String customerName;

	private String zip;

	private String address;

	private String tel;

	private String contactName;

	private List<String> type;

	private LocalDateTime createdAtDateTime;

	private LocalDateTime updatedAtDateTime;

	private int userId;

	private boolean active;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}


	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


}
