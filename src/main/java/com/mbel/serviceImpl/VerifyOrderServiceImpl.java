package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.dao.OrderDao;
import com.mbel.dao.ProductDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.model.Order;
import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;

@Service("VerifyOrderServiceImpl")
public class VerifyOrderServiceImpl {

	@Autowired
	ProductDao productDao;
	
	@Autowired
	OrderDao orderDao;

	@Autowired
	ForecastServiceImpl forecastServiceImpl;

	@Autowired
	OrderServiceImpl orderServiceImpl;

	@Autowired
	ProductServiceImpl productServiceImpl;

	public ResponseEntity<Map<String, List<ProductSetModel>>> getForecastOrderStatus(@Valid int productId, @Valid LocalDateTime dueDate, @Valid int amountRequired) {
		List<Order>unfulfilledDueDateOrder=getUnfulfilledOrder(dueDate);
		List<Order>sortedOrder=forecastServiceImpl.getSortedOrder(unfulfilledDueDateOrder);
		return productStockCheck(productId,sortedOrder,amountRequired,dueDate);
	}
	private ResponseEntity<Map<String, List<ProductSetModel>>> productStockCheck(@Valid int productId, List<Order> sortedOrder,
			@Valid int amountRequired, @Valid LocalDateTime dueDate) {
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		Map<Integer,List<Integer>>incomingShipmentMap=new HashMap<>();
		for(Order unfulfilledorder:sortedOrder) {
			Map<Integer, List<Mappingfields>>productDetails=new HashMap<>();
			List<String> forecastString=new ArrayList<>();
			List<FetchOrderdProducts> orderdProducts= orderServiceImpl.getAllProducts(unfulfilledorder.getOrderId());
			for(FetchOrderdProducts product:orderdProducts) {
				forecastServiceImpl.checkProductStatus(product,unfulfilledorder.getDueDate(),productDetails,
						productQuantityMap,incomingShipmentMap,forecastString);
			}
			
		}
		
		return verifyProductStatus(productId,productQuantityMap,amountRequired,dueDate);
		
	}



	private ResponseEntity<Map<String, List<ProductSetModel>>> verifyProductStatus(@Valid int productId,
			Map<Integer, Mappingfields> productQuantityMap, @Valid int amountRequired, @Valid LocalDateTime dueDate) {
		List<ProductSetModel> productSetModelList = new ArrayList<>();
		Product availProduct = productDao.findById(productId).orElse(null);
		if(productQuantityMap.containsKey(productId)) {
			if(Objects.nonNull(availProduct)&&!availProduct.isSet()) {
			verifySingleProduct(productId,productQuantityMap,productSetModelList,amountRequired,dueDate);
			}else {
				FetchProductSetDto fetchProductSet = productServiceImpl.getProductSetById(productId);
				for(int i=0;i<fetchProductSet.getProducts().size();i++) {
					int individualProductId=fetchProductSet.getProducts().get(i).getProduct().getProductId();
					int packageAmountRequired = amountRequired*fetchProductSet.getProducts().get(i).getQuantity();
					verifySingleProduct(individualProductId,productQuantityMap,productSetModelList,packageAmountRequired,dueDate);
			}
			}
		}else {
			if(Objects.nonNull(availProduct)){
			verifyNewIncomingProduct(productId,productQuantityMap,amountRequired,dueDate,productSetModelList,availProduct);
			}
		}
	
		Map<String, List<ProductSetModel> > response = new HashMap<>();
		if(! productSetModelList.isEmpty()) {			
		 response.put("Following Products cannot be delivered", productSetModelList);
		 return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.NOT_ACCEPTABLE);
		}else {
			response.put("Products can be delivered", productSetModelList);
			return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.ACCEPTED);
	}
	}
	private void verifyNewIncomingProduct(@Valid int productId, Map<Integer, Mappingfields> productQuantityMap,
			@Valid int amountRequired, @Valid LocalDateTime dueDate, List<ProductSetModel> productSetModelList, Product availProduct) {

		if(!productQuantityMap.get(productId).isSet()) {
		verifyStockQuantityProduct(availProduct,productSetModelList,amountRequired,dueDate);	
		}else {
			FetchProductSetDto fetchProductSet = productServiceImpl.getProductSetById(productId);
			for(int i=0;i<fetchProductSet.getProducts().size();i++) {
				int individualProductId=fetchProductSet.getProducts().get(i).getProduct().getProductId();
				if(productQuantityMap.containsKey(productId)) {
					int packageAmountRequired = amountRequired*fetchProductSet.getProducts().get(i).getQuantity();
					verifySingleProduct(individualProductId,productQuantityMap,productSetModelList,packageAmountRequired,dueDate);
				}else {
				Product productValue = productDao.findById(individualProductId).orElse(null);
				if(Objects.nonNull(productValue)) {
					verifyStockQuantityProduct(productValue,productSetModelList,amountRequired,dueDate);
				}
				}
			}
			
		}
	
		
	}
	private void verifyStockQuantityProduct(Product product, List<ProductSetModel> productSetModelList,
			@Valid int amountRequired, @Valid LocalDateTime dueDate) {
		if(amountRequired>product.getQuantity()) {
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setQuantity(amountRequired);
			productSetModel.setCurrentQuantity(product.getQuantity());
			productSetModel.setProduct(product);
			productSetModel.setRequiredQuantity(amountRequired);
			productSetModel.setForecast(false);
			productSetModel.setMod(dueDate.minusWeeks(product.getLeadTime()+3L));

			productSetModelList.add(productSetModel);
		}


	}




	private void verifySingleProduct(@Valid int productId,
			Map<Integer, Mappingfields> productQuantityMap, List<ProductSetModel> productSetModelList, 
			@Valid int amountRequired, @Valid LocalDateTime dueDate) {
		int	tillDateQuantity =productQuantityMap.get(productId).getAvailableStockQuantity();
		if(amountRequired>tillDateQuantity) {	
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setQuantity(amountRequired);
			productSetModel.setCurrentQuantity(productQuantityMap.get(productId).getAvailableStockQuantity());
			productSetModel.setProduct(productQuantityMap.get(productId).getProduct());
			productSetModel.setRequiredQuantity(amountRequired);
			productSetModel.setForecast(false);
			productSetModel.setMod(dueDate.minusWeeks(productQuantityMap.get(productId).getProduct().getLeadTime()+3L));
			productSetModelList.add(productSetModel);
		}


	}
	private List<Order> getUnfulfilledOrder(@Valid LocalDateTime dueDate) {
		List<Order>order =orderDao.findAll(); 
		return order.stream()
				.filter(predicate->!predicate.isFulfilled() && predicate.isActive()
				&& predicate.getDueDate().isBefore(dueDate) || predicate.getDueDate().isEqual(dueDate))
				.collect(Collectors.toList());
	}



}
