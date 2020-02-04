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
		Map<Integer,List<Mappingfields>>productDetails=new HashMap<>();
		Map<Integer,List<Mappingfields>>productPackageDetails=new HashMap<>();
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
			Set<Entry<Integer, List<Mappingfields>>>productMap=productDetails.entrySet();
			for(Entry<Integer, List<Mappingfields>> update:productMap) {
			    int productKey = update.getKey();
			    List<Mappingfields> productValue = update.getValue();
			    Product statusCheck =productDao.findById(productKey).get();
			    if(!statusCheck.isSet()) {
			     for (int i=0;i<productValue.size();i++) {
			    	 FetchOrderdProducts fetchOrderedproducts = new FetchOrderdProducts();
				fetchOrderedproducts.setProduct(productFetch(productValue.get(i),unfulfilledorder));
				fetchOrderedproducts.setQuantity(update.getValue().get(i).getRequiredQuantity());
				fetchOrderedproducts.setCurrentQuantity(update.getValue().get(i).getCurrentQuantity());
				fetchOrderedproducts.setRequiredQuantity(update.getValue().get(i).getRequiredQuantity());
				fetchOrderedproducts.setForecast(update.getValue().get(i).isForecast());
				fetchOrderedproducts.setMod(unfulfilledorder.getDueDate().minusWeeks(update.getValue().get(i).getProduct().getLeadTime()+3));
				fetchOrderedproductsList.add(fetchOrderedproducts);
				}
			    }else {
			    	FetchOrderdProducts fetchOrderedproducts = new FetchOrderdProducts();
			    	fetchOrderedproducts.setProduct(productPackageFetch(statusCheck,productValue,unfulfilledorder));
			    	fetchOrderedproducts.setQuantity(productQuantityMap.get(productKey).getPackageQuantity());
			    	fetchOrderedproducts.setForecast(productQuantityMap.get(productKey).isForecast());
			    	fetchOrderedproductsList.add(fetchOrderedproducts);
			    	
			    }
			}
			
			forecastProductDto.setOrderedProducts(fetchOrderedproductsList);
			forecastProductDtoList.add(forecastProductDto);
			productDetails.clear();
			forecastOrder.clear();
			
			
		}

		return forecastProductDtoList;
	}


	private void checkProductStatus(FetchOrderdProducts product, FetchProductSetDto productSet, Order unfulfilledorder,
			Map<Integer, List<Mappingfields>> productDetails, Map<Integer, Mappingfields> productQuantityMap,
			Map<Integer, Boolean> forecastOrder, Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {

		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			productStockCaluculate(product,productId,unfulfilledorder,productDetails,
					productQuantityMap,forecastOrder,incomingShipmentMap,forecastString);

		}else {
				 productSet = productServiceImpl.getProductSetById(productId);
				 List<String>forecastPackage =new ArrayList<>();
				 Mappingfields mappingPackage =new Mappingfields();
				for(ProductSetModel individualProduct:productSet.getProducts()) {
					productSetStockCaluculate(product,individualProduct,unfulfilledorder,
							productDetails,productQuantityMap,forecastOrder,incomingShipmentMap,forecastString,forecastPackage);
			}
				mappingPackage.setPackageQuantity(product.getQuantity());
				if(forecastPackage.contains("false")) {
					mappingPackage.setForecast(false);
				productQuantityMap.put(productId,mappingPackage);
				}else {
					mappingPackage.setForecast(true);
					productQuantityMap.put(productId,mappingPackage);
					
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

	private FetchProductSetDto productFetch(Mappingfields mappingfields,
			Order unfulfilledorder) {
		FetchProductSetDto componentSet= new FetchProductSetDto();
		Product proCheck = mappingfields.getProduct();
		return getProductInformation(componentSet,proCheck);
	}

private FetchProductSetDto getProductInformation( FetchProductSetDto componentSet,Product proCheck) {
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
	return componentSet;
		
	}

public FetchProductSetDto productPackageFetch(Product statusCheck, List<Mappingfields> productValue, Order unfulfilledorder){
	FetchProductSetDto componentSet= new FetchProductSetDto();
	List<ProductSetModel> productList = new ArrayList<>();
	getProductInformation(componentSet,statusCheck);
	for(int i=0;i< productValue.size();i++) {
		ProductSetModel productSetModel = new ProductSetModel();
		productSetModel.setProduct( productValue.get(i).getProduct());
		productSetModel.setQuantity(productValue.get(i).getOrderdQuantity());
		productSetModel.setCurrentQuantity(productValue.get(i).getCurrentQuantity());
		productSetModel.setRequiredQuantity(productValue.get(i).getRequiredQuantity());
		productSetModel.setForecast(productValue.get(i).isForecast());
		productSetModel.setMod(unfulfilledorder.getDueDate().minusWeeks(productValue.get(i).getProduct().getLeadTime()+3));
		productList.add(productSetModel);
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
			ProductSetModel individualProduct,Order unfulfilledorder, Map<Integer, List<Mappingfields>> productDetails,
			Map<Integer, Mappingfields> productQuantityMap, Map<Integer, Boolean> forecastOrder, 
			Map<Integer, List<Integer>> incomingShipmentMap,List<String> forecastString, List<String> forecastPackage) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity=0, orderdQunatity = 0,previousOrderQuantity = 0,tillDateQuantity=0;
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		mappingFields.setProduct(individualProduct.getProduct());
		mappingFields.setOrderdQuantity(individualProduct.getQuantity());
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setSet(true);
		stockQuantity=individualProduct.getProduct().getQuantity();
		if(!productQuantityMap.containsKey(individualProduct.getProduct().getProductId())) {
			tillDateQuantity =getTillDateQuantity(individualProduct.getProduct(),stockQuantity,unfulfilledorder,mappingFields,incomingShipmentMap);
			updateOrder(unfulfilledorder,orderdQunatity,tillDateQuantity,mappingFields,forecastString);
			forecastPackage.add(String.valueOf(mappingFields.isForecast()));
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
		}else {
			stockQuantity=productQuantityMap.get(individualProduct.getProduct().getProductId()).getCurrentQuantity();
			int availableStockQuantity = productQuantityMap.get(individualProduct.getProduct().getProductId()).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(individualProduct.getProduct(),availableStockQuantity,unfulfilledorder,mappingFields,incomingShipmentMap);
			previousOrderQuantity=productQuantityMap.get(individualProduct.getProduct().getProductId()).getRequiredQuantity();
			updateOrder(unfulfilledorder,orderdQunatity,tillDateQuantity,mappingFields,forecastString);
			forecastPackage.add(String.valueOf(mappingFields.isForecast()));
				mappingFields.setCurrentQuantity(tillDateQuantity);
				mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(individualProduct.getProduct().getProductId(), mappingFields);
			
		}
		multipleProductOrder(productDetails,product.getProduct().getProductId(),mappingFields);
		forecastOrder.put(product.getProduct().getProductId(),mappingFields.isForecast())	;
	
	}



	public void productStockCaluculate(FetchOrderdProducts product, int productId, Order unfulfilledorder,
			Map<Integer, List<Mappingfields>> productDetails, Map<Integer, Mappingfields> productQuantityMap, Map<Integer, Boolean> forecastOrder, 
			Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {
		int stockQuantity=0,orderdQunatity = 0,previousOrderQuantity = 0,tillDateQuantity=0;
		Mappingfields mappingFields =new Mappingfields();
		Optional<Product> productValue = productDao.findById(productId);
		orderdQunatity=product.getQuantity();
		stockQuantity =productValue.get().getQuantity();
		mappingFields.setProduct(productValue.get());
		mappingFields.setOrderdQuantity(orderdQunatity);
		mappingFields.setRequiredQuantity(orderdQunatity);
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
		multipleProductOrder(productDetails,productId,mappingFields);
		forecastOrder.put(productId,mappingFields.isForecast())	;
	}

	private void multipleProductOrder(Map<Integer, List<Mappingfields>> productDetails, int productId, Mappingfields mappingFields) {
		List<Mappingfields>productMappingList =new ArrayList<>();
		if(mappingFields.isSet()) {
		if(!productDetails.containsKey(productId)) {
			productMappingList.add(mappingFields);
		productDetails.put(productId,productMappingList);
		}else {
			productMappingList.addAll(productDetails.get(productId));
			productMappingList.add(mappingFields);
			productDetails.put(productId,productMappingList);
		}
		}else if(!productDetails.containsKey(productId))  {
			productMappingList.add(mappingFields);
			productDetails.put(productId,productMappingList);			
		}else {
			productMappingList.addAll(productDetails.get(productId));
			productMappingList.add(mappingFields);
			productDetails.put(productId,productMappingList);
		}
	}

	public int getTillDateQuantity(Product newproduct, int stockQuantity, Order unfulfilledorder, Mappingfields mappingFields, Map<Integer, List<Integer>> incomingShipmentMap) {
		List<Integer>incomingOrderList=new ArrayList<>();
		int tillDateQuantity = stockQuantity;
		List<PopulateIncomingShipmentDto> incomingShipmentList=incomingShipmentServiceImpl.getAllUnarrivedIncomingShipment();
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
	
	private int orderdQuantity;
	
	private int packageQuantity;
	
	private boolean forecast;
	
	private boolean set;
	
	private Product product;

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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getOrderdQuantity() {
		return orderdQuantity;
	}

	public void setOrderdQuantity(int orderdQuantity) {
		this.orderdQuantity = orderdQuantity;
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	public int getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(int packageQuantity) {
		this.packageQuantity = packageQuantity;
	}


	
}

