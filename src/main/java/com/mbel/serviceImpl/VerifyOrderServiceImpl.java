package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.dao.ProductDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.model.Order;
import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;

@Service("VerifyOrderServiceImpl")
public class VerifyOrderServiceImpl {

	@Autowired
	ProductDao productDao;

	@Autowired
	ForecastServiceImpl forecastServiceImpl;

	@Autowired
	OrderServiceImpl orderServiceImpl;

	@Autowired
	ProductServiceImpl productServiceImpl;

	public ResponseEntity<Map<String, List<ProductSetModel>>> getForecastOrderStatus(@Valid int productId, @Valid LocalDateTime dueDate, @Valid int amountRequired) {


		List<Order> order =orderServiceImpl.getActiveOrders();
		List<Order>unfulfilledDueDateOrder=getUnfulfilledOrder(order,dueDate);
		List<Order>sortedOrder=forecastServiceImpl.getSortedOrder(unfulfilledDueDateOrder);
		return productStockCheck(productId,sortedOrder,amountRequired,dueDate);
	}
	private ResponseEntity<Map<String, List<ProductSetModel>>> productStockCheck(@Valid int productId, List<Order> sortedOrder,
			@Valid int amountRequired, @Valid LocalDateTime dueDate) {
		Map<Product,Integer>productDetails=new HashMap<>();
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		Map<Integer,List<Integer>>incomingShipmentMap=new HashMap<>();
		for(Order unfulfilledorder:sortedOrder) {
			PopulateOrderDto order=orderServiceImpl.getOrderById(unfulfilledorder.getOrderId());
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			FetchProductSetDto productSet =new FetchProductSetDto();
			for(FetchOrderdProducts product:orderdProducts) {
				checkProductStatus(product,productSet,productDetails,
						productQuantityMap,unfulfilledorder,incomingShipmentMap,dueDate);
			}
			productDetails.clear();
		}
		List<ProductSetModel> productSetModelList = new ArrayList<>();
		Optional<Product> availProduct = productDao.findById(productId);
		if(availProduct.isPresent()&&!availProduct.get().isSet()) {
			if(productQuantityMap.containsKey(productId)) {
				verifySingleProduct(productId,productQuantityMap,productSetModelList,amountRequired,dueDate);
			}else {
				verifyStockQuantityProduct(availProduct.get(),productSetModelList,amountRequired,dueDate);

			}
		}else {
			if(productQuantityMap.containsKey(productId)) {
				FetchProductSetDto fetchProductSet = productServiceImpl.getProductSetById(productId);
				for(int i=0;i<fetchProductSet.getProducts().size();i++) {
					int individualProductId=fetchProductSet.getProducts().get(i).getProduct().getProductId();
					int packageAmountRequired = amountRequired*fetchProductSet.getProducts().get(i).getQuantity();
					verifySingleProduct(individualProductId,productQuantityMap,productSetModelList,packageAmountRequired,dueDate);
				}
			}else {
				FetchProductSetDto fetchProductSet = productServiceImpl.getProductSetById(productId);
				for(int i=0;i<fetchProductSet.getProducts().size();i++) {
					int individualProductId=fetchProductSet.getProducts().get(i).getProduct().getProductId();
					if(productQuantityMap.containsKey(productId)) {
						int packageAmountRequired = amountRequired*fetchProductSet.getProducts().get(i).getQuantity();
						verifySingleProduct(individualProductId,productQuantityMap,productSetModelList,packageAmountRequired,dueDate);
					}else {
						Optional<Product> productValue = productDao.findById(individualProductId);
						verifyStockQuantityProduct(productValue.get(),productSetModelList,amountRequired,dueDate);
					}
				}
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



	private void verifyStockQuantityProduct(Product product, List<ProductSetModel> productSetModelList,
			@Valid int amountRequired, @Valid LocalDateTime dueDate) {
		if(amountRequired>product.getQuantity()) {
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setQuantity(amountRequired);
			productSetModel.setCurrentQuantity(product.getQuantity());
			productSetModel.setProduct(product);
			productSetModel.setRequiredQuantity(amountRequired);
			productSetModel.setForecast(false);
			productSetModel.setMod(dueDate.minusWeeks(product.getLeadTime()+3));

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
			productSetModel.setMod(dueDate.minusWeeks(productQuantityMap.get(productId).getProduct().getLeadTime()+3));
			
			productSetModelList.add(productSetModel);
		}


	}
	private void checkProductStatus(FetchOrderdProducts product, FetchProductSetDto productSet,
			Map<Product, Integer> productDetails, Map<Integer, Mappingfields> productQuantityMap, 
			Order unfulfilledorder, Map<Integer, List<Integer>> incomingShipmentMap, @Valid LocalDateTime dueDate) {
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			productStockCaluculate(product,productId,productDetails,productQuantityMap,unfulfilledorder,incomingShipmentMap,dueDate);

		}else {
			List<Product>productInfo =new ArrayList<>();
			productSet = productServiceImpl.getProductSetById(productId);
			for(ProductSetModel individualProduct:productSet.getProducts()) {
				productSetStockCaluculate(product,individualProduct,productDetails,productQuantityMap,unfulfilledorder,incomingShipmentMap,dueDate);
				productInfo.add(individualProduct.getProduct());
			}
		}



	}
	private void productSetStockCaluculate(FetchOrderdProducts product, ProductSetModel individualProduct,
			Map<Product, Integer> productDetails,	Map<Integer, Mappingfields> productQuantityMap, 
			Order unfulfilledorder, Map<Integer, List<Integer>> incomingShipmentMap, @Valid LocalDateTime dueDate) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity=0, orderdQunatity = 0,previousOrderQuantity = 0,tillDateQuantity=0;
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		mappingFields.setProduct(individualProduct.getProduct());
		mappingFields.setOrderdQuantity(individualProduct.getQuantity());
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setSet(true);
		stockQuantity=individualProduct.getProduct().getQuantity();
		if(!productQuantityMap.containsKey(individualProduct.getProduct().getProductId())) {
			tillDateQuantity =forecastServiceImpl.getTillDateQuantity(individualProduct.getProduct(),stockQuantity,dueDate,incomingShipmentMap);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
		}else {
			 stockQuantity = productQuantityMap.get(individualProduct.getProduct().getProductId()).getAvailableStockQuantity();
			tillDateQuantity =forecastServiceImpl.getTillDateQuantity(individualProduct.getProduct(),
					stockQuantity,dueDate,incomingShipmentMap);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);

		}

	}
	private void productStockCaluculate(FetchOrderdProducts product, int productId,
			Map<Product, Integer> productDetails, Map<Integer, Mappingfields> productQuantityMap, Order unfulfilledorder, 
			Map<Integer, List<Integer>> incomingShipmentMap, @Valid LocalDateTime dueDate) {
		int stockQuantity=0,orderdQunatity = 0,previousOrderQuantity = 0,tillDateQuantity=0;
		Mappingfields mappingFields =new Mappingfields();
		Optional<Product> productValue = productDao.findById(productId);
		orderdQunatity=product.getQuantity();
		stockQuantity =productValue.get().getQuantity();
		mappingFields.setProduct(productValue.get());
		mappingFields.setOrderdQuantity(orderdQunatity);
		mappingFields.setRequiredQuantity(orderdQunatity);
		if(!productQuantityMap.containsKey(productId)) {
			tillDateQuantity =forecastServiceImpl.getTillDateQuantity(productValue.get(),stockQuantity,dueDate,incomingShipmentMap);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(productId, mappingFields);
		}else {
			 stockQuantity = productQuantityMap.get(productId).getAvailableStockQuantity();
			tillDateQuantity =forecastServiceImpl.getTillDateQuantity(productValue.get(),stockQuantity,dueDate,incomingShipmentMap);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(productId, mappingFields);

		}
	}
	private List<Order> getUnfulfilledOrder(List<Order> order, @Valid LocalDateTime dueDate) {
		return order.stream()
				.filter(predicate->predicate.isFulfilled()==false 
				&& predicate.getDueDate().isBefore(dueDate) || predicate.getDueDate().isEqual(dueDate))
				.collect(Collectors.toList());
	}



}
