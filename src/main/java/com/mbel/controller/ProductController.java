package com.mbel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.SaveProductSetDto;
import com.mbel.model.Product;
import com.mbel.serviceImpl.ProductServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mbel")

public class  ProductController{
	@Autowired
	private ProductServiceImpl productServiceImpl;   
	

	@PostMapping("/product/")	
	@ResponseStatus(HttpStatus.CREATED)
	public Product saveProduct(@Valid @RequestBody Product newProduct){
		return productServiceImpl.save(newProduct);
	}

	@GetMapping("/product/")
	public List<Product> allProducts() {
		return productServiceImpl.getAllProducts();
	}

	@GetMapping("/product/{productId}")
	public Optional<Product> productById(@PathVariable (value="productId")@NonNull int productId) {
		return productServiceImpl.getProductsById(productId);

	}

	@PutMapping("/product/{productId}")
	public Product updateProductById(@PathVariable (value="productId")@NonNull int productId,
			@Valid @RequestBody Product productDetails) {
		return productServiceImpl.getupdateById(productId,productDetails);


	}

	@DeleteMapping("/product/{productId}")
	public Product deleteProductById(@PathVariable (value="productId")@NonNull int productId) {
		return productServiceImpl.deleteProductById(productId);

	}
	@PostMapping("/productset/")
	public Product saveProductSet(@Valid @RequestBody SaveProductSetDto newProductSet){
		return productServiceImpl.saveProductSet(newProductSet);

	}


	@GetMapping("/productset/")
	public List<FetchProductSetDto> allProductSet() {
		return productServiceImpl.getAllProductSet();
	}
	
	@GetMapping("/productsetsql/")
	public List<FetchProductSetDto> allProductSetSql() {
		return productServiceImpl.getAllSqlProductSet();
	}

	@GetMapping("/productset/{productId}")
	public FetchProductSetDto productSetById(@PathVariable (value="productId")@NonNull int productId) {
		return productServiceImpl.getProductSetById(productId);

	}
	
	@GetMapping("/productsetsql/{productId}")
	public FetchProductSetDto productSetSqlById(@PathVariable (value="productId")@NonNull int productId) {
		return productServiceImpl.getProductSetSqlById(productId);

	}
	
	
	@PutMapping("/productset/{productId}")
	public Product updateProductSetById(@PathVariable (value="productId")@NonNull int productId,
			@Valid @RequestBody SaveProductSetDto productSetDetails) {
		return  productServiceImpl.getupdateProductSetById(productId, productSetDetails);

	}

	@DeleteMapping("/productset/{productId}")
	public Product deleteProductSetById(@PathVariable (value="productId")@NonNull int productId) {
		return productServiceImpl.deleteProductSetById(productId);

	}
	


}



