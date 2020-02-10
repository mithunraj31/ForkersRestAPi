package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.constants.Constants;
import com.mbel.dao.OrderDao;
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
	IncomingShipmentServiceImpl incomingShipmentServiceImpl;

	@Autowired
	OrderDao orderDao;


	public List<PopulateOrderDto> getForecastOrderDetails() {

		List<Order> order =orderDao.findAll();
		List<Order>activeUnfulfilledOrder=getActiveUnfulfilledOrder(order);
		List<Order>sortedOrder=getSortedOrder(activeUnfulfilledOrder);
		return calculateStock(sortedOrder);

	}

	private List<PopulateOrderDto> calculateStock(List<Order> sortedOrder) {
		List<PopulateOrderDto> forecastProductDtoList =new ArrayList<>();
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		Map<Integer,List<Integer>>incomingShipmentMap=new HashMap<>();
		for(Order unfulfilledorder:sortedOrder) {
			Map<Integer,List<Mappingfields>>productDetails=new HashMap<>();
			List<String> forecastString=new ArrayList<>();
			List<FetchOrderdProducts> fetchOrderedproductsList =new ArrayList<>();
			PopulateOrderDto forecastProductDto=new PopulateOrderDto();
			List<FetchOrderdProducts> orderdProducts = orderServiceImpl.getAllProducts(unfulfilledorder.getOrderId());
			for(FetchOrderdProducts product:orderdProducts) {
				checkProductStatus(product,unfulfilledorder.getDueDate(),productDetails,
						productQuantityMap,incomingShipmentMap,forecastString);
				updateOrderForecastFlag(forecastString,unfulfilledorder);
			}
			orderForecast(forecastProductDto,unfulfilledorder,productDetails,fetchOrderedproductsList,productQuantityMap,forecastProductDtoList);
		}
		return forecastProductDtoList;
	}



	private void orderForecast(PopulateOrderDto forecastProductDto, 
			Order unfulfilledorder, Map<Integer, List<Mappingfields>> productDetails,
			List<FetchOrderdProducts> fetchOrderedproductsList, Map<Integer, Mappingfields> productQuantityMap, List<PopulateOrderDto> forecastProductDtoList) {
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
			if(!productValue.get(0).isSet()) {
				for (int i=0;i<productValue.size();i++) {
					FetchOrderdProducts fetchOrderedproducts = new FetchOrderdProducts();
					fetchOrderedproducts.setProduct(productFetch(productValue.get(i)));
					fetchOrderedproducts.setQuantity(update.getValue().get(i).getRequiredQuantity());
					fetchOrderedproducts.setCurrentQuantity(update.getValue().get(i).getCurrentQuantity());
					fetchOrderedproducts.setRequiredQuantity(update.getValue().get(i).getRequiredQuantity());
					fetchOrderedproducts.setForecast(update.getValue().get(i).isForecast());
					fetchOrderedproducts.setMod(unfulfilledorder.getDueDate().minusWeeks(update.getValue().get(i).getProduct().getLeadTime()+3));
					fetchOrderedproductsList.add(fetchOrderedproducts);
				}
			}else {
				FetchOrderdProducts fetchOrderedproducts = new FetchOrderdProducts();
				fetchOrderedproducts.setProduct(productPackageFetch(productQuantityMap.get(productKey).getPackageProduct(),productValue,unfulfilledorder));
				fetchOrderedproducts.setQuantity(productQuantityMap.get(productKey).getPackageQuantity());
				fetchOrderedproducts.setForecast(productQuantityMap.get(productKey).isForecast());
				fetchOrderedproductsList.add(fetchOrderedproducts);

			}
		}

		forecastProductDto.setOrderedProducts(fetchOrderedproductsList);
		forecastProductDtoList.add(forecastProductDto);

	}

	public void checkProductStatus(FetchOrderdProducts product, LocalDateTime dueDate,
			Map<Integer, List<Mappingfields>> productDetails, Map<Integer, Mappingfields> productQuantityMap,
			Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {

		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {

			productStockCaluculate(product,dueDate,productDetails,
					productQuantityMap,incomingShipmentMap,forecastString);
		}else {
			List<String>forecastPackage =new ArrayList<>();
			Mappingfields mappingPackage =new Mappingfields();
			mappingPackage.setPackageProduct(product.getProduct());
			for(ProductSetModel individualProduct:product.getProduct().getProducts()) {
				productSetStockCaluculate(product,individualProduct,dueDate,
						productDetails,productQuantityMap,incomingShipmentMap,forecastString,forecastPackage);
			}
			mappingPackage.setPackageQuantity(product.getQuantity());
			if(forecastPackage.contains(Constants.STRING_FALSE)) {
				mappingPackage.setForecast(false);
				productQuantityMap.put(productId,mappingPackage);
			}else {
				mappingPackage.setForecast(true);
				productQuantityMap.put(productId,mappingPackage);

			}
		}



	}

	private void updateOrderForecastFlag(List<String> forecastString, Order unfulfilledorder) {
		if(forecastString.contains(Constants.STRING_FALSE)) {
			unfulfilledorder.setForecast(false);
			orderDao.save(unfulfilledorder);
		}else {
			unfulfilledorder.setForecast(true);
			orderDao.save(unfulfilledorder);

		}


	}

	private FetchProductSetDto productFetch(Mappingfields mappingfields) {
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

	public FetchProductSetDto productPackageFetch(Product product, List<Mappingfields> productValue, Order unfulfilledorder){
		FetchProductSetDto componentSet= new FetchProductSetDto();
		List<ProductSetModel> productList = new ArrayList<>();
		getProductInformation(componentSet,product);
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

	private List<Order> getActiveUnfulfilledOrder(List<Order> order) {
		return order.stream()
				.filter(predicate->predicate.isActive()
						&&!predicate.isFulfilled())
				.collect(Collectors.toList());
	}

	public void productSetStockCaluculate(FetchOrderdProducts product,
			ProductSetModel individualProduct,LocalDateTime dueDate, Map<Integer, List<Mappingfields>> productDetails,
			Map<Integer, Mappingfields> productQuantityMap, 
			Map<Integer, List<Integer>> incomingShipmentMap,List<String> forecastString, List<String> forecastPackage) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity=0;
		int orderdQunatity = 0;
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		mappingFields.setProduct(individualProduct.getProduct());
		mappingFields.setOrderdQuantity(individualProduct.getQuantity());
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setSet(true);
		stockQuantity=individualProduct.getProduct().getQuantity();
		updateStockValues(individualProduct.getProduct(),stockQuantity,orderdQunatity,dueDate,mappingFields,productQuantityMap,incomingShipmentMap,forecastString);
		forecastPackage.add(String.valueOf(mappingFields.isForecast()));
		multipleProductOrder(productDetails,product.getProduct().getProductId(),mappingFields);
	}



	public void productStockCaluculate(FetchOrderdProducts product, LocalDateTime dueDate,
			Map<Integer, List<Mappingfields>> productDetails, Map<Integer, Mappingfields> productQuantityMap,
			Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {
		int stockQuantity=0;
		int orderdQunatity = 0;
		Mappingfields mappingFields =new Mappingfields();
		Product productValue =product.getProduct();
		orderdQunatity=product.getQuantity();
		stockQuantity =productValue.getQuantity();
		mappingFields.setProduct(productValue);
		mappingFields.setOrderdQuantity(orderdQunatity);
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setSet(false);
		updateStockValues(productValue,stockQuantity,orderdQunatity,dueDate,mappingFields,productQuantityMap,incomingShipmentMap,forecastString);
		multipleProductOrder(productDetails,productValue.getProductId(),mappingFields);
	}

	public void updateStockValues(Product product, int stockQuantity, int orderdQunatity, LocalDateTime dueDate,
			Mappingfields mappingFields, Map<Integer, Mappingfields> productQuantityMap, Map<Integer, List<Integer>> incomingShipmentMap, List<String> forecastString) {
		int tillDateQuantity=0;
		if(!productQuantityMap.containsKey(product.getProductId())) {
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap);
			updateOrder(orderdQunatity,tillDateQuantity,mappingFields,forecastString);

			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);
		}else {
			stockQuantity = productQuantityMap.get(product.getProductId()).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap);
			updateOrder(orderdQunatity,tillDateQuantity,mappingFields,forecastString);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);

		}
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

	public int getTillDateQuantity(Product newproduct, int stockQuantity, LocalDateTime dueDate, 
			Map<Integer, List<Integer>> incomingShipmentMap) {
		List<Integer>incomingOrderList=new ArrayList<>();
		int tillDateQuantity = stockQuantity;
		List<PopulateIncomingShipmentDto> incomingShipmentList=incomingShipmentServiceImpl.getAllUnarrivedDueDateIncomingShipment(dueDate);
		for(PopulateIncomingShipmentDto arrivedOrder:incomingShipmentList) {
			for(FetchIncomingOrderdProducts incomingProduct: arrivedOrder.getProducts()) {
				if(newproduct.getProductId() == incomingProduct.getProduct().getProductId()) {
				tillDateQuantity=addArrivedQuantity(tillDateQuantity,incomingShipmentMap,newproduct,arrivedOrder,incomingOrderList,incomingProduct);
				}
			}
		}
		return tillDateQuantity;

	}

	private int addArrivedQuantity(int tillDateQuantity, Map<Integer, List<Integer>> incomingShipmentMap, 
			Product newproduct, PopulateIncomingShipmentDto arrivedOrder, List<Integer> incomingOrderList, FetchIncomingOrderdProducts incomingProduct) {
		if(!incomingShipmentMap.containsKey(newproduct.getProductId())){ 
			tillDateQuantity+=incomingProduct.getQuantity();
				incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
				incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);
		}else {
			List<Integer> idList=incomingShipmentMap.get(newproduct.getProductId());
			if(!idList.contains(arrivedOrder.getIncomingShipmentId())){
				tillDateQuantity+=incomingProduct.getQuantity();
					incomingOrderList.addAll(incomingShipmentMap.get(newproduct.getProductId()));
					incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
					incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);

			}
		}
		return tillDateQuantity;
	}

	private void updateOrder(int orderdQunatity, int stockQuantity, Mappingfields mappingFields, List<String> forecastString) {
		if(orderdQunatity>stockQuantity) {
			mappingFields.setForecast(false);
			forecastString.add(Constants.STRING_FALSE);
		}else {
			mappingFields.setForecast(true);
			forecastString.add(Constants.STRING_TRUE);

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
	
	private Product packageProduct;

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

	public Product getPackageProduct() {
		return packageProduct;
	}

	public void setPackageProduct(Product packageProduct) {
		this.packageProduct = packageProduct;
	}




}

