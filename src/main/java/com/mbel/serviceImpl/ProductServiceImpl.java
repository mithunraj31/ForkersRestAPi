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

	public List<FetchProductSetDto> getAllProductSet() {
		List<FetchProductSetDto> fetchList =new ArrayList<>();
		List<Product> proCheck = getAllProducts();
		List<Product> proSet = new ArrayList<>(); 
		for(int j=0;j<proCheck.size();j++) {
			if(proCheck.get(j).isSet()) {
				proSet.add(proCheck.get(j));
			}
		}
		for(int i=0;i<proSet.size();i++) {
			List<ProductSetModel> productList = new ArrayList<>();
			FetchProductSetDto componentSet= new FetchProductSetDto();
			componentSet.setProductId(proSet.get(i).getProductId());
			componentSet.setProductName(proSet.get(i).getProductName());
			componentSet.setDescription(proSet.get(i).getDescription());
			componentSet.setPrice(proSet.get(i).getPrice());
			componentSet.setMoq(proSet.get(i).getMoq());
			componentSet.setLeadTime(proSet.get(i).getLeadTime());
			componentSet.setObicNo(proSet.get(i).getObicNo());
			componentSet.setQuantity(proSet.get(i).getQuantity());
			componentSet.setSet(proSet.get(i).isSet());
			componentSet.setActive(proSet.get(i).isActive());
			componentSet.setCreatedAtDateTime(proSet.get(i).getCreatedAtDateTime());
			componentSet.setUpdatedAtDateTime(proSet.get(i).getUpdatedAtDateTime());
			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proSet.get(i).getProductId());
			for(int l=0;l< productsetList.size();l++ ) {
			ProductSetModel productSetModel = new ProductSetModel();
			Product component = new Product();
			component=productDao.findById((Integer) productsetList.get(l).get("product_component_id")).get();
			productSetModel.setProduct(component);
			productSetModel.setQuantity((Integer)productsetList.get(l).get("qty"));
			productList.add(productSetModel);
		}
			componentSet.setProducts(productList);
			fetchList.add(componentSet);
		}
		
		return fetchList;
	}

	public FetchProductSetDto getProductSetById(int productId) {
	Product proCheck = getProductsById(productId).get();
			List<ProductSetModel> productList = new ArrayList<>();
			FetchProductSetDto componentSet= new FetchProductSetDto();
			componentSet.setProductId(proCheck.getProductId());
			componentSet.setProductName(proCheck.getProductName());
			componentSet.setDescription(proCheck.getDescription());
			componentSet.setPrice(proCheck.getPrice());
			componentSet.setMoq(proCheck.getMoq());
			componentSet.setLeadTime(proCheck.getLeadTime());
			componentSet.setObicNo(proCheck.getObicNo());
			componentSet.setQuantity(proCheck.getQuantity());
			componentSet.setSet(proCheck.isSet());
			componentSet.setActive(proCheck.isActive());
			componentSet.setCreatedAtDateTime(proCheck.getCreatedAtDateTime());
			componentSet.setUpdatedAtDateTime(proCheck.getUpdatedAtDateTime());
			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proCheck.getProductId());
			for(int l=0;l< productsetList.size();l++ ) {
			ProductSetModel productSetModel = new ProductSetModel();
			Product component = new Product();
			component=productDao.findById((Integer) productsetList.get(l).get("product_component_id")).get();
			productSetModel.setProduct(component);
			productSetModel.setQuantity((Integer)productsetList.get(l).get("qty"));
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

	public FetchProductSetDto getupdateProductSetById(int productId, @Valid SaveProductSetDto productSetDetails) {
		Product product = productDao.findById(productId).get();
		product.setProductName(productSetDetails.getProductName());
		product.setDescription(productSetDetails.getDescription());
		product.setPrice(productSetDetails.getPrice());
		product.setMoq(productSetDetails.getMoq());
		product.setLeadTime(productSetDetails.getLeadTime());
		product.setObicNo(productSetDetails.getObicNo());
		product.setQuantity(productSetDetails.getQuantity());
		product.setSet(true);
		product.setActive(productSetDetails.isActive());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(productSetDetails.getUserId());
		productDao.save(product);
		int setValue  =productSetDetails.getProductset().size();
		ProductSet productSet = new ProductSet();
		for(int i=0;i<setValue;i++) {
			 productSetDao.deleteBySet(productId,productSetDetails.getProductset().get(i).getProductcomponentId());
		}
		for(int i=0;i<setValue;i++) {
			productSet.setSetId(productId);
			productSet.setQuantity(productSetDetails.getProductset().get(i).getQty());
			productSet.setProductComponentId(productSetDetails.getProductset().get(i).getProductcomponentId());
			productSetDao.save(productSet);
		}


		return getProductSetById(productId);


	}

	public FetchProductSetDto deleteProductSetById(int productId) {
		Product product = productDao.findById(productId).get();
		product.setActive(false);
		productDao.save(product);
		return getProductSetById(productId);


	}


}

