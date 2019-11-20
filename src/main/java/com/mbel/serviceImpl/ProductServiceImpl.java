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
import com.mbel.dto.FetchProductSetDto;
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

	public FetchProductSetDto saveProductSet(SaveProductSetDto productSet) {
		Product product = new Product();
		product.setProductName(productSet.getProductName());
		product.setDescription(productSet.getDescription());
		product.setPrice(productSet.getPrice());
		product.setMoq(productSet.getMoq());
		product.setLeadTime(productSet.getLeadTime());
		product.setObicNo(productSet.getObicNo());
		product.setQuantity(productSet.getQuantity());
		product.setSet(true);
		product.setActive(productSet.isActive());
		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(productSet.getUserId());
		productDao.save(product);
		int id  = product.getProductId();
		int setValue  =productSet.getProductset().size();
		int setId = 0 ;
		for(int i=0;i<setValue;i++) {
			ProductSet newProductSet = new ProductSet();
		newProductSet.setSetId(id);
		newProductSet.setQuantity(productSet.getProductset().get(i).getQty());
		newProductSet.setProductComponentId(productSet.getProductset().get(i).getProductcomponentId());
		productSetDao.save(newProductSet);
		setId  = newProductSet.getProductSetId();

		}
		return getProductSetById(setId);
	}

	public FetchProductSetDto getAllProductSet() {
		FetchProductSetDto componentSet= new FetchProductSetDto();

		List<ProductSetModel> productList = new ArrayList<>();
		List<Map<Object, Object>> elements = productSetDao.getAll();
		Product mainProduct=productDao.findById((Integer)elements.get(0).get("package_id")).get();
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
		componentSet.setCreatedAtDateTime(mainProduct.getCreatedAtDateTime());
		componentSet.setUpdatedAtDateTime(mainProduct.getUpdatedAtDateTime());

		for(Map<Object, Object> a : elements ) {
			ProductSetModel productSetModel = new ProductSetModel();
			Product component = new Product();
			Product subProduct=productDao.findById((Integer)a.get("product_id")).get();
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
			component.setCreatedAtDateTime(subProduct.getCreatedAtDateTime());
			component.setUpdatedAtDateTime(subProduct.getUpdatedAtDateTime());
			component.setUserId((Integer)a.get("user_id"));
			productSetModel.setProduct(component);
			productSetModel.setQuantity((Integer)a.get("quantity"));
			productList.add(productSetModel);
		}
		componentSet.setProducts(productList);
		return componentSet;
	}

	public FetchProductSetDto getProductSetById(int productSetId) {
		FetchProductSetDto componentSet= new FetchProductSetDto();
		ProductSet productSet=productSetDao.findById(productSetId).get();
        int setId = productSet.getSetId();
		List<ProductSetModel> productList = new ArrayList<>();
		List<Map<Object, Object>> elements = productSetDao.getProductSetsById(setId);
		Product mainProduct=productDao.findById((Integer)elements.get(0).get("package_id")).get();
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
		componentSet.setCreatedAtDateTime(mainProduct.getCreatedAtDateTime());
		componentSet.setUpdatedAtDateTime(mainProduct.getUpdatedAtDateTime());

		for(Map<Object, Object> a : elements ) {
			ProductSetModel productSetModel = new ProductSetModel();
			Product component = new Product();
			Product subProduct=productDao.findById((Integer)a.get("product_id")).get();
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
			component.setCreatedAtDateTime(subProduct.getCreatedAtDateTime());
			component.setUpdatedAtDateTime(subProduct.getUpdatedAtDateTime());
			component.setUserId((Integer)a.get("user_id"));
			productSetModel.setProduct(component);
			productSetModel.setQuantity((Integer)a.get("quantity"));
			productList.add(productSetModel);
		}
		componentSet.setProducts(productList);
		return componentSet;
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

