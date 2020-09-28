package com.mbel.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.ProductSetSummaryDto;
import com.mbel.serviceImpl.ProductSetHistoryServiceImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  ProductSetHistoryController{
	@Autowired
	private ProductSetHistoryServiceImpl productSetHistoryServiceImpl;   
	
	@GetMapping("/productset/summary/{productId}/{year}/{month}")
	public ProductSetSummaryDto getProductSummary(@PathVariable (value="productId") @Valid int productId,
			@PathVariable (value="year") @Valid int year,
			@PathVariable (value="month") @Valid int month) {
		return productSetHistoryServiceImpl.getProductSummaryByProductId(year,month,productId);
	}
	

}
