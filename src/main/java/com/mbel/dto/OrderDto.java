package com.mbel.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
public class OrderDto{
	
	    private int OrderId;
	    private String salesRepresentative;
	    private LocalDate date;
	    private int customerId;
	    private int salesDestination;
	    private int contractorId;
	    private LocalDateTime receivedDate;
	    private String proposalNo;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    private int userId;
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
		public LocalDate getDate() {
			return date;
		}
		public void setDate(LocalDate date) {
			this.date = date;
		}
		public int getCustomerId() {
			return customerId;
		}
		public void setCustomerId(int customerId) {
			this.customerId = customerId;
		}
		public int getSalesDestination() {
			return salesDestination;
		}
		public void setSalesDestination(int salesDestination) {
			this.salesDestination = salesDestination;
		}
		public int getContractorId() {
			return contractorId;
		}
		public void setContractorId(int contractorId) {
			this.contractorId = contractorId;
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
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		
	
	
}