package com.mbel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.ProductPredictionDto;
import com.mbel.serviceImpl.ProductPredictionServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class ProductPredictionController {

	@Autowired
	ProductPredictionServiceImpl productPredictionServiceImpl;

	@GetMapping("/product/forecast/")
	public List<ProductPredictionDto> productPrediction() {
		 return productPredictionServiceImpl.getProductPrediction(2020, 02);
	}

}
