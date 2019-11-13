package com.mbel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.ProductionDto;
import com.mbel.dto.ProductionSetDto;
import com.mbel.model.Production;
import com.mbel.model.ProductionSet;
import com.mbel.service.ProductionService;

@RestController
@RequestMapping("/mbel")

public class  ProductionController{
	@Autowired
	private ProductionService productionService;
	
	@PostMapping("/production")
	public Production saveProduct(@RequestBody ProductionDto newProduct){
        return productionService.save(newProduct);
    }
	
	 @GetMapping("/products")
	    public List<Production> allProducts() {
	      return productionService.getAllProducts();
	}
	 
	 @GetMapping("/products/{productId}")
	    public Optional<Production> productById(@PathVariable (value="productId")int productId) {
	      return productionService.getProductsById(productId);
	      
	      }
	 @PostMapping("/productionset")
	  	public ProductionSet saveProductSet(@RequestBody ProductionSetDto newProductSet){
	          return productionService.save(newProductSet);
	      
	}
	 

	 @GetMapping("/productset")
	    public List<ProductionSet> allProductSet() {
	      return productionService.getAllProductSet();
	}
	 
	 @GetMapping("/productset/{productSetId}")
	    public Optional<ProductionSet> productSetById(@PathVariable (value="productSetId")int productSetId) {
	      return productionService.getProductSetById(productSetId);
	      
	      }
}

