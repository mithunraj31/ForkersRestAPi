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
import com.mbel.serviceImpl.KittingBaseServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class KittingBaseController {

	@Autowired
	KittingBaseServiceImpl kittingBaseServiceImpl;

	@GetMapping("/kitting/forecast/{year}/{month}")
	public List<ProductPredictionDto> productPrediction(@PathVariable (value="year") @Valid int year,
			@PathVariable (value="month") @Valid int month) {
		 return kittingBaseServiceImpl.getProductPrediction(year, month);
	}
	
	@GetMapping("/kitting/forecast/")
	public List<ProductPredictionDto> productPrediction() {
		 return kittingBaseServiceImpl.getProductPrediction(2020, 02);
	}

}
