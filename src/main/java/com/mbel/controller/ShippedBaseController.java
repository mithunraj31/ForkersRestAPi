package com.mbel.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.ProductPredictionDto;
import com.mbel.serviceImpl.ShippedBaseServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class ShippedBaseController {

	@Autowired
	ShippedBaseServiceImpl shippedBaseServiceImpl;

	@GetMapping("/shippedbase/forecast/{year}/{month}")
	public List<ProductPredictionDto> productPrediction(@PathVariable (value="year") @Valid int year,
			@PathVariable (value="month") @Valid int month) {
		 return shippedBaseServiceImpl.getProductPrediction(year, month);
	}
	
	@GetMapping("/shippedbase/forecast/")
	public List<ProductPredictionDto> productPrediction() {
		 return shippedBaseServiceImpl.getProductPrediction(2020, 02);
	}

}
