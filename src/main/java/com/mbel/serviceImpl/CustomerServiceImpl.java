package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.dao.CustomerDao;
import com.mbel.dto.CustomerDto;
import com.mbel.model.Customer;


@Service("CustomerServiceImpl")
public class CustomerServiceImpl  {

	@Autowired
	CustomerDao customerDao; 

	@Autowired
	JwtAuthenticationFilter jwt;

	public CustomerDto save(CustomerDto newCustomer) {
		Customer customer =new Customer();
		customer.setAddress(newCustomer.getAddress());
		customer.setContactName(newCustomer.getContactName());
		customer.setCustomerName(newCustomer.getCustomerName());
		customer.setTel(newCustomer.getTel());
		customer.setZip(newCustomer.getZip());
		customer.setCreatedAtDateTime(LocalDateTime.now());
		customer.setUpdatedAtDateTime(LocalDateTime.now());	
		customer.setUserId(jwt.getUserdetails().getUserId());
		customer.setActive(true);
		customer.setType(concatAllReceivedType(newCustomer.getType()));
		customerDao.save(customer);
		return newCustomer;

	}

	private String concatAllReceivedType(List<String> typeList) {
		String type = typeList.get(0);
		for(int i=1;i<typeList.size();i++) {
			type=type.concat(","+typeList.get(i));
		}
		return type;
	}

	public List<CustomerDto> getAllCustomers() {
		List<CustomerDto>customerDtoList=new ArrayList<>();
		List<Customer> allCustomer = customerDao.findAll().stream()
				.filter(Customer::isActive)
				.collect(Collectors.toList());
		for(Customer customer:allCustomer) {
			CustomerDto customerDto = new CustomerDto();
			customerDto.setActive(customer.isActive());
			customerDto.setAddress(customer.getAddress());
			customerDto.setContactName(customer.getCustomerName());
			customerDto.setCustomerName(customer.getCustomerName());
			customerDto.setCreatedAtDateTime(customer.getCreatedAtDateTime());
			customerDto.setCustomerId(customer.getCustomerId());
			customerDto.setTel(customer.getTel());
			customerDto.setUpdatedAtDateTime(customer.getUpdatedAtDateTime());
			customerDto.setUserId(customer.getUserId());
			customerDto.setZip(customer.getZip());
			customerDto.setType(getListOfType(customer.getType()));
			customerDtoList.add(customerDto);

		}
		return customerDtoList;

	}

	private List<String> getListOfType(String type) {
		String[] typeValues=type.split(",");
		return Arrays.asList(typeValues);
	}

	public CustomerDto getCustomerById(int customerId) {
		Customer customer=customerDao.findById(customerId).orElse(null);
		CustomerDto customerDto = new CustomerDto();
		if(Objects.nonNull(customer)) {
			customerDto.setActive(customer.isActive());
			customerDto.setAddress(customer.getAddress());
			customerDto.setContactName(customer.getContactName());
			customerDto.setCustomerName(customer.getCustomerName());
			customerDto.setCreatedAtDateTime(customer.getCreatedAtDateTime());
			customerDto.setCustomerId(customer.getCustomerId());
			customerDto.setTel(customer.getTel());
			customerDto.setUpdatedAtDateTime(customer.getUpdatedAtDateTime());
			customerDto.setUserId(customer.getUserId());
			customerDto.setZip(customer.getZip());
			customerDto.setType(getListOfType(customer.getType()));
		}
		return customerDto;

	}

	public  CustomerDto getupdateCustomerById(int customerId, @Valid CustomerDto customerDetails) {
		Customer customer = customerDao.findById(customerId).orElse(null);
		if(Objects.nonNull(customer)) {
			customer.setAddress(customerDetails.getAddress());
			customer.setContactName(customerDetails.getContactName());
			customer.setCustomerName(customerDetails.getCustomerName());
			customer.setType(concatAllReceivedType(customerDetails.getType()));
			customer.setUpdatedAtDateTime(LocalDateTime.now());
			customer.setUserId(jwt.getUserdetails().getUserId());
			customer.setTel(customerDetails.getTel());
			customer.setZip(customerDetails.getZip());
			customer.setActive(true);
			customerDao.save(customer);
		}
		return customerDetails;
	}

	public ResponseEntity<Map<String, String>> deleteCustomerById(int customerId) {
		Optional<Customer> customerValue = customerDao.findById(customerId);
		if(customerValue.isPresent()) {
			Customer customer = customerValue.get();
			customer.setActive(false);
			customerDao.save(customer); 
		}
		Map<String, String> response = new HashMap<>();
		response.put("message", "Customer has been deleted");
		response.put("customerId", String.valueOf(customerId));


		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}

}




