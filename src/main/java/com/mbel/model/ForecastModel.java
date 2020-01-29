package com.mbel.model;

import java.time.LocalDateTime;

public class ForecastModel{
	
	    private int orderId;
	     
	    private LocalDateTime dueDate;
	    
	    private LocalDateTime receivedDate;
	    
	    private String proposalNo;
	    
	    private boolean active;
	    
	    private boolean forecast;
	    
	    private boolean fulfilled;
	    
	    

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