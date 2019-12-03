package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.dao.CustomerDao;
import com.mbel.model.Customer;


@Service("CustomerServiceImpl")
public class CustomerServiceImpl  {
	
	@Autowired
	 CustomerDao customerDao; 
	
	 @Autowired
	 JwtAuthenticationFilter jwt;
	
	
	public Customer save(Customer newCustomer) {
		newCustomer.setCreatedAtDateTime(LocalDateTime.now());
		newCustomer.setUpdatedAtDateTime(LocalDateTime.now());	
		newCustomer.setUserId(jwt.getUserdetails().getUserId());
		newCustomer.setActive(true);
		return customerDao.save(newCustomer);
		
	}

	public List<Customer> getAllCustomers() {
		List<Customer> activeCustomer = new ArrayList<>();
		List<Customer> customer = customerDao.findAll();
		for(Customer pd :customer ) {
			if(pd.isActive()) {
				activeCustomer.add(pd);
			}
		}
		return activeCustomer;
		
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
		customer.setUserId(jwt.getUserdetails().getUserId());
		customer.setTel(customerDetails.getTel());
		customer.setZip(customerDetails.getZip());
		customer.setActive(true);
		return customerDao.save(customer);
	}

	public ResponseEntity<Map<String, String>> deleteCustomerById(int customerId) {
		Customer customer = customerDao.findById(customerId).get();
		customer.setActive(false);
		 customerDao.save(customer); 
		 Map<String, String> response = new HashMap<>();
		 response.put("message", "Customer has been deleted");
		 response.put("customerId", String.valueOf(customerId));
		 
		 return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}


}

