package com.mbel.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

import com.mbel.dto.PopulateOrderDto;
import com.mbel.dto.SaveOrderSetDto;
import com.mbel.model.Order;
import com.mbel.serviceImpl.OrderServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  OrderController{
	
	
	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@PostMapping("/order/")
	public Order saveOrder(@Valid @RequestBody SaveOrderSetDto newOrder){
		return orderServiceImpl.save(newOrder);
	}

	@GetMapping("/order/")
	public List<PopulateOrderDto> allOrder() {
		return orderServiceImpl.getAllOrders();
	}
	
	@GetMapping("/order/fulfilled/")
	public List<PopulateOrderDto> allFulfilledOrder() {
		return orderServiceImpl.getAllFulfilledOrders();
	}

	@GetMapping("/order/{orderId}")
	public PopulateOrderDto orderById(@PathVariable (value="orderId")@NotNull int orderId) {
		return orderServiceImpl.getOrderById(orderId);

	}

	@PutMapping("/order/{orderId}")
	public Order updateOrderById(@PathVariable (value="orderId")@NotNull int orderId,
			 @RequestBody @Valid SaveOrderSetDto orderDetails) {
		return orderServiceImpl.getupdateOrderById(orderId,orderDetails);


	}

	@DeleteMapping("/order/{orderId}")
	public Order deleteOrderById(@PathVariable (value="orderId")@NotNull int orderId) {
		return orderServiceImpl.deleteOrderById(orderId);

	}
	
}



