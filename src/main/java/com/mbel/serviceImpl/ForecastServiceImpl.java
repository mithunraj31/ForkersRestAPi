package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.PopulateIncomingShipmentDto;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.model.Order;
import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;

@Service("ForecastServiceImpl")
public class ForecastServiceImpl {

	@Autowired
	OrderServiceImpl orderServiceImpl;

	@Autowired
	FullfillOrderServiceImpl fullfillOrderServiceImpl;
	
	@Autowired
	IncomingShipmentServiceImpl incomingShipmentServiceImpl;

	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired
	ProductDao productDao;
	
	@Autowired
	OrderDao orderDao;
	
	@Autowired 
	ProductSetDao productSetDao;

	public List<PopulateOrderDto> getForecastOrderDetails() {

		List<Order> order =orderServiceImpl.getActiveOrders();
		List<Order>unfulfilledOrder=getUnfulfilledOrder(order);
		List<Order>sortedOrder=getSortedOrder(unfulfilledOrder);
		return calculateStock(sortedOrder);


	}

	private List<PopulateOrderDto> calculateStock(List<Order> sortedOrder) {
		List<PopulateOrderDto> forecastProductDtoList =new ArrayList<>();
		Map<Product,Integer>productDetails=new HashMap<>();
		Map<Integer,Boolean>forecastOrder=new HashMap<>();
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		Map<Integer,List<Integer>>incomingShipmentMap=new HashMap<>();
		for(Order unfulfilledorder:sortedOrder) {
			List<String> forecastString=new ArrayList<>();
			List<FetchOrderdProducts> fetchOrderedproductsList =new ArrayList<>();
			PopulateOrderDto forecastProductDto=new PopulateOrderDto();
			PopulateOrderDto order=orderServiceImpl.getOrderById(unfulfilledorder.getOrderId());
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			FetchProductSetDto productSet =new FetchProductSetDto();
			for(FetchOrderdProducts product:orderdProducts) {
				checkProductStatus(product,productSet,unfulfilledorder,productDetails,
						productQuantityMap,forecastOrder,incomingShipmentMap,forecastString);
				updateOrderForecast(forecastString,unfulfilledorder);
			}
			forecastProductDto.setActive(unfulfilledorder.isActive());
			forecastProductDto.setDueDate(unfulfilledorder.getDueDate());
			forecastProductDto.setForecast(unfulfilledorder.isForecast());
			forecastProductDto.setFulfilled(unfulfilledorder.isFulfilled());
			forecastProductDto.setOrderId(unfulfilledorder.getOrderId());
			forecastProductDto.setProposalNo(unfulfilledorder.getProposalNo());
			forecastProductDto.setReceivedDate(unfulfilledorder.getReceivedDate());
			Set<Entry<Product, Integer>>productMap=productDetails.entrySet();
			for(Entry<Product, Integer> update:productMap) {
				FetchOrderdProducts fetchOrderedproducts = new FetchOrderdProducts();
				fetchOrderedproducts.setProduct(productDetails(update,productQuantityMap,unfulfilledorder));
				fetchOrderedproducts.setQuantity(productDetails.get(update.getKey()));
				if(!update.getKey().isSet()) {
				fetchOrderedproducts.setQuantity(productQuantityMap.get(update.getKey().getProductId()).getRequiredQuantity());
				fetchOrderedproducts.setCurrentQuantity(productQuantityMap.get(update.getKey().getProductId()).getCurrentQuantity());
				fetchOrderedproducts.setRequiredQuantity(productQuantityMap.get(update.getKey().getProductId()).getRequiredQuantity());
				fetchOrderedproducts.setForecast(productQuantityMap.get(update.getKey().getProductId()).isForecast());
				fetchOrderedproducts.setMod(unfulfilledorder.getDueDate().minusWeeks(update.getKey().getLeadTime()+1));
				}
				fetchOrderedproductsList.add(fetchOrderedproducts);
			}
			forecastProductDto.setOrderedProducts(fetchOrderedproductsList);
			forecastProductDtoList.add(forecastProductDto);
			productDetails.clear();
			forecastOrder.clear();
		}

		return forecastProductDtoList;
	}


	private void checkProductStatus(FetchOrderdProducts product, FetchProductSetDto productSet, Order unfulfilledorder,
			Map<Product, Integer> productDetails, Map<Integer, Mappingfields> productQuantityMap,
			Map<Integer, Boolean> forecastOrder, Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {

		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			productStockCaluculate(product,productId,unfulfilledorder,productDetails,
					productQuantityMap,forecastOrder,incomingShipmentMap,forecastString);

		}else {
				 productSet = productServiceImpl.getProductSetById(productId);
				for(ProductSetModel individualProduct:productSet.getProducts()) {
					productSetStockCaluculate(product,individualProduct,unfulfilledorder,
							productDetails,productQuantityMap,forecastOrder,incomingShipmentMap,forecastString);
			}
		}

	
		
	}

