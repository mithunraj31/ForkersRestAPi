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

import com.mbel.dto.OutgoingShipmentDto;
import com.mbel.dto.PopulateOutgoingShipmentDto;
import com.mbel.model.OutgoingShipment;
import com.mbel.serviceImpl.OutgoingShipmentServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  OutgoingShipmentController{
	
	@Autowired
	private OutgoingShipmentServiceImpl outgoingShipmentServiceImpl;  
	

	@PostMapping("/outgoing/shipment/")
	@ResponseStatus(HttpStatus.CREATED)
	public OutgoingShipment saveOutgoingShipment(@Valid @RequestBody OutgoingShipmentDto newOutgoingShipment) {
		return outgoingShipmentServiceImpl.save(newOutgoingShipment);
	}

	@GetMapping("/outgoing/shipment/")
	public List<PopulateOutgoingShipmentDto> allOutgoingShipment()  {
		return outgoingShipmentServiceImpl.getAllOutgoingShipment();
	}
	@GetMapping("/outgoing/shipment/{outgoingShipmentId}")
	public PopulateOutgoingShipmentDto outgoingShipmentById(@PathVariable (value="outgoingShipmentId") @Valid int outgoingShipmentId) {
		return outgoingShipmentServiceImpl.getOutgoingShipmentById(outgoingShipmentId);

	}

	@PutMapping("/outgoing/shipment/{outgoingShipmentId}")
	public OutgoingShipment updateOutgoingShipmentById(@PathVariable (value="outgoingShipmentId")int outgoingShipmentId,
			@Valid @RequestBody OutgoingShipmentDto OutgoingShipmentDetails)   {
		return outgoingShipmentServiceImpl.getUpdateOutgoingShipmentId(outgoingShipmentId,OutgoingShipmentDetails);


	}

	@DeleteMapping("/outgoing/shipment/{outgoingShipmentId}")
	public ResponseEntity<Map<String, String>> deleteOutgoingShipmentById(@PathVariable (value="outgoingShipmentId")@Valid int outgoingShipmentId) {
		return outgoingShipmentServiceImpl.deleteOutgoingShipmentById(outgoingShipmentId);

	}
	
}



