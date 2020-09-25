package com.mbel.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.model.ProductSetModel;
import com.mbel.serviceImpl.VerifyOrderServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class VerifyOrderController {

	@Autowired
	VerifyOrderServiceImpl verifyOrderServiceImpl;

	@GetMapping("/order/verify/{productId}/{dueDate}/{amount}")
	public ResponseEntity<Map<String, List<ProductSetModel>>> orderForecast(@PathVariable (value="productId") @Valid int productId,
			@PathVariable (value="dueDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
    LocalDateTime date,@PathVariable (value="amount") @Valid int amount) {
		return verifyOrderServiceImpl.getForecastOrderStatus(productId,date,amount); 
	}

}
