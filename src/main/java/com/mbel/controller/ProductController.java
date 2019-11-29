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

public class  ProductController{
	@Autowired
	private ProductServiceImpl productServiceImpl;   
	
	@Autowired
	private CustomerServiceImpl customerServiceImpl;  
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@PostMapping("/product/")
	public Product saveProduct(@RequestBody Product newProduct){
		return productServiceImpl.save(newProduct);
	}

	@GetMapping("/product/")
	public List<Product> allProducts() {
		return productServiceImpl.getAllProducts();
	}

	@GetMapping("/product/{productId}")
	public Optional<Product> productById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.getProductsById(productId);

	}

	@PutMapping("/product/{productId}")
	public Optional<Product> updateProductById(@PathVariable (value="productId")int productId,
			@Valid @RequestBody Product productDetails) {
		return productServiceImpl.getupdateById(productId,productDetails);


	}

	@DeleteMapping("/product/{productId}")
	public Optional<Product> deleteProductById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.deleteProductById(productId);

	}
	@PostMapping("/productset/")
	public FetchProductSetDto saveProductSet(@RequestBody SaveProductSetDto newProductSet){
		return productServiceImpl.saveProductSet(newProductSet);

	}


	@GetMapping("/productset/")
	public List<FetchProductSetDto> allProductSet() {
		return productServiceImpl.getAllProductSet();
	}

	@GetMapping("/productset/{productId}")
	public FetchProductSetDto productSetById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.getProductSetById(productId);

	}
	@PutMapping("/productset/{productId}")
	public FetchProductSetDto updateProductSetById(@PathVariable (value="productId")int productId,
			@Valid @RequestBody SaveProductSetDto productSetDetails) {
		return  productServiceImpl.getupdateProductSetById(productId, productSetDetails);

	}

	@DeleteMapping("/productset/{productId}")
	public FetchProductSetDto deleteProductSetById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.deleteProductSetById(productId);

	}
	

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
	
	@PostMapping("/order")
	public Order saveOrder(@RequestBody SaveOrderSetDto newOrder){
		return orderServiceImpl.save(newOrder);
	}

	@GetMapping("/order")
	public List<PopulateOrderDto> allOrder() {
		return orderServiceImpl.getAllOrders();
	}

	@GetMapping("/order/{orderId}")
	public PopulateOrderDto orderById(@PathVariable (value="orderId")int orderId) {
		return orderServiceImpl.getOrderById(orderId);

	}

	@PutMapping("/order/{orderId}")
	public Order updateOrderById(@PathVariable (value="orderId")int orderId,
			 @RequestBody @Valid SaveOrderSetDto orderDetails) {
		return orderServiceImpl.getupdateOrderById(orderId,orderDetails);


	}

	@DeleteMapping("/order/{orderId}")
	public Order deleteOrderById(@PathVariable (value="orderId")int orderId) {
		return orderServiceImpl.deleteOrderById(orderId);

	}
	
}



