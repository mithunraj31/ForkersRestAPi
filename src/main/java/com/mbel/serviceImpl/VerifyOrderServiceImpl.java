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

	public ProductSetModel getForecastOrderStatus(@Valid int productId, @Valid LocalDateTime dueDate, @Valid int amountRequired) {
		
		
		List<Order> order =orderServiceImpl.getActiveOrders();
		List<Order>unfulfilledDueDateOrder=getUnfulfilledOrder(order,dueDate);
		List<Order>sortedOrder=forecastServiceImpl.getSortedOrder(unfulfilledDueDateOrder);
		return productStockCheck(productId,sortedOrder,amountRequired,dueDate);
	}
	private ProductSetModel productStockCheck(@Valid int productId, List<Order> sortedOrder, @Valid int amountRequired, @Valid LocalDateTime dueDate) {
		Map<Product,Integer>productDetails=new HashMap<>();
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		for(Order unfulfilledorder:sortedOrder) {
			List<FetchOrderdProducts> fetchOrderedproductsList =new ArrayList<>();
			PopulateOrderDto order=orderServiceImpl.getOrderById(unfulfilledorder.getOrderId());
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			FetchProductSetDto productSet =new FetchProductSetDto();
			for(FetchOrderdProducts product:orderdProducts) {
				checkProductStatus(product,productSet,productDetails,productQuantityMap);
			}
			productDetails.clear();
	}
		ProductSetModel productSetModel = new ProductSetModel();
		if(productQuantityMap.containsKey(productId)) {
			Optional<Product> availProduct = productDao.findById(productId);
			int tillDateQuantity =productQuantityMap.get(productId).getAvailableStockQuantity();
			if(amountRequired>tillDateQuantity) {				
				productSetModel.setCurrentQuantity(tillDateQuantity);
				productSetModel.setRequiredQuantity(amountRequired);
				productSetModel.setForecast(false);
				productSetModel.setMod(dueDate.minusWeeks(availProduct.get().getLeadTime()+1));
				
			}else {
				productSetModel.setForecast(true);
				
			}
		}else {
			productSetModel.setForecast(true);
			
		}
		return productSetModel;
		
	}
	private void checkProductStatus(FetchOrderdProducts product, FetchProductSetDto productSet,
			Map<Product, Integer> productDetails, Map<Integer, Mappingfields> productQuantityMap) {
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			productStockCaluculate(product,productId,productDetails,productQuantityMap);

		}else {
				 productSet = productServiceImpl.getProductSetById(productId);
				for(ProductSetModel individualProduct:productSet.getProducts()) {
					productSetStockCaluculate(product,individualProduct,productDetails,productQuantityMap);
				

			}
		}

	
		
	}
	private void productSetStockCaluculate(FetchOrderdProducts product, ProductSetModel individualProduct,
		 Map<Product, Integer> productDetails,
			Map<Integer, Mappingfields> productQuantityMap) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity = 0,orderdQunatity = 0,previousOrderQuantity = 0;
		stockQuantity =individualProduct.getProduct().getQuantity();			
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		productDetails.put(product.getProduct(), product.getQuantity());
		mappingFields.setRequiredQuantity(orderdQunatity);
		if(!productQuantityMap.containsKey(individualProduct.getProduct().getProductId())) {
			mappingFields.setCurrentQuantity(stockQuantity);
			mappingFields.setAvailableStockQuantity(stockQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
		}else {
			stockQuantity =productQuantityMap.get(individualProduct.getProduct().getProductId()).getCurrentQuantity();
			int availableStockQuantity = productQuantityMap.get(individualProduct.getProduct().getProductId()).getAvailableStockQuantity();
			previousOrderQuantity=productQuantityMap.get(individualProduct.getProduct().getProductId()).getRequiredQuantity();
				mappingFields.setCurrentQuantity(stockQuantity-previousOrderQuantity);
				mappingFields.setAvailableStockQuantity(availableStockQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
			
		}
		
		
	}
	private void productStockCaluculate(FetchOrderdProducts product, int productId,
			Map<Product, Integer> productDetails, Map<Integer, Mappingfields> productQuantityMap) {
		int stockQuantity = 0,orderdQunatity = 0,previousOrderQuantity = 0;
		Mappingfields mappingFields =new Mappingfields();
		Optional<Product> productValue = productDao.findById(productId);
		if(productValue.isPresent()) {
			stockQuantity =productValue.get().getQuantity();
		}				
		orderdQunatity=product.getQuantity();
		productDetails.put(productValue.get(),orderdQunatity);
		mappingFields.setRequiredQuantity(orderdQunatity);
		
		if(!productQuantityMap.containsKey(productId)) {
			mappingFields.setCurrentQuantity(stockQuantity);
			mappingFields.setAvailableStockQuantity(stockQuantity-orderdQunatity);
			productQuantityMap.put(productId, mappingFields);
		}else {
			stockQuantity =productQuantityMap.get(productId).getCurrentQuantity();
			int availableStockQuantity = productQuantityMap.get(productId).getAvailableStockQuantity();
			previousOrderQuantity=productQuantityMap.get(productId).getRequiredQuantity();
			mappingFields.setCurrentQuantity((stockQuantity)-(previousOrderQuantity));
			mappingFields.setAvailableStockQuantity(availableStockQuantity-orderdQunatity);
			productQuantityMap.put(productId, mappingFields);
			
		}

	
		
	}
	private List<Order> getUnfulfilledOrder(List<Order> order, @Valid LocalDateTime dueDate) {
		return order.stream()
				.filter(predicate->predicate.isFulfilled()==false 
				&& predicate.getDueDate().isBefore(dueDate) || predicate.getDueDate().isEqual(dueDate))
				.collect(Collectors.toList());
//		for(Order orderCheck:order) {
//			if(!orderCheck.isFulfilled()&&orderCheck.getDueDate().isBefore(dueDate)
//					||orderCheck.getDueDate().isEqual(dueDate)) {
//				dueDateOrders.add(orderCheck);
//				
//			}
//		}
//		return dueDateOrders;
}
	


}
