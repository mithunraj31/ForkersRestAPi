package com.mbel.dto;

import java.util.List;

import javax.persistence.Transient;

import com.mbel.model.Customer;
import com.mbel.model.OrderModel;

public class PopulateOrderDto extends OrderModel {
	
	private Customer customer;
	
	private Customer salesDestination;
	
	private Customer contractor;
	
	
	private List<FetchOrderdProducts> orderedProducts;

    
	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}



	public Customer getSalesDestination() {
		return salesDestination;
	}


	public void setSalesDestination(Customer salesDestination) {
		this.salesDestination = salesDestination;
	}


	public Customer getContractor() {
		return contractor;
	}


	public void setContractor(Customer contractor) {
		this.contractor = contractor;
	}


	public List<FetchOrderdProducts> getOrderedProducts() {
		return orderedProducts;
	}


	public void setOrderedProducts(List<FetchOrderdProducts> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}





	
}
