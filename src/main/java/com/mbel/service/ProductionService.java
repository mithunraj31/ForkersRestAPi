package com.mbel.service;

import java.util.List;
import java.util.Optional;

import com.mbel.dto.ProductionDto;
import com.mbel.dto.ProductionSetDto;
import com.mbel.model.Production;
import com.mbel.model.ProductionSet;


public interface ProductionService {
	
	Production save (ProductionDto product);

	List<Production> getAllProducts();

	 Optional<Production> getProductsById(int productId);

	ProductionSet save(ProductionSetDto newProductSet);
	
	List<ProductionSet> getAllProductSet();

	 Optional<ProductionSet> getProductSetById(int productId);
		 

}
