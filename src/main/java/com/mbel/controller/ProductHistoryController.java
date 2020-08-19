package com.mbel.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.ProductSummaryDto;
import com.mbel.model.Product;
import com.mbel.serviceImpl.ProductHistoryServiceImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  ProductHistoryController{
	@Autowired
	private ProductHistoryServiceImpl productHistoryServiceImpl;   
	
	
	@GetMapping("/product/history/{year}/{month}/{day}")
	public List<Product> getProductHistory(@PathVariable (value="year") @Valid int year,
			@PathVariable (value="month") @Valid int month,@PathVariable (value="day") @Valid int day) {
		return productHistoryServiceImpl.getProductHistory(year,month,day);
	}
	
	@GetMapping("/product/summary/{productId}/{year}/{month}")
	public ProductSummaryDto getProductSummary(@PathVariable (value="productId") @Valid int productId,
			@PathVariable (value="year") @Valid int year,
			@PathVariable (value="month") @Valid int month) {
		return productHistoryServiceImpl.getProductSummaryByProductId(year,month,productId);
	}

}
