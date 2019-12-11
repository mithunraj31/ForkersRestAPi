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

import com.mbel.dto.IncomingShipmentDto;
import com.mbel.dto.PopulateIncomingShipmentDto;
import com.mbel.model.IncomingShipment;
import com.mbel.serviceImpl.IncomingShipmentServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  IncomingShipmentController{
	
	@Autowired
	private IncomingShipmentServiceImpl incomingShipmentServiceImpl;  
	

	@PostMapping("/incoming/shipment/")
	@ResponseStatus(HttpStatus.CREATED)
	public IncomingShipment saveIncomingShipment(@Valid @RequestBody IncomingShipmentDto incomingShipment) {
		return incomingShipmentServiceImpl.save(incomingShipment);
	}

	@GetMapping("/incoming/shipment/")
	public List<PopulateIncomingShipmentDto> allIncomingShipment()  {
		return incomingShipmentServiceImpl.getAllIncomingShipment();
	}
	@GetMapping("/incoming/shipment/{incomingShipmentId}")
	public PopulateIncomingShipmentDto incomingShipmentById(@PathVariable (value="incomingShipmentId") @Valid int incomingShipmentId) {
		return incomingShipmentServiceImpl.getIncomingShipmentById(incomingShipmentId);

	}

	@PutMapping("/incoming/shipment/{incomingShipmentId}")
	public IncomingShipment updateIncomingShipmentById(@PathVariable (value="incomingShipmentId")int incomingShipmentId,
			@Valid @RequestBody IncomingShipmentDto incomingShipmentDetails)   {
		return incomingShipmentServiceImpl.getUpdateIncomingShipmentId(incomingShipmentId,incomingShipmentDetails);


	}

	@DeleteMapping("/incoming/shipment/{incomingShipmentId}")
	public ResponseEntity<Map<String, String>> deleteIncomingShipmentById(@PathVariable (value="incomingShipmentId")@Valid int incomingShipmentId) {
		return incomingShipmentServiceImpl.deleteIncomingShipmentById(incomingShipmentId);

	}
	
}



