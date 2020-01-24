package com.mbel.model;

import java.time.LocalDateTime;

public class OrderModel{
	
	    private int orderId;
	     
	    private LocalDateTime dueDate;
	    
	    private LocalDateTime receivedDate;
	    
	    private String proposalNo;
	    
	    private LocalDateTime createdAt;
	    
	    private LocalDateTime updatedAt;
	    
	    private UserEntity user;
	    
	    private UserEntity salesUser;
	    
	    private String editReason;
	    
	    private boolean active;
	    
	    private boolean forecast;
	    
	    private boolean fullfilled;

		public int getOrderId() {
			return orderId;
		}

		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}

		public LocalDateTime getDueDate() {
			return dueDate;
		}

		public void setDueDate(LocalDateTime dueDate) {
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

		public UserEntity getUser() {
			return user;
		}

		public void setUser(UserEntity user) {
			this.user = user;
		}

		public UserEntity getSalesUser() {
			return salesUser;
		}

		public void setSalesUser(UserEntity salesUser) {
			this.salesUser = salesUser;
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

		public boolean isFullfilled() {
			return fullfilled;
		}

		public void setFullfilled(boolean fullfilled) {
			this.fullfilled = fullfilled;
		}

	    
	    
	    
	
}