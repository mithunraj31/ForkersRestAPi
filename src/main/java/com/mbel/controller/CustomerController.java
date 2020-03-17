package com.mbel.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.CustomerDto;
import com.mbel.serviceImpl.CustomerServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  CustomerController{
	
	@Autowired
	private CustomerServiceImpl customerServiceImpl;  
	

	@PostMapping("/customer/")
	@ResponseStatus(HttpStatus.CREATED)
	public CustomerDto saveProduct(@Valid @RequestBody CustomerDto newCustomer) {
		return customerServiceImpl.save(newCustomer);
	}

	@GetMapping("/customer/")
	public List<CustomerDto> allCustomer()  {
		return customerServiceImpl.getAllCustomers();
	}
	@GetMapping("/customer/{customerId}")
	public CustomerDto customerById(@PathVariable (value="customerId") @Valid int customerId) {
		return customerServiceImpl.getCustomerById(customerId);

	}

	@PutMapping("/customer/{customerId}")
	public CustomerDto updateCustomerById(@PathVariable (value="customerId")int customerId,
			@Valid @RequestBody CustomerDto customerDetails)   {
		return customerServiceImpl.getupdateCustomerById(customerId,customerDetails);


	}

	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<Map<String, String>> deleteCustomerById(@PathVariable (value="customerId")@Valid int customerId) {
		return customerServiceImpl.deleteCustomerById(customerId);

	}
	
}



