package com.mbel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PostMapping("/order/display/")
	public Order displayOrder(@Valid @RequestBody OrderDisplay orderDisplay){
		return orderServiceImpl.orderDisplay(orderDisplay.getOrderId(),orderDisplay.isDisplay());
	}
	
	@GetMapping("/order/delayed/count/")
	public Map<String, Integer> delayedOrderCount() {
		return orderServiceImpl.getDelayedOrderCount();
	}
	
	@PostMapping("/order/confirm/")
	public Order confirmOrder(@Valid @RequestBody ConfirmOrder orderDisplay){
		return orderServiceImpl.orderConfirm(orderDisplay.getOrderId(),orderDisplay.isConfirm());
	}
	
	@GetMapping("/order/sort/")
	public List<PopulateOrderDto>  sortOrder(@RequestParam(defaultValue = "1") boolean fcst,
			@RequestParam(defaultValue = "1") boolean wait ,@RequestParam(defaultValue = "1") boolean withKitting,
			@RequestParam(defaultValue = "1") boolean withoutKitting) {
		return orderServiceImpl.sortOrder(fcst,wait,withKitting,withoutKitting);
	}
	
	
}

class ConfirmOrder {
	
	private int orderId;
	
	private boolean confirm;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	
	}


class OrderDisplay {
	
	private int orderId;
	
	private boolean display;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

}


