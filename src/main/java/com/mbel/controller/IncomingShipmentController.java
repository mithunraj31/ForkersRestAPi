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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.model.IncomingShipment;
import com.mbel.serviceImpl.IncomingShipmentServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  IncomingShipmentController{
	
	@Autowired
	private IncomingShipmentServiceImpl incomingShipmentServiceImpl;  
	

	@PostMapping("/shipment/incoming/")
	@ResponseStatus(HttpStatus.CREATED)
	public @Valid List<IncomingShipment> saveIncomingShipment(@Valid @RequestBody List<IncomingShipment> incomingShipment) {
		return incomingShipmentServiceImpl.save(incomingShipment);
	}

	@GetMapping("/shipment/incoming/")
	@ResponseBody
	public List<FetchIncomingOrderdProducts> allIncomingShipment(@RequestParam Map<String,String> allParams)  {
		return incomingShipmentServiceImpl.getAllIncomingShipment(allParams);
	}
	
	@GetMapping("/shipment/incoming/arrived/")
	public List<FetchIncomingOrderdProducts> arivedIncomingShipment()  {
		return incomingShipmentServiceImpl.getAllArrivedIncomingShipment();
	}
	
	@GetMapping("/shipment/incoming/{incomingShipmentId}")
	public FetchIncomingOrderdProducts incomingShipmentById(@PathVariable (value="incomingShipmentId") @Valid int incomingShipmentId) {
		return incomingShipmentServiceImpl.getIncomingShipmentById(incomingShipmentId);

	}

	@PutMapping("/shipment/incoming/{incomingShipmentId}")
	public IncomingShipment updateIncomingShipmentById(@PathVariable (value="incomingShipmentId")int incomingShipmentId,
			@Valid @RequestBody IncomingShipment incomingShipmentDetails)   {
		return incomingShipmentServiceImpl.getUpdateIncomingShipmentId(incomingShipmentId,incomingShipmentDetails);


	}

	@DeleteMapping("/shipment/incoming/{incomingShipmentId}")
	public ResponseEntity<Map<String, String>> deleteIncomingShipmentById(@PathVariable (value="incomingShipmentId")@Valid int incomingShipmentId) {
		return incomingShipmentServiceImpl.deleteIncomingShipmentById(incomingShipmentId);

	}
	
	@PostMapping("/shipment/incoming/confirm/")
	public IncomingShipment undoConfirmOrder(@Valid @RequestBody UndoConfirmedIncomingOrder incomingOrderDisplay){
		return incomingShipmentServiceImpl.undoConfirmedIncomingOrder(incomingOrderDisplay.getIncomingShipmentId(),incomingOrderDisplay.isConfirm());
	}
	
}

class UndoConfirmedIncomingOrder {
	
	private int incomingShipmentId;
	
	private boolean confirm;

	public int getIncomingShipmentId() {
		return incomingShipmentId;
	}

	public void setIncomingShipmentId(int incomingShipmentId) {
		this.incomingShipmentId = incomingShipmentId;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}
}



