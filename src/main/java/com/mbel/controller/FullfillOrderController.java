package com.mbel.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.model.ProductSetModel;
import com.mbel.serviceImpl.FullfillOrderServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class FullfillOrderController {
	
	@Autowired
	FullfillOrderServiceImpl fullfillOrderServiceImpl;
	
	
	
	@PostMapping("/order/fulfillment/")
	public ResponseEntity<Map<String, List<ProductSetModel>>> fullfilledOrder(@RequestBody FullfillRequestBody requestBody) {
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
