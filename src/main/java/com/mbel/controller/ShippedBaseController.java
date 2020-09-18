package com.mbel.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.ProductPredictionDto;
import com.mbel.serviceImpl.ShippedBaseServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class ShippedBaseController {

	@Autowired
	ShippedBaseServiceImpl shippedBaseServiceImpl;

	@GetMapping("/shippedbase/forecast")
	public List<ProductPredictionDto> productPrediction(@RequestParam Map<String,String> allParams) {
		 return shippedBaseServiceImpl.getProductPrediction(allParams);
	}
	

}
