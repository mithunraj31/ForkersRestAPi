package com.mbel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.PopulateOrderDto;
import com.mbel.serviceImpl.ForecastServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")
public class ForecastController {
	
	@Autowired
	ForecastServiceImpl forecastServiceImpl;
	
	@GetMapping("/order/forecast/")
	public List<PopulateOrderDto> orderForecast() {
		return forecastServiceImpl.getForecastOrderDetails();
	}

}
