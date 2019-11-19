package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.SaveComponentDto;
import com.mbel.dto.SaveProductSetDto;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;


@Service("ProductServiceImpl")
public class ProductServiceImpl  {
	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;


	public Product save(Product product) {
		return productDao.save(product);
	}

	public List<Product> getAllProducts() {
		List<Product>product =productDao.findAll();
		for(int i=0;i<product.size();i++) {
			if(!product.get(i).isActive()) {
				product.remove(product.get(i));
			}
		}
		return product;
	}

	public Optional<Product> getProductsById(int productId) {
		return productDao.findById(productId);
	}

	public SaveProductSetDto saveProductSet(SaveProductSetDto productSet) {
		Product product = new Product();
		product.setProductName(productSet.getProductName());
		product.setDescription(productSet.getDescription());
		product.setPrice(productSet.getPrice());
		product.setMoq(productSet.getMoq());
		product.setLeadTime(productSet.getLeadTime());
		product.setObicNo(productSet.getObicNo());
		product.setQuantity(productSet.getQuantity());
		product.setIsSet(productSet.isSet());
		product.setActive(productSet.isActive());
		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(productSet.getUserId());
	    productDao.save(product);
	    ProductSet newProductSet = new ProductSet();
	    SaveProductSetDto component=new SaveProductSetDto();
	    int length = productSet.getProducts().length;
	    for(int i=0;i<length;i++)
	    {
	    	newProductSet.setSetId(product.getProductId());
	    	component.setProducts(productSet.getProducts());
	    	//newProductSet.setProductComponentId(component.get);
	    	newProductSet.setQuantity(component.getQuantity());
	    	productSetDao.save(newProductSet);
	    	
	    }
			
		return component;
	}

	public List<ProductSet> getAllProductSet() {
		return productSetDao.getAll();
	}

	public Optional<ProductSet> getProductSetById(int productSetId) {
		return null; //productSetDao.findById(productSetId);
	}

	public Optional<Product> getupdateById(int productId, @Valid Product productionDetails) {
		Product product = productDao.findById(productId).get();
		product.setProductName(productionDetails.getProductName());
		product.setDescription(productionDetails.getDescription());
		product.setPrice(productionDetails.getPrice());
		product.setMoq(productionDetails.getMoq());
		product.setLeadTime(productionDetails.getLeadTime());
		product.setObicNo(productionDetails.getObicNo());
		product.setQuantity(productionDetails.getQuantity());
		product.setIsSet(productionDetails.isSet());
		product.setActive(productionDetails.isActive());
		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(productionDetails.getUserId());
		productDao.save(product);
		return productDao.findById(productId);
	}

	public Optional<Product> deleteProductById(int productId) {
		Product product = productDao.findById(productId).get();
		product.setActive(false);
		productDao.save(product);
		return productDao.findById(productId);
	}

//	public Optional<ProductSet> getupdateProductSetById(int productSetId, @Valid ProductSet productionSetDetails) {
//		Optional<ProductSet> productSet = productSetDao.findById(productSetId);
//		productSet.get().setQuantity(productionSetDetails.getQuantity());
//		productSetDao.save(productSet.get());
//		return productSet;
//	}
//
//	public Optional<ProductSet> deleteProductSetById(int productSetId) {
//		Optional<ProductSet> productSet = productSetDao.findById(productSetId);
//		productSetDao.save(productSet.get());
//		return productSet;
//	}


}

