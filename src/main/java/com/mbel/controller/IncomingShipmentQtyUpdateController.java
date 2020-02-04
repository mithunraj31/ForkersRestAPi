package com.mbel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.serviceImpl.IncomingShipmentQtyUpdateServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class IncomingShipmentQtyUpdateController {
	
	@Autowired
	IncomingShipmentQtyUpdateServiceImpl incomingShipmentQtyUpdateServiceImpl;
	
	@PostMapping("/shipment/incoming/arrival/")
	@ResponseStatus(HttpStatus.CREATED)
	public void updateIncomingShipment(@RequestBody IncomingProductUpdate incomingProductUpdate) {
		if(incomingProductUpdate.isArrival()){
		  incomingShipmentQtyUpdateServiceImpl.updateQuantity(incomingProductUpdate.getIncomingShipmentId());
		}
	}

}

class IncomingProductUpdate {
	
	private int incomingShipmentId;
	
	private boolean arrival;

	public int getIncomingShipmentId() {
		return incomingShipmentId;
	}

	public void setIncomingShipmentId(int incomingShipmentId) {
		this.incomingShipmentId = incomingShipmentId;
	}

	public boolean isArrival() {
		return arrival;
	}

	public void setArrival(boolean arrival) {
		this.arrival = arrival;
	}

	
	
	
}
