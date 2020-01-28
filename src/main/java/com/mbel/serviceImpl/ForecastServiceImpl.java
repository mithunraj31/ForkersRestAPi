package com.mbel.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.OrderDao;
import com.mbel.dao.ProductDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.ForecastProductDto;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.model.Order;
import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;
import com.mbel.model.ProductStockCheck;

@Service("ForecastServiceImpl")
public class ForecastServiceImpl {

	@Autowired
	OrderServiceImpl orderServiceImpl;

	@Autowired
	FullfillOrderServiceImpl fullfillOrderServiceImpl; 

	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired
	ProductDao productDao;
	
	@Autowired
	OrderDao orderDao;

	public List<ForecastProductDto> getForecastOrderDetails() {

		List<Order> order =orderServiceImpl.getActiveOrders();
		List<Order>unfulfilledOrder=getUnfulfilledOrder(order);
		List<Order>sortedOrder=getSortedOrder(unfulfilledOrder);
		return calculateStock(sortedOrder);


	}

	private List<ForecastProductDto> calculateStock(List<Order> sortedOrder) {
		List<ForecastProductDto> forecastProductDtoList =new ArrayList<>();
		Map<Product,Integer>productDetails=new HashMap<>();
		Map<Integer,Integer>currentProductQuantityMap=new HashMap<>();
		for(Order unfulfilledorder:sortedOrder) {
			ForecastProductDto forecastProductDto=new ForecastProductDto();
			PopulateOrderDto order=orderServiceImpl.getOrderById(unfulfilledorder.getOrderId());
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			for(FetchOrderdProducts product:orderdProducts) {
				int productId = product.getProduct().getProductId();
				if(!product.getProduct().isSet()) {
					productStockCaluculate(product,productId,unfulfilledorder,productDetails,currentProductQuantityMap);

				}else {
					int packageStockQuantity = 0,packageOrderdQunatity=0,packagecurrentQuantity=0;
					packageStockQuantity =product.getProduct().getQuantity();
					packageOrderdQunatity=product.getQuantity();
					
					updateCurrentQuantity(productId,packageOrderdQunatity,packageStockQuantity,currentProductQuantityMap,unfulfilledorder);
					productDetails.put(product.getProduct(), packageOrderdQunatity);
					if(unfulfilledorder.isForecast()) {
						packagecurrentQuantity=packageStockQuantity - packageOrderdQunatity;
						FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
						for(ProductSetModel individualProduct:productSet.getProducts()) {
							productSetStockCaluculate(product,individualProduct,unfulfilledorder,productDetails,currentProductQuantityMap);
						}

					}
				}

			}
			forecastProductDto.setActive(unfulfilledorder.isActive());
			forecastProductDto.setDueDate(unfulfilledorder.getDueDate());
			forecastProductDto.setForecast(unfulfilledorder.isForecast());
			forecastProductDto.setFullfilled(unfulfilledorder.isFullfilled());
			forecastProductDto.setOrderId(unfulfilledorder.getOrderId());
			forecastProductDto.setProposalNo(unfulfilledorder.getProposalNo());
			forecastProductDto.setReceivedDate(unfulfilledorder.getReceivedDate());
			Set<Entry<Product, Integer>>product=productDetails.entrySet();
			List<ProductStockCheck> productStockCheckList = new ArrayList<>();
			for(Entry<Product, Integer> update:product) {				
				ProductStockCheck productStockCheck = new ProductStockCheck();
				productStockCheck.setProduct(update.getKey());
				productStockCheck.setOrderedQuantity(update.getValue());
				productStockCheck.setStockQuantity(update.getKey().getQuantity());
				productStockCheck.setCurrentQuantity(currentProductQuantityMap.get(update.getKey().getProductId()));
				productStockCheck.setMod(unfulfilledorder.getDueDate().minusWeeks(update.getKey().getLeadTime()+1));
				productStockCheckList.add(productStockCheck);					
			}
			forecastProductDto.setOrderedProducts(productStockCheckList);
			forecastProductDtoList.add(forecastProductDto);
			productDetails.clear();
		}

		return forecastProductDtoList;
	}


	private void updateCurrentQuantity(int productId, int packageOrderdQunatity, int packageStockQuantity, Map<Integer, Integer> currentProductQuantityMap, Order unfulfilledorder) {
		if(!currentProductQuantityMap.containsKey(productId)) {
			updateOrder(unfulfilledorder,packageOrderdQunatity,packageStockQuantity);
			currentProductQuantityMap.put(productId, packageStockQuantity-packageOrderdQunatity);
		}else {
			packageStockQuantity =currentProductQuantityMap.get(productId);
			updateOrder(unfulfilledorder,packageOrderdQunatity,packageStockQuantity);
			currentProductQuantityMap.put(productId, packageStockQuantity-packageOrderdQunatity);
			
		}
	}

	private List<Order> getSortedOrder(List<Order> unfulfilledOrder) {
		Collections.sort(unfulfilledOrder, new Comparator<Order>() {

			@Override
			public int compare(Order o1, Order o2) {
				return (o1.getDueDate().compareTo(o2.getDueDate()));
			}
		});
		return unfulfilledOrder;

	}

	private List<Order> getUnfulfilledOrder(List<Order> order) {
		return order.stream()
				.filter(predicate->predicate.isFullfilled()==false)
				.collect(Collectors.toList());
	}

	public void productSetStockCaluculate(FetchOrderdProducts product,
			ProductSetModel individualProduct,Order unfulfilledorder, Map<Product, Integer> productDetails, Map<Integer, Integer> currentProductQuantityMap) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
		stockQuantity =individualProduct.getProduct().getQuantity();			
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		if(!currentProductQuantityMap.containsKey(individualProduct.getProduct().getProductId())) {
			updateOrder(unfulfilledorder,orderdQunatity,stockQuantity);
			currentProductQuantityMap.put(individualProduct.getProduct().getProductId(), stockQuantity-orderdQunatity);
		}else {
			stockQuantity =currentProductQuantityMap.get(individualProduct.getProduct().getProductId());
			updateOrder(unfulfilledorder,orderdQunatity,stockQuantity);
			currentProductQuantityMap.put(individualProduct.getProduct().getProductId(), stockQuantity-orderdQunatity);
			
		}
	}




	public void productStockCaluculate(FetchOrderdProducts product, int productId, Order unfulfilledorder, Map<Product, Integer> productDetails, Map<Integer, Integer> currentProductQuantityMap) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0,tillDateQuantity=0;
		Optional<Product> productValue = productDao.findById(productId);
		if(productValue.isPresent()) {
			stockQuantity =productValue.get().getQuantity();
		}				
		orderdQunatity=product.getQuantity();
		productDetails.put(productValue.get(),orderdQunatity);
		
		if(!currentProductQuantityMap.containsKey(productId)) {
			updateOrder(unfulfilledorder,orderdQunatity,stockQuantity);
			currentProductQuantityMap.put(productId, stockQuantity-orderdQunatity);
		}else {
			stockQuantity =currentProductQuantityMap.get(productId);
			updateOrder(unfulfilledorder,orderdQunatity,stockQuantity);
			currentProductQuantityMap.put(productId, stockQuantity-orderdQunatity);
			
		}

			
	}

	private void updateOrder(Order unfulfilledorder, int orderdQunatity, int stockQuantity) {
		if(orderdQunatity>stockQuantity && unfulfilledorder.isForecast()) {
			unfulfilledorder.setForecast(false);
			orderDao.save(unfulfilledorder);
		}
		
	}

}



