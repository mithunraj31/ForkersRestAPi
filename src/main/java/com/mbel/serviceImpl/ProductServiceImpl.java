package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;


@Service("ProductionService")
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
		for(Product iterate: product) {
			if(iterate.isActive()== false) {
				product.remove(iterate);
			}

		}
		return product;
	}

	public Optional<Product> getProductsById(int productId) {
		return productDao.findById(productId);
	}

	public ProductSet save(ProductSet productSet) {
		return productSetDao.save(productSet);
	}

	public List<ProductSet> getAllProductSet() {
		return productSetDao.findAll();
	}

	public Optional<ProductSet> getProductSetById(int productSetId) {
		return productSetDao.findById(productSetId);
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

	public Optional<ProductSet> getupdateProductSetById(int productSetId, @Valid ProductSet productionSetDetails) {
		Optional<ProductSet> productSet = productSetDao.findById(productSetId);
		productSet.get().setQuantity(productionSetDetails.getQuantity());
		productSet.get().setProduction(productionSetDetails.getProduction());
		productSetDao.save(productSet.get());
		return productSet;
	}

	public Optional<ProductSet> deleteProductSetById(int productSetId) {
		Optional<ProductSet> productSet = productSetDao.findById(productSetId);
		productSet.get().getProduction().setActive(false);
		productSetDao.save(productSet.get());
		return productSet;
	}


}

