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
@Table(name="Order")
public class Order{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "order_id")
	    private int OrderId;
	     
	    @NotNull(message="* Please Enter dueDate")
	    @Column(name = "due_date")
	    private LocalDateTime dueDate;
	    
	    @NotNull(message="* Please Enter customerId")
	    @Column(name = "customer_id")
	    private int customerId;
	    
	    @NotNull(message="* Please Enter salesDestinationId")
	    @Column(name = "sales_destination_id")
	    private int salesDestinationId;
	    
	    @NotNull(message="* Please Enter contractorId")
	    @Column(name = "contractor_id")
	    private int contractorId;
	    
	    @NotNull(message="* Please Enter receivedDate")
	    @Column(name = "received_date")
	    private LocalDateTime receivedDate;
	    
	    @NotEmpty(message="* Please Enter proposalNo")
	    @Column(name = "proposal_no")
	    private String proposalNo;
	    
	    @Column(name = "created_at")
	    private LocalDateTime createdAt;
	    
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;
	    
	    @Column(name = "user_id")
	    private int userId;
	    
	    @NotNull(message="* Please Enter salesUserId")
	    @Column(name = "sales_user_id")
	    private int salesUserId;
	    
	    @Column(name = "edit_reason")
	    private String editReason;
	    
	    @Column(name = "active")
	    private boolean active;
	    
	    @Column(name = "forecast")
	    private boolean forecast;
	    
	    @Column(name = "fulfilled")
	    private boolean fulfilled;
		
		public int getOrderId() {
			return OrderId;
		}
		public void setOrderId(int orderId) {
			OrderId = orderId;
		}
		
		public LocalDateTime getDueDate() {
			return dueDate;
		}
		public void setDueDate(LocalDateTime dueDate) {
			this.dueDate = dueDate;
		}
		public int getCustomerId() {
			return customerId;
		}
		public void setCustomerId(int customerId) {
			this.customerId = customerId;
		}
		
		public int getSalesDestinationId() {
			return salesDestinationId;
		}
		public void setSalesDestinationId(int salesDestinationId) {
			this.salesDestinationId = salesDestinationId;
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
		public int getSalesUserId() {
			return salesUserId;
		}
		public void setSalesUserId(int salesUserId) {
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
		public boolean isFulfilled() {
			return fulfilled;
		}
		public void setFulfilled(boolean fulfilled) {
			this.fulfilled = fulfilled;
		}
	    
	
}