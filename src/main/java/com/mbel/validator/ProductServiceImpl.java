//package com.mbel.serviceImpl;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.mbel.dao.ProductDao;
//import com.mbel.dao.ProductSetDao;
//import com.mbel.dto.FetchProductSetDto;
//import com.mbel.dto.SaveProductSetDto;
//import com.mbel.model.Product;
//import com.mbel.model.ProductSet;
//import com.mbel.model.ProductSetModel;
//
//
//@Service("ProductServiceImpl")
//public class ProductServiceImpl  {
//	@Autowired 
//	ProductDao productDao;
//
//	@Autowired 
//	ProductSetDao productSetDao;
//
//
//	public Product save(Product product) {
//		product.setCreatedAtDateTime(LocalDateTime.now());
//		product.setUpdatedAtDateTime(LocalDateTime.now());
//		return productDao.save(product);
//	}
//
//	public List<Product> getAllProducts() {
//		List<Product>product =productDao.findAll();
//		for(int i=0;i<product.size();i++) {
//			if(!product.get(i).isActive()) {
//				product.remove(product.get(i));
//			}
//		}
//		return product;
//	}
//
//	public Optional<Product> getProductsById(int productId) {
//		return productDao.findById(productId);
//	}
//
//	public FetchProductSetDto saveProductSet(SaveProductSetDto productSet) {
//		Product product = new Product();
//		product.setProductName(productSet.getProductName());
//		product.setDescription(productSet.getDescription());
//		product.setPrice(productSet.getPrice());
//		product.setMoq(productSet.getMoq());
//		product.setLeadTime(productSet.getLeadTime());
//		product.setObicNo(productSet.getObicNo());
//		product.setQuantity(productSet.getQuantity());
//		product.setSet(true);
//		product.setActive(productSet.isActive());
//		product.setCreatedAtDateTime(LocalDateTime.now());
//		product.setUpdatedAtDateTime(LocalDateTime.now());
//		product.setUserId(productSet.getUserId());
//		productDao.save(product);
//		int id  = product.getProductId();
//		int setValue  =productSet.getProductset().size();
//		int setId = 0 ;
//		for(int i=0;i<setValue;i++) {
//			ProductSet newProductSet = new ProductSet();
//			newProductSet.setSetId(id);
//			newProductSet.setQuantity(productSet.getProductset().get(i).getQty());
//			newProductSet.setProductComponentId(productSet.getProductset().get(i).getProductcomponentId());
//			productSetDao.save(newProductSet);
//			setId  = newProductSet.getProductSetId();
//
//		}
//		return getProductSetById(setId);
//	}
//
//	public List<FetchProductSetDto> getAllProductSet() {
//		List<FetchProductSetDto> fetchList =new ArrayList<>();
//		List<ProductSetModel> productList = new ArrayList<>();
//
//		//List<Map<Object, Object>> elements = productSetDao.getAll();
//		List<Product> proCheck = productDao.findAll();
//		List<Product> proSet= new ArrayList<>();
//		
//		for(int j=0;j<proCheck.size();j++) {
//			if(proCheck.get(j).isSet() && proCheck.get(j).isActive()) {
//				proSet.add(proCheck.get(j));
//			}
//		}
//		for(int i=0;i<proSet.size();i++) {
//			int k =0;
//			FetchProductSetDto componentSet= new FetchProductSetDto();
//			componentSet.setProductId((Integer)proSet.get(i).getProductId());
//			componentSet.setProductName((String)proSet.get(i).getProductName());
//			componentSet.setDescription((String)proSet.get(i).getDescription());
//			componentSet.setPrice((Double)proSet.get(i).getPrice());
//			componentSet.setMoq((Integer)proSet.get(i).getMoq());
//			componentSet.setLeadTime((Integer)proSet.get(i).getLeadTime());
//			componentSet.setObicNo((String)proSet.get(i).getObicNo());
//			componentSet.setQuantity((Integer)proSet.get(i).getQuantity());
//			componentSet.setSet((boolean)proSet.get(i).isSet());
//			componentSet.setActive((boolean)proSet.get(i).isActive());
//			componentSet.setCreatedAtDateTime(proSet.get(i).getCreatedAtDateTime());
//			componentSet.setUpdatedAtDateTime(proSet.get(i).getUpdatedAtDateTime());
//			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proSet.get(i).getProductId());
//			for(int l=0;l< productsetList.size();l++ ) {
//				List<Map<Object, Object>> elem = productSetDao.getAll();
//			ProductSetModel productSetModel = new ProductSetModel();
//			Product component = new Product();
//			Product subProduct=productDao.findById((Integer)elem.get(k).get("product_id")).get();
//			component.setProductId((Integer)elem.get(k).get("product_id"));
//			component.setProductName((String)elem.get(k).get("product_name"));
//			component.setDescription((String)elem.get(k).get("description"));
//			component.setPrice((Double)elem.get(k).get("price"));
//			component.setMoq((Integer)elem.get(k).get("moq"));
//			component.setLeadTime((Integer)elem.get(k).get("lead_time"));
//			component.setObicNo((String)elem.get(k).get("obic_no"));
//			component.setQuantity((Integer)elem.get(k).get("qty"));
//			component.setActive((boolean)elem.get(k).get("active"));
//			component.setSet((boolean)elem.get(k).get("is_set"));
//			component.setCreatedAtDateTime(subProduct.getCreatedAtDateTime());
//			component.setUpdatedAtDateTime(subProduct.getUpdatedAtDateTime());
//			component.setUserId((Integer)elem.get(k).get("user_id"));
//			productSetModel.setProduct(component);
//			productSetModel.setQuantity((Integer)elem.get(k).get("quantity"));
//			productList.add(productSetModel);
//			k++;
//			
//		}
//			componentSet.setProducts(productList);
//			fetchList.add(componentSet);
//			
//		}
//		
//		
//		
//			
//		
//		return fetchList;
//	}
//
//	public FetchProductSetDto getProductSetById(int productId) {
//		FetchProductSetDto componentSet= new FetchProductSetDto();
//		List<ProductSetModel> productList = new ArrayList<>();
//		List<Map<Object, Object>> elements = productSetDao.getProductSetsById(productId);
//		Product mainProduct=productDao.findById(productId).get();
//		componentSet.setProductId((Integer)elements.get(0).get("package_id"));
//		componentSet.setProductName((String)elements.get(0).get("package_name"));
//		componentSet.setDescription((String)elements.get(0).get("package_desc"));
//		componentSet.setPrice((Double)elements.get(0).get("package_price"));
//		componentSet.setMoq((Integer)elements.get(0).get("package_moq"));
//		componentSet.setLeadTime((Integer)elements.get(0).get("package_lead"));
//		componentSet.setObicNo((String)elements.get(0).get("package_obic"));
//		componentSet.setQuantity((Integer)elements.get(0).get("package_qty"));
//		componentSet.setSet((boolean)elements.get(0).get("package_set"));
//		componentSet.setActive((boolean)elements.get(0).get("package_active"));
//		componentSet.setCreatedAtDateTime(mainProduct.getCreatedAtDateTime());
//		componentSet.setUpdatedAtDateTime(mainProduct.getUpdatedAtDateTime());
//
//		for(Map<Object, Object> a : elements ) {
//			ProductSetModel productSetModel = new ProductSetModel();
//			Product component = new Product();
//			Product subProduct=productDao.findById((Integer)a.get("product_id")).get();
//			component.setProductId((Integer)a.get("product_id"));
//			component.setProductName((String)a.get("product_name"));
//			component.setDescription((String)a.get("description"));
//			component.setPrice((Double)a.get("price"));
//			component.setMoq((Integer)a.get("moq"));
//			component.setLeadTime((Integer)a.get("lead_time"));
//			component.setObicNo((String)a.get("obic_no"));
//			component.setQuantity((Integer)a.get("qty"));
//			component.setActive((boolean)a.get("active"));
//			component.setSet((boolean)a.get("is_set"));
//			component.setCreatedAtDateTime(subProduct.getCreatedAtDateTime());
//			component.setUpdatedAtDateTime(subProduct.getUpdatedAtDateTime());
//			component.setUserId((Integer)a.get("user_id"));
//			productSetModel.setProduct(component);
//			productSetModel.setQuantity((Integer)a.get("quantity"));
//			productList.add(productSetModel);
//		}
//		componentSet.setProducts(productList);
//		return componentSet;
//	}
//
//	public Optional<Product> getupdateById(int productId, @Valid Product productionDetails) {
//		Product product = productDao.findById(productId).get();
//		product.setProductName(productionDetails.getProductName());
//		product.setDescription(productionDetails.getDescription());
//		product.setPrice(productionDetails.getPrice());
//		product.setMoq(productionDetails.getMoq());
//		product.setLeadTime(productionDetails.getLeadTime());
//		product.setObicNo(productionDetails.getObicNo());
//		product.setQuantity(productionDetails.getQuantity());
//		product.setSet(productionDetails.isSet());
//		product.setActive(productionDetails.isActive());
//		product.setUpdatedAtDateTime(LocalDateTime.now());
//		product.setUserId(productionDetails.getUserId());
//		productDao.save(product);
//		return productDao.findById(productId);
//	}
//
//	public Optional<Product> deleteProductById(int productId) {
//		Product product = productDao.findById(productId).get();
//		product.setActive(false);
//		productDao.save(product);
//		return productDao.findById(productId);
//	}
//
//	public FetchProductSetDto getupdateProductSetById(int productId, @Valid SaveProductSetDto productSetDetails) {
//		Product product = productDao.findById(productId).get();
//		product.setProductName(productSetDetails.getProductName());
//		product.setDescription(productSetDetails.getDescription());
//		product.setPrice(productSetDetails.getPrice());
//		product.setMoq(productSetDetails.getMoq());
//		product.setLeadTime(productSetDetails.getLeadTime());
//		product.setObicNo(productSetDetails.getObicNo());
//		product.setQuantity(productSetDetails.getQuantity());
//		product.setSet(true);
//		product.setActive(productSetDetails.isActive());
//		product.setUpdatedAtDateTime(LocalDateTime.now());
//		product.setUserId(productSetDetails.getUserId());
//		productDao.save(product);
//		int setValue  =productSetDetails.getProductset().size();
//		ProductSet productSet = new ProductSet();
//		for(int i=0;i<setValue;i++) {
//			 productSetDao.deleteBySet(productId,productSetDetails.getProductset().get(i).getProductcomponentId());
//		}
//		for(int i=0;i<setValue;i++) {
//			productSet.setSetId(productId);
//			productSet.setQuantity(productSetDetails.getProductset().get(i).getQty());
//			productSet.setProductComponentId(productSetDetails.getProductset().get(i).getProductcomponentId());
//			productSetDao.save(productSet);
//		}
//
//
//		return getProductSetById(productId);
//
//
//	}
//
//	public FetchProductSetDto deleteProductSetById(int productId) {
//		Product product = productDao.findById(productId).get();
//		product.setActive(false);
//		productDao.save(product);
//		return getProductSetById(productId);
//
//
//	}
//
//
//}
//