	private void updateOrderForecast(List<String> forecastString, Order unfulfilledorder) {
		if(forecastString.contains("false")) {
			unfulfilledorder.setForecast(false);
			orderDao.save(unfulfilledorder);
		}else {
			unfulfilledorder.setForecast(true);
			orderDao.save(unfulfilledorder);
			
		}
		
		
	}

	private FetchProductSetDto productDetails(Entry<Product, Integer> update, Map<Integer, Mappingfields> productQuantityMap, Order unfulfilledorder) {
		Product proCheck = update.getKey();
		List<ProductSetModel> productList = new ArrayList<>();
		FetchProductSetDto componentSet= new FetchProductSetDto();
		componentSet.setProductId(proCheck.getProductId());
		componentSet.setProductName(proCheck.getProductName());
		componentSet.setDescription(proCheck.getDescription());
		componentSet.setPrice(proCheck.getPrice());
		componentSet.setObicNo(proCheck.getObicNo());
		componentSet.setSet(proCheck.isSet());
		componentSet.setActive(proCheck.isActive());
		componentSet.setUserId(proCheck.getUserId());
		componentSet.setCreatedAtDateTime(proCheck.getCreatedAtDateTime());
		componentSet.setUpdatedAtDateTime(proCheck.getUpdatedAtDateTime());
		if(!proCheck.isSet()) {
		componentSet.setQuantity(proCheck.getQuantity());
		componentSet.setMoq(proCheck.getMoq());
		componentSet.setLeadTime(proCheck.getLeadTime());
		}
		else {
			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proCheck.getProductId());
			for(int l=0;l< productsetList.size();l++ ) {
				ProductSetModel productSetModel = new ProductSetModel();
				Product component = new Product();
				component=productDao.findById((Integer) productsetList.get(l).get("product_component_id")).get();
				productSetModel.setProduct(component);
				productSetModel.setQuantity((Integer)productsetList.get(l).get("qty"));
				productSetModel.setCurrentQuantity(productQuantityMap.get(component.getProductId()).getCurrentQuantity());
				productSetModel.setRequiredQuantity(productQuantityMap.get(component.getProductId()).getRequiredQuantity());
				productSetModel.setForecast(productQuantityMap.get(component.getProductId()).isForecast());
				productSetModel.setMod(unfulfilledorder.getDueDate().minusWeeks(component.getLeadTime()+1));
				
				productList.add(productSetModel);
			}
		}
		componentSet.setProducts(productList);
		return componentSet;



	}

	public List<Order> getSortedOrder(List<Order> unfulfilledOrder) {
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
				.filter(predicate->predicate.isFulfilled()==false)
				.collect(Collectors.toList());
	}

	public void productSetStockCaluculate(FetchOrderdProducts product,
			ProductSetModel individualProduct,Order unfulfilledorder, Map<Product, Integer> productDetails,
			Map<Integer, Mappingfields> productQuantityMap, Map<Integer, Boolean> forecastOrder, Map<Integer, List<Integer>> incomingShipmentMap,List<String> forecastString) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity=0, orderdQunatity = 0,previousOrderQuantity = 0,tillDateQuantity=0;
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		productDetails.put(product.getProduct(), product.getQuantity());
		stockQuantity=individualProduct.getProduct().getQuantity();
		if(!productQuantityMap.containsKey(individualProduct.getProduct().getProductId())) {
			tillDateQuantity =getTillDateQuantity(individualProduct.getProduct(),stockQuantity,unfulfilledorder,mappingFields,incomingShipmentMap);
			updateOrder(unfulfilledorder,orderdQunatity,tillDateQuantity,mappingFields,forecastString);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
		}else {
			stockQuantity=productQuantityMap.get(individualProduct.getProduct().getProductId()).getCurrentQuantity();
			int availableStockQuantity = productQuantityMap.get(individualProduct.getProduct().getProductId()).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(individualProduct.getProduct(),availableStockQuantity,unfulfilledorder,mappingFields,incomingShipmentMap);
			previousOrderQuantity=productQuantityMap.get(individualProduct.getProduct().getProductId()).getRequiredQuantity();
			updateOrder(unfulfilledorder,orderdQunatity,tillDateQuantity,mappingFields,forecastString);
				mappingFields.setCurrentQuantity(tillDateQuantity);
				mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
			
		}
		forecastOrder.put(product.getProduct().getProductId(),mappingFields.isForecast())	;
	}




	public void productStockCaluculate(FetchOrderdProducts product, int productId, Order unfulfilledorder,
			Map<Product, Integer> productDetails, Map<Integer, Mappingfields> productQuantityMap, Map<Integer, Boolean> forecastOrder, Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {
		int stockQuantity=0,orderdQunatity = 0,previousOrderQuantity = 0,tillDateQuantity=0;
		Mappingfields mappingFields =new Mappingfields();
		Optional<Product> productValue = productDao.findById(productId);
		orderdQunatity=product.getQuantity();
		productDetails.put(productValue.get(),orderdQunatity);
		stockQuantity =productValue.get().getQuantity();
		if(!productQuantityMap.containsKey(productId)) {
			tillDateQuantity =getTillDateQuantity(productValue.get(),stockQuantity,unfulfilledorder,mappingFields,incomingShipmentMap);
			updateOrder(unfulfilledorder,orderdQunatity,tillDateQuantity,mappingFields,forecastString);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(productId, mappingFields);
		}else {
			stockQuantity=productQuantityMap.get(productId).getCurrentQuantity();
			int availableStockQuantity = productQuantityMap.get(productId).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(productValue.get(),availableStockQuantity,unfulfilledorder,mappingFields,incomingShipmentMap);
			previousOrderQuantity=productQuantityMap.get(productId).getRequiredQuantity();
			updateOrder(unfulfilledorder,orderdQunatity,tillDateQuantity,mappingFields,forecastString);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(productId, mappingFields);
			
		}

		forecastOrder.put(productId,mappingFields.isForecast())	;
	}

	private int getTillDateQuantity(Product newproduct, int stockQuantity, Order unfulfilledorder, Mappingfields mappingFields, Map<Integer, List<Integer>> incomingShipmentMap) {
		List<Integer>incomingOrderList=new ArrayList<>();
		int tillDateQuantity = stockQuantity;
		List<PopulateIncomingShipmentDto> incomingShipmentList=incomingShipmentServiceImpl.getAllIncomingShipment();
		List<PopulateIncomingShipmentDto> arrivedOrderList =
		incomingShipmentList.stream().filter(predicate->predicate.getArrivalDate().isBefore(unfulfilledorder.getDueDate())
				||predicate.getArrivalDate().isEqual(unfulfilledorder.getDueDate()))
		        .collect(Collectors.toList());
		for(PopulateIncomingShipmentDto arrivedOrder:arrivedOrderList) {
			for(FetchIncomingOrderdProducts incomingProduct: arrivedOrder.getProducts()) {
				if(newproduct.getProductId() == incomingProduct.getProduct().getProductId()) {
						if(!incomingShipmentMap.containsKey(newproduct.getProductId())){ 
					tillDateQuantity+=incomingProduct.getQuantity();
					if(!incomingShipmentMap.containsKey(newproduct.getProductId())) {
						incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
					incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);
				}else {
					incomingOrderList.addAll(incomingShipmentMap.get(incomingShipmentMap.get(newproduct.getProductId())));
					incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
					incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);
							
				}
				}else {
					List<Integer> idList=incomingShipmentMap.get(newproduct.getProductId());
					if(!idList.contains(arrivedOrder.getIncomingShipmentId())){
							tillDateQuantity+=incomingProduct.getQuantity();
							if(!incomingShipmentMap.containsKey(newproduct.getProductId())) {
								incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
							incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);
						}else {
							incomingOrderList.addAll(incomingShipmentMap.get(newproduct.getProductId()));
							incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
							incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);
									
						}
							
						
						
						
					}
				}
				}
		}
		}
		return tillDateQuantity;
		
	}

	private void updateOrder(Order unfulfilledorder, int orderdQunatity, int stockQuantity, Mappingfields mappingFields, List<String> forecastString) {
		mappingFields.setRequiredQuantity(orderdQunatity);
		if(orderdQunatity>stockQuantity) {
			mappingFields.setForecast(false);
			forecastString.add("false");
		}else {
			mappingFields.setForecast(true);
			forecastString.add("true");
			
		}
		
	}

}

class Mappingfields{
	
	private int currentQuantity;
	
	private int requiredQuantity;
	
	private int availableStockQuantity;
	
	private LocalDateTime previousIncomingOrderDate;
	
	private int previousIncomingQuantity;
	
	private boolean forecast;

	public int getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public int getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setRequiredQuantity(int requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}

	public boolean isForecast() {
		return forecast;
	}

	public void setForecast(boolean forecast) {
		this.forecast = forecast;
	}

	public int getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(int availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public LocalDateTime getPreviousIncomingOrderDate() {
		return previousIncomingOrderDate;
	}

	public void setPreviousIncomingOrderDate(LocalDateTime previousIncomingOrderDate) {
		this.previousIncomingOrderDate = previousIncomingOrderDate;
	}

	public int getPreviousIncomingQuantity() {
		return previousIncomingQuantity;
	}

	public void setPreviousIncomingQuantity(int previousIncomingQuantity) {
		this.previousIncomingQuantity = previousIncomingQuantity;
	}

	
}

