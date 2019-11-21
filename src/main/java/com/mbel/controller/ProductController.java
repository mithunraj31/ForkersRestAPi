package com.mbel.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.SaveProductSetDto;
import com.mbel.model.Product;
import com.mbel.serviceImpl.ProductServiceImpl;

@RestController
@RequestMapping("/mbel")

public class  ProductController{
	@Autowired
	private ProductServiceImpl productServiceImpl;

	@PostMapping("/product")
	public Product saveProduct(@RequestBody Product newProduct){
		return productServiceImpl.save(newProduct);
	}

	@GetMapping("/product")
	public List<Product> allProducts() {
		return productServiceImpl.getAllProducts();
	}

	@GetMapping("/product/{productId}")
	public Optional<Product> productById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.getProductsById(productId);

	}

	@PutMapping("/product/{productId}")
	public Optional<Product> updateProductById(@PathVariable (value="productId")int productId,
			@Valid @RequestBody Product productDetails) {
		return productServiceImpl.getupdateById(productId,productDetails);


	}

	@DeleteMapping("/product/{productId}")
	public Optional<Product> deleteProductById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.deleteProductById(productId);

	}
	@PostMapping("/productset")
	public FetchProductSetDto saveProductSet(@RequestBody SaveProductSetDto newProductSet){
		return productServiceImpl.saveProductSet(newProductSet);

	}


	@GetMapping("/productset")
	public List<FetchProductSetDto> allProductSet() {
		return productServiceImpl.getAllProductSet();
	}

	@GetMapping("/productset/{productId}")
	public FetchProductSetDto productSetById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.getProductSetById(productId);

	}
	@PutMapping("/productset/{productId}")
	public FetchProductSetDto updateProductSetById(@PathVariable (value="productId")int productId,
			@Valid @RequestBody SaveProductSetDto productSetDetails) {
		return  productServiceImpl.getupdateProductSetById(productId, productSetDetails);

	}

	@DeleteMapping("/productset/{productSetId}")
	public FetchProductSetDto deleteProductSetById(@PathVariable (value="productSetId")int productId) {
		return productServiceImpl.deleteProductSetById(productId);

	}
}



