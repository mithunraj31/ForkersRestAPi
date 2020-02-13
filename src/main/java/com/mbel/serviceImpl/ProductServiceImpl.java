package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
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

	@Autowired
	private JwtAuthenticationFilter jwt;



	public Product save(Product product) {

		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(jwt.getUserdetails().getUserId());
		product.setActive(true);
		product.setSet(false);
		return productDao.save(product);
	}

	public List<Product> getAllProducts() {
		List<Product>product =productDao.findAll();
		return product.stream()
				.filter(predicate->predicate.isActive()
						&&!predicate.isSet())
				.collect(Collectors.toList());
	}

	public List<Product> getAllActiveProductset() {
		List<Product>product =productDao.findAll();
		return product.stream()
				.filter(predicate->predicate.isActive()
						&&predicate.isSet())
				.collect(Collectors.toList());
	}

	public Optional<Product> getProductsById(int productId) {
		return productDao.findById(productId);
	}

	public Product saveProductSet(SaveProductSetDto productSet) {
		Product product = new Product();
		product.setProductName(productSet.getProductName());
		product.setDescription(productSet.getDescription());
		product.setPrice(productSet.getPrice());
		product.setMoq(productSet.getMoq());
		product.setLeadTime(productSet.getLeadTime());
		product.setObicNo(productSet.getObicNo());
		product.setQuantity(productSet.getQuantity());
		product.setSet(true);
		product.setActive(true);
		product.setCreatedAtDateTime(LocalDateTime.now());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(jwt.getUserdetails().getUserId());
		product.setCurrency(productSet.getCurrency());
		Product productsave=productDao.save(product);
		int id  = product.getProductId();
		if(productSet.getProducts() != null) {
			int setValue  =productSet.getProducts().size();
			for(int i=0;i<setValue;i++) {
				ProductSet newProductSet = new ProductSet();
				newProductSet.setSetId(id);
				newProductSet.setQuantity(productSet.getProducts().get(i).getQuantity());
				newProductSet.setProductComponentId(productSet.getProducts().get(i).getProductId());
				productSetDao.save(newProductSet);
			}

		}
		return productsave;
	}

	public List<FetchProductSetDto> getAllProductSet() {
		List<FetchProductSetDto> fetchList =new ArrayList<>();
		List<Product> proSet = getAllActiveProductset();
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
			componentSet.setCurrency(proSet.get(i).getCurrency());
			if((Integer)proSet.get(i).getProductId()!= null) {
				List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proSet.get(i).getProductId());
				if(productsetList != null) {
					for(int l=0;l< productsetList.size();l++ ) {
						ProductSetModel productSetModel = new ProductSetModel();
						if((Integer) productsetList.get(l).get(Constants.PRODUCT_COMPONENT_ID) != 0) {
							Product component =productDao.findById((Integer) productsetList.get(l).get(Constants.PRODUCT_COMPONENT_ID)).orElse(null);
							productSetModel.setProduct(component);
							productSetModel.setQuantity((Integer)productsetList.get(l).get(Constants.QTY));
							productList.add(productSetModel);
						}
					}
				}
			}
			componentSet.setProducts(productList);
			fetchList.add(componentSet);
		}

		return fetchList;
	}

	public FetchProductSetDto getProductSetById(int productId) {
		Product proCheck = getProductsById(productId).orElse(null);
		List<ProductSetModel> productList = new ArrayList<>();
		FetchProductSetDto componentSet= new FetchProductSetDto();
		if(proCheck!=null) {
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
		componentSet.setUserId(proCheck.getUserId());
		componentSet.setCreatedAtDateTime(proCheck.getCreatedAtDateTime());
		componentSet.setUpdatedAtDateTime(proCheck.getUpdatedAtDateTime());
		componentSet.setCurrency(proCheck.getCurrency());
		if(proCheck.isSet()) {
			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proCheck.getProductId());
			for(int l=0;l< productsetList.size();l++ ) {
				ProductSetModel productSetModel = new ProductSetModel();
				Product component =productDao.findById((Integer) productsetList.get(l).get(Constants.PRODUCT_COMPONENT_ID)).orElse(null);
				productSetModel.setProduct(component);
				productSetModel.setQuantity((Integer)productsetList.get(l).get(Constants.QTY));
				productList.add(productSetModel);
			}
		}
		}
		componentSet.setProducts(productList);
		return componentSet;



	}

	public Product getupdateById(int productId, @Valid Product productionDetails) {
		Product product = productDao.findById(productId).orElse(null);
		if(product!=null) {
		product.setProductName(productionDetails.getProductName());
		product.setDescription(productionDetails.getDescription());
		product.setPrice(productionDetails.getPrice());
		product.setMoq(productionDetails.getMoq());
		product.setLeadTime(productionDetails.getLeadTime());
		product.setObicNo(productionDetails.getObicNo());
		product.setQuantity(productionDetails.getQuantity());
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(jwt.getUserdetails().getUserId());
		product.setCurrency(productionDetails.getCurrency());
		return productDao.save(product);
		}
		return product;
	}

	public Product deleteProductById(int productId) {
		Product product = productDao.findById(productId).orElse(null);
		if(product!=null) {
		product.setActive(false);
		return productDao.save(product);
		}
		return product;
	}

	public Product getupdateProductSetById(int productId, @Valid SaveProductSetDto productSetDetails) {
		Product product = productDao.findById(productId).orElse(null);
		if(product!=null) {
		product.setProductName(productSetDetails.getProductName());
		product.setDescription(productSetDetails.getDescription());
		product.setPrice(productSetDetails.getPrice());
		product.setMoq(productSetDetails.getMoq());
		product.setLeadTime(productSetDetails.getLeadTime());
		product.setObicNo(productSetDetails.getObicNo());
		product.setQuantity(productSetDetails.getQuantity());
		product.setSet(true);
		product.setActive(true);
		product.setUpdatedAtDateTime(LocalDateTime.now());
		product.setUserId(jwt.getUserdetails().getUserId());
		product.setCurrency(productSetDetails.getCurrency());
		Product productupdate=productDao.save(product);
		int setValue  =productSetDetails.getProducts().size();
		productSetDao.deleteBySet(productId);
		for(int i=0;i<setValue;i++) {
			ProductSet productSet = new ProductSet();
			productSet.setSetId(productId);
			productSet.setQuantity(productSetDetails.getProducts().get(i).getQuantity());
			productSet.setProductComponentId(productSetDetails.getProducts().get(i).getProductId());
			productSetDao.save(productSet);
		}


		return productupdate;
		}
		return product;

	}

	public Product deleteProductSetById(int productId) {
		return deleteProductById(productId);
	}


}

