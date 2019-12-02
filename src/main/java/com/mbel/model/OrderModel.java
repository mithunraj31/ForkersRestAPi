package com.mbel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderModel{
	
	    private int OrderId;
	     
	    private String salesRepresentative;
	    
	    private LocalDate dueDate;
	    
	    private LocalDateTime receivedDate;
	    
	    private String proposalNo;
	    
	    private LocalDateTime createdAt;
	    
	    private LocalDateTime updatedAt;
	    
	    private UserEntity userId;
	    
	    private UserEntity salesUserId;
	    
	    private String editReason;
	    
	    private boolean active;
	    
	    private boolean forecast;
	    
	    
	    
		public int getOrderId() {
			return OrderId;
		}
		public void setOrderId(int orderId) {
			OrderId = orderId;
		}
		public String getSalesRepresentative() {
			return salesRepresentative;
		}
		public void setSalesRepresentative(String salesRepresentative) {
			this.salesRepresentative = salesRepresentative;
		}
		public LocalDate getDueDate() {
			return dueDate;
		}
		public void setDueDate(LocalDate dueDate) {
			this.dueDate = dueDate;
		}
		public LocalDateTime getReceivedDate() {
			return receivedDate;
		}
		public void setReceivedDate(LocalDateTime receivedDate) {
			this.receivedDate = receivedDate;
		}
		public String getProposalNo() {
			return proposalNo;
		}
		public void setProposalNo(String proposalNo) {
			this.proposalNo = proposalNo;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}

		
		public UserEntity getUserId() {
			return userId;
		}
		public void setUserId(UserEntity userId) {
			this.userId = userId;
		}
		public UserEntity getSalesUserId() {
			return salesUserId;
		}
		public void setSalesUserId(UserEntity salesUserId) {
			this.salesUserId = salesUserId;
		}
		public String getEditReason() {
			return editReason;
		}
		public void setEditReason(String editReason) {
			this.editReason = editReason;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public boolean isForecast() {
			return forecast;
		}
		public void setForecast(boolean forecast) {
			this.forecast = forecast;
		}
	    
	
	
}