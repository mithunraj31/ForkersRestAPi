package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.CustomerDao;
import com.mbel.model.Customer;


@Service("CustomerServiceImpl")
public class CustomerServiceImpl  {
	
	@Autowired
	 CustomerDao customerDao; 
	
	
	public Customer save(Customer newCustomer) {
		newCustomer.setCreatedAtDateTime(LocalDateTime.now());
		newCustomer.setUpdatedAtDateTime(LocalDateTime.now());		
		return customerDao.save(newCustomer);
		 
	}

	public List<Customer> getAllCustomers() {
		return customerDao.findAll();
	}

	public Optional<Customer> getCustomerById(int customerId) {
		 return customerDao.findById(customerId);
	}

	public Customer getupdateCustomerById(int customerId, @Valid Customer customerDetails) {
		Customer customer = customerDao.findById(customerId).get();
		customer.setAddress(customerDetails.getAddress());
		customer.setContactName(customerDetails.getContactName());
		customer.setCustomerName(customerDetails.getCustomerName());
		customer.setType(customerDetails.getType());
		customer.setUpdatedAtDateTime(LocalDateTime.now());
		customer.setUserId(customerDetails.getUserId());
		customer.setTel(customerDetails.getTel());
		customer.setZip(customerDetails.getZip());
		return customerDao.save(customer);
	}

	public String deleteCustomerById(int customerId) {
		 customerDao.deleteById(customerId);
		 return "deleted";
	}


}

