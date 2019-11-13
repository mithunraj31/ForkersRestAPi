package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.ProductionDao;
import com.mbel.dao.ProductionSetDao;
import com.mbel.dto.ProductionDto;
import com.mbel.dto.ProductionSetDto;
import com.mbel.model.Production;
import com.mbel.model.ProductionSet;
import com.mbel.service.ProductionService;


@Service("ProductionService")
public class ProductionServiceImpl implements ProductionService {
	@Autowired 
	ProductionDao productionDao;
	
	@Autowired 
	ProductionSetDao productionSetDao;

	@Override
	public Production save(ProductionDto product) {
		Production newProduct = new Production();
		newProduct.setProductName(product.getProductName());
		newProduct.setDescription(product.getDescription());
		newProduct.setPrice(product.getPrice());
		newProduct.setMoq(product.getMoq());
		newProduct.setLeadTime(product.getLeadTime());
		newProduct.setObicNo(product.getObicNo());
		newProduct.setQuantity(product.getQuantity());
		newProduct.setSet(product.isSet());
		newProduct.setActive(product.isActive());
		newProduct.setCreatedAtDateTime(LocalDateTime.now());
		newProduct.setUpdatedAtDateTime(LocalDateTime.now());
		newProduct.setUserId(product.getUserId());
		
		return productionDao.save(newProduct);
	}

	@Override
	public List<Production> getAllProducts() {
		return productionDao.findAll();
	}

	@Override
	public Optional<Production> getProductsById(int productId) {
		return productionDao.findById(productId);
	}

	@Override
	public ProductionSet save(ProductionSetDto ProductSet) {
		ProductionSet newProductSet = new ProductionSet();
		newProductSet.setQuantity(ProductSet.getQuantity());
		return productionSetDao.save(newProductSet);
	}

	@Override
	public List<ProductionSet> getAllProductSet() {
		 return productionSetDao.findAll();
	}

	@Override
	public Optional<ProductionSet> getProductSetById(int productSetId) {
		return productionSetDao.findById(productSetId);
	}
		
	}

