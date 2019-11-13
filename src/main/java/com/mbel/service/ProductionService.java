package com.mbel.service;

import java.util.List;
import java.util.Optional;

import com.mbel.dto.ProductionDto;
import com.mbel.model.Production;


public interface ProductionService {
	
	Production save (ProductionDto product);

	List<Production> getAllProducts();

	 Optional<Production> getProductsProductById(int productId);
		 

}
