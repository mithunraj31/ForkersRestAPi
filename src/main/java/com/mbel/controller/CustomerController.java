package com.mbel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.dto.SaveOrderSetDto;
import com.mbel.dto.SaveProductSetDto;
import com.mbel.model.Customer;
import com.mbel.model.Order;
import com.mbel.model.Product;
import com.mbel.serviceImpl.CustomerServiceImpl;
import com.mbel.serviceImpl.OrderServiceImpl;
import com.mbel.serviceImpl.ProductServiceImpl;

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
	public String deleteCustomerById(@PathVariable (value="customerId")int customerId) {
		return customerServiceImpl.deleteCustomerById(customerId);

	}
	
}


