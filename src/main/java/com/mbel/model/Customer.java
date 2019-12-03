package com.mbel.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	private int customerId; 

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "zip")
	private String zip;

	@Column(name = "address")
	private String address;

	@Column(name = "tel")
	private String tel;

	@Column(name = "contact_name")
	private String contactName;

	@Column(name = "type")
	private String type;

	@Column(name = "created_at_date_time")
	private LocalDateTime createdAtDateTime;

	@Column(name = "updated_at_date_time")
	private LocalDateTime updatedAtDateTime;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "active")
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
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
