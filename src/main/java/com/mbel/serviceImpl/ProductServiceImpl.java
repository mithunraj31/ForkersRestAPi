package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


@Service("ProductServiceImpl")
public class ProductServiceImpl  {
	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;


	public Product save(Product product) {
		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
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
		product.setSet(productSet.isSet());
		product.setActive(productSet.isActive());
		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(productSet.getUserId());
		productDao.save(product);
		ProductSet newProductSet = new ProductSet();
		SaveProductSetDto component=new SaveProductSetDto();
		//int length = productSet.getProducts().length;
		// for(int i=0;i<length;i++)
		{
			newProductSet.setSetId(product.getProductId());
			component.setProducts(productSet.getProducts());
			//newProductSet.setProductComponentId(component.get);
			newProductSet.setQuantity(component.getQuantity());
			//	productSetDao.save(newProductSet);

		}

		return component;
	}

	public SaveComponentDto getAllProductSet() {
		Product component = new Product();
		SaveComponentDto componentSet= new SaveComponentDto();
		List<Product> productsetList = new ArrayList<>();
		List<Map<Object, Object>> elements = productSetDao.getAll();
		componentSet.setProductId((Integer)elements.get(0).get("package_id"));
		componentSet.setProductName((String)elements.get(0).get("package_name"));
		componentSet.setDescription((String)elements.get(0).get("package_desc"));
		componentSet.setPrice((Double)elements.get(0).get("package_price"));
		componentSet.setMoq((Integer)elements.get(0).get("package_moq"));
		componentSet.setLeadTime((Integer)elements.get(0).get("package_lead"));
		componentSet.setObicNo((String)elements.get(0).get("package_obic"));
		componentSet.setQuantity((Integer)elements.get(0).get("package_qty"));
		componentSet.setSet((boolean)elements.get(0).get("package_set"));
		componentSet.setActive((boolean)elements.get(0).get("package_active"));
		//componentSet.setCreatedAtDateTime((Integer)elements.get(0).get("package_created"));
		//componentSet.setUpdatedAtDateTime((Integer)elements.get(0).get("package_update"));
		componentSet.setUserId((Integer)elements.get(0).get("package_user"));
		componentSet.setProductSetId((Integer)elements.get(0).get("product_set_id"));
		componentSet.setProductComponentId((Integer)elements.get(0).get("product_component_id"));
		componentSet.setSetId((Integer)elements.get(0).get("product_component_id"));
		componentSet.setQty((Integer)elements.get(0).get("quantity"));

		for(Map<Object, Object> a : elements ) {
			component.setProductId((Integer)a.get("product_id"));
			component.setProductName((String)a.get("product_name"));
			component.setDescription((String)a.get("description"));
			component.setPrice((Double)a.get("price"));
			component.setMoq((Integer)a.get("moq"));
			component.setLeadTime((Integer)a.get("lead_time"));
			component.setObicNo((String)a.get("obic_no"));
			component.setQuantity((Integer)a.get("qty"));
			component.setActive((boolean)a.get("active"));
			component.setSet((boolean)a.get("is_set"));
//			component.setCreatedAtDateTime((LocalDateTime)a.get("created_at_date_time"));
//			component.setUpdatedAtDateTime((LocalDateTime)a.get("updated_at_date_time"));
			component.setUserId((Integer)a.get("user_id"));
			productsetList.add(component);
		}
		componentSet.setProduct(productsetList);
		return componentSet;
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
		product.setSet(productionDetails.isSet());
		product.setActive(productionDetails.isActive());
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

