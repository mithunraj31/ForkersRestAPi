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

import com.mbel.model.Product;
import com.mbel.model.ProductSet;
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

	@GetMapping("/products")
	public List<Product> allProducts() {
		return productServiceImpl.getAllProducts();
	}

	@GetMapping("/products/{productId}")
	public Optional<Product> productById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.getProductsById(productId);

	}

	@PutMapping("/updateproducts/{productId}")
	public Optional<Product> updateProductById(@PathVariable (value="productId")int productId,
			@Valid @RequestBody Product productDetails) {
		return productServiceImpl.getupdateById(productId,productDetails);


	}

	@DeleteMapping("/deleteproducts/{productId}")
	public Optional<Product> deleteProductById(@PathVariable (value="productId")int productId) {
		return productServiceImpl.deleteProductById(productId);

	}
	@PostMapping("/productset")
	public ProductSet saveProductSet(@RequestBody ProductSet newProductSet){
		return productServiceImpl.save(newProductSet);

	}


	@GetMapping("/productionset")
	public List<ProductSet> allProductSet() {
		return productServiceImpl.getAllProductSet();
	}

	@GetMapping("/productset/{productSetId}")
	public Optional<ProductSet> productSetById(@PathVariable (value="productSetId")int productSetId) {
		return productServiceImpl.getProductSetById(productSetId);

	}
	@PutMapping("/updateproductset/{productSetId}")

	public Optional<ProductSet> updateProductSetById(@PathVariable (value="productSetId")int productSetId,
			@Valid @RequestBody ProductSet productionSetDetails) {
		return productServiceImpl.getupdateProductSetById(productSetId,productionSetDetails);

	}

	@DeleteMapping("/deleteproductset/{productSetId}")
	public Optional<ProductSet> deleteProductSetById(@PathVariable (value="productSetId")int productSetId) {
		return productServiceImpl.deleteProductSetById(productSetId);

	}
}



