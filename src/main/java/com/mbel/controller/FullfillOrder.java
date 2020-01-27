package com.mbel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.serviceImpl.FullfillOrderServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class FullfillOrder {
	
	@Autowired
	FullfillOrderServiceImpl fullfillOrderServiceImpl;
	
	
	
	@PostMapping("/order/fulfillment/")
	public List<String> fullfilledOrder(@RequestBody FullfillRequestBody requestBody) {
		if(requestBody.isFulfillment()) {
		return fullfillOrderServiceImpl.getFullfillOrder(requestBody.getOrderId());
		}
		return null;
		
	}
	
	

}

 class FullfillRequestBody{
	
	private int orderId;
	
	private boolean fulfillment;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public boolean isFulfillment() {
		return fulfillment;
	}

	public void setFulfillment(boolean fulfillment) {
		this.fulfillment = fulfillment;
	}

	
	
}
