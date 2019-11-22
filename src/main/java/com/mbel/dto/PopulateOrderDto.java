package com.mbel.dto;

import java.util.List;

import com.mbel.model.Customer;
import com.mbel.model.Order;

public class PopulateOrderDto extends Order {
	
	private List<Customer> customers;

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}


	
	
	
}
