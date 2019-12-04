package com.mbel.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.model.Customer;
import com.mbel.serviceImpl.CustomerServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  CustomerController{
	
	@Autowired
	private CustomerServiceImpl customerServiceImpl;  
	

	@PostMapping("/customer/")
	public Customer saveProduct(@RequestBody Customer newCustomer){
		return customerServiceImpl.save(newCustomer);
	}

	@GetMapping("/customer/")
	public List<Customer> allCustomer() {
		return customerServiceImpl.getAllCustomers();
	}

	@GetMapping("/customer/{customerId}")
	public Optional<Customer> customerById(@PathVariable (value="customerId")int customerId) {
		return customerServiceImpl.getCustomerById(customerId);

	}

	@PutMapping("/customer/{customerId}")
	public Customer updateCustomerById(@PathVariable (value="customerId")int customerId,
			@Valid @RequestBody Customer customerDetails) {
		return customerServiceImpl.getupdateCustomerById(customerId,customerDetails);


	}

	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<Map<String, String>> deleteCustomerById(@PathVariable (value="customerId")int customerId) {
		return customerServiceImpl.deleteCustomerById(customerId);

	}
	
}



