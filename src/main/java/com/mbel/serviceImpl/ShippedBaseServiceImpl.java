package com.mbel.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mbel.constants.Constants;
import com.mbel.dao.CustomerDao;
import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.OrderDao;
import com.mbel.dao.OrderProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dao.SchedulePatternDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.ProductDataDto;
import com.mbel.dto.ProductPredictionDto;
import com.mbel.model.Customer;
import com.mbel.model.IncomingOrderData;
import com.mbel.model.IncomingShipment;
import com.mbel.model.Order;
import com.mbel.model.OrderData;
import com.mbel.model.OrderProduct;
import com.mbel.model.PredictionData;
import com.mbel.model.Product;
import com.mbel.model.ProductIncomingShipmentModel;
import com.mbel.model.ProductOutgoingShipmentModel;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;
import com.mbel.model.SchedulePattern;

@Service("ShippedBaseServiceImpl")
public class ShippedBaseServiceImpl {

	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired
	OrderServiceImpl orderServiceImpl;

	@Autowired
	IncomingShipmentDao incomingShipmentDao;

	@Autowired
	IncomingShipmentServiceImpl incomingShipmentServiceImpl;

	@Autowired
	OrderDao orderDao;

	@Autowired
	OrderProductDao orderProductDao;

	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;

	@Autowired 
	CustomerDao customerDao;
	
	@Autowired 
	SchedulePatternDao schedulePatternDao;

	public List<ProductPredictionDto> getProductPrediction(Map<String, String> allParams) {
		int year=Integer.parseInt(allParams.get(Constants.YEAR));
		int month=Integer.parseInt(allParams.get(Constants.MONTH));
		int patternId=allParams.containsKey(Constants.PATTERN)?Integer.parseInt(allParams.get(Constants.PATTERN)):0;
		SchedulePattern schedulePattern=schedulePatternDao.findById(patternId).orElse(null);
		List<Product> allProduct = getAllSortedProducts();
		List<ProductSet> allProductSet =productSetDao.findAll();
		List<Order>order =getFulfilledOrdersBetweenDueDates(year,month);
		List<OrderProduct>orderProduct=order.isEmpty()?null: getOrderedProductsBasedOnOrderId(order);
		List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll(); 
		List<Customer> allCustomer = customerDao.findAll();
		return predictProduct(allCustomer,allProduct,allProductSet, order,orderProduct,incomingShipment,year,month,schedulePattern);

	}

	private List<OrderProduct> getOrderedProductsBasedOnOrderId(List<Order> order) {
		List<Integer>orderIdList=order.stream().map(Order::getOrderId).collect(Collectors.toList());
		return orderProductDao.findAllByOrderId(orderIdList);
	}

	private List<Order> getFulfilledOrdersBetweenDueDates(int year, int month) {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime receivedDate =LocalDateTime.of(year, month, 1, 0, 0);
		LocalDate initial = LocalDate.of(year, month, 1);

		LocalDateTime dueDateEnd =LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime finaldateTime= (today.plusMonths(2L));
		LocalDate finaldate=LocalDate.of(finaldateTime.getYear(),finaldateTime.getMonth(),1);
		LocalDateTime dueDateStart =LocalDateTime.of(finaldateTime.getYear(),finaldateTime.getMonth(), finaldate.lengthOfMonth(), 0, 0);
		if(receivedDate.isAfter(today.plusMonths(2L))) {
			dueDateEnd =LocalDateTime.of(year, month, 1, 0, 0);
			dueDateStart =LocalDateTime.of(year, month, initial.lengthOfMonth(), 0, 0);

		}
		dueDateStart =DateTimeUtil.toUtc(dueDateStart).plusDays(1);
		dueDateEnd=DateTimeUtil.toUtc(dueDateEnd).minusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return orderDao.getFulfilledOrdersBetweenDueDates(dueDateEnd.format(formatter), dueDateStart.format(formatter));
	}

	private List<Product> getAllSortedProducts() {
		List<Product>product =productDao.getActiveProducts();
		return productServiceImpl.arrangeProductbySortField(product);
	}


	private List<ProductPredictionDto> predictProduct(List<Customer> allCustomer, List<Product> allProduct, List<ProductSet> allProductSet,
			List<Order> order, List<OrderProduct> orderProduct, List<IncomingShipment> incomingShipment,
			int year, int month, SchedulePattern schedulePattern) {
		if(Objects.isNull(schedulePattern)) {
		List<ProductPredictionDto> productPredictionDtoList = new ArrayList<>();
		for(Product product:allProduct.stream().filter(predicate->predicate.isActive()
				&&predicate.isSet()&&predicate.isDisplay()).collect(Collectors.toList())) {
			List<PredictionData> predictionDataList = new ArrayList<>();
			ProductPredictionDto productPredictionDto =new ProductPredictionDto();
			productPredictionDto.setObicNo(product.getObicNo());
			productPredictionDto.setProductId(product.getProductId());
			productPredictionDto.setProductName(product.getProductName());
			productPredictionDto.setDescription(product.getDescription());
			productPredictionDto.setColor(product.getColor());
			List<ProductSet> productsetList= allProductSet.stream()
					.filter(predicate->predicate.getSetId()==product.getProductId())
					.collect(Collectors.toList());
			List<Product> productList=sortProductInProductSet(productsetList,allProduct);
			List<ProductDataDto>productDataDtoList=new ArrayList<>();
			for(int l=0;l< productList.size();l++ ) {
				ProductDataDto productDataDto =new ProductDataDto();
				predictionDataList = new ArrayList<>();
				Product component =productList.get(l);
				productDataDto.setDescription(component.getDescription());
				productDataDto.setProductId(component.getProductId());
				productDataDto.setObicNo(component.getObicNo());
				productDataDto.setProductName(component.getProductName());
				productDataDto.setColor(component.getColor());
				List<PredictionData> data =calculateAccordingToDate(component, year,month,predictionDataList,order,
						incomingShipment,orderProduct,allProduct,allProductSet,allCustomer);
				productDataDto.setValues(data);
				productDataDtoList.add(productDataDto);
			}
			productPredictionDto.setProducts(productDataDtoList);
			productPredictionDtoList.add(productPredictionDto);
		}


			getIndividualProductPrediction(productPredictionDtoList,allCustomer,allProduct,allProductSet, order,orderProduct,incomingShipment,year,month);
			return productPredictionDtoList;
		}else {
			return PatternProductPredictionData(schedulePattern,allCustomer,allProduct,allProductSet, order,orderProduct,incomingShipment,year,month);
		}


	} 
	
	private void getIndividualProductPrediction(List<ProductPredictionDto> productPredictionDtoList, List<Customer> allCustomer, List<Product> allProduct, List<ProductSet> allProductSet, List<Order> order, List<OrderProduct> orderProduct, List<IncomingShipment> incomingShipment, int year, int month) {	ProductPredictionDto productPredictionDto =new ProductPredictionDto();
		List<ProductDataDto>productDataDtoList=new ArrayList<>();
		productPredictionDto.setProductId(0);
		productPredictionDto.setDescription(" ");
		productPredictionDto.setObicNo(" ");
		productPredictionDto.setProductName("Individual Product");
		productPredictionDto.setColor("");
		for(Product product:allProduct.stream()
				.filter(predicate->predicate.isActive()&&!predicate.isSet()&&predicate.isDisplay())
				.collect(Collectors.toList())) {
			List<PredictionData> predictionDataList = new ArrayList<>();
			ProductDataDto productDataDto =new ProductDataDto();
			productDataDto.setDescription(product.getDescription());
			productDataDto.setProductId(product.getProductId());
			productDataDto.setObicNo(product.getObicNo());
			productDataDto.setProductName(product.getProductName());
			productDataDto.setColor(product.getColor());
			List<PredictionData> data  =calculateAccordingToDate(product, year,month
					,predictionDataList,order,incomingShipment,orderProduct,allProduct,allProductSet, allCustomer);
			productDataDto.setValues(data);
			productDataDtoList.add(productDataDto);
		}

		productPredictionDto.setProducts(productDataDtoList);
		productPredictionDtoList.add(productPredictionDto);
	}

	private void getIndividualScheduleProductPrediction(List<ProductPredictionDto> productPredictionDtoList, List<Customer> allCustomer,
			List<Product> individualProductList, List<Product> allProduct, List<ProductSet> allProductSet, List<Order> order, List<OrderProduct> orderProduct, List<IncomingShipment> incomingShipment, int year, int month) {	ProductPredictionDto productPredictionDto =new ProductPredictionDto();
	List<ProductDataDto>productDataDtoList=new ArrayList<>();
	productPredictionDto.setProductId(0);
	productPredictionDto.setDescription(" ");
	productPredictionDto.setObicNo(" ");
	productPredictionDto.setProductName("Individual Product");
	productPredictionDto.setColor("");
	for(Product product:individualProductList) {
		List<PredictionData> predictionDataList = new ArrayList<>();
		ProductDataDto productDataDto =new ProductDataDto();
		productDataDto.setDescription(product.getDescription());
		productDataDto.setProductId(product.getProductId());
		productDataDto.setObicNo(product.getObicNo());
		productDataDto.setProductName(product.getProductName());
		productDataDto.setColor(product.getColor());
		List<PredictionData> data  =calculateAccordingToDate(product, year,month
				,predictionDataList,order,incomingShipment,orderProduct,allProduct,allProductSet, allCustomer);
		productDataDto.setValues(data);
		productDataDtoList.add(productDataDto);
	}

	productPredictionDto.setProducts(productDataDtoList);
	productPredictionDtoList.add(productPredictionDto);
	}

	private List<ProductPredictionDto> PatternProductPredictionData(SchedulePattern schedulePattern, List<Customer> allCustomer, List<Product> allProduct, List<ProductSet> allProductSet, List<Order> order, List<OrderProduct> orderProduct, List<IncomingShipment> incomingShipment, int year, int month) {
		List<Integer>productIdList=new ArrayList<>();
		List<ProductPredictionDto> productPredictionDtoList = new ArrayList<>();
		if(Objects.nonNull(schedulePattern)) {
			String pattern=schedulePattern.getPattern();
			JsonArray convertedObject = new Gson().fromJson(pattern, JsonArray.class);
			for(int i=0;i<convertedObject.size();i++) {
				JsonObject explrObject = convertedObject.get(i).getAsJsonObject();
				productIdList.add(explrObject.get("id").getAsInt());
				JsonArray itemsArray=(explrObject.get("items").getAsJsonArray());
				for(int j=0;j<itemsArray.size();j++) {
					productIdList.add(itemsArray.get(j).getAsInt());
				}
				productPredictionDtoList.addAll(PredictPatternProduct(productIdList,allCustomer,allProduct,allProductSet,
						order,orderProduct,incomingShipment,year,month));
				productIdList.clear();
			}

		}	
		return productPredictionDtoList;
	}
	
	private List<ProductPredictionDto> PredictPatternProduct(List<Integer> productIdList, List<Customer> allCustomer, List<Product> allProduct, List<ProductSet> allProductSet, List<Order> order, List<OrderProduct> orderProduct, List<IncomingShipment> incomingShipment, int year, int month) {
		List<ProductPredictionDto> productPredictionDtoList = new ArrayList<>();
		if(productIdList.get(0)!=0) {
		for(Product product:allProduct.stream()
				.filter(predicate->predicate.getProductId()==productIdList.get(0)).collect(Collectors.toList())) {
				List<PredictionData> predictionDataList = new ArrayList<>();
				ProductPredictionDto productPredictionDto =new ProductPredictionDto();
				productPredictionDto.setObicNo(product.getObicNo());
				productPredictionDto.setProductId(product.getProductId());
				productPredictionDto.setProductName(product.getProductName());
				productPredictionDto.setDescription(product.getDescription());
				productPredictionDto.setColor(product.getColor());
				List<Product> productList=sortPatternProductSet(productIdList,allProduct);
				List<ProductDataDto>productDataDtoList=new ArrayList<>();
				for(int l=0;l< productList.size();l++ ) {
					ProductDataDto productDataDto =new ProductDataDto();
					predictionDataList = new ArrayList<>();
					Product component =productList.get(l);
					productDataDto.setDescription(component.getDescription());
					productDataDto.setProductId(component.getProductId());
					productDataDto.setObicNo(component.getObicNo());
					productDataDto.setProductName(component.getProductName());
					productDataDto.setColor(component.getColor());
					List<PredictionData> data =calculateAccordingToDate(component, year,month,predictionDataList,order,
							incomingShipment,orderProduct,allProduct,allProductSet,allCustomer);
					productDataDto.setValues(data);
					productDataDtoList.add(productDataDto);
				}
				productPredictionDto.setProducts(productDataDtoList);
				productPredictionDtoList.add(productPredictionDto);
			}
			}else {
				productIdList.remove(0);
				List<Product>individualProductList=getIndividualPatternProductList(allProduct,productIdList);
				individualProductList.forEach(action->action.setDisplay(true));
				getIndividualScheduleProductPrediction(productPredictionDtoList, allCustomer, individualProductList, allProduct,allProductSet, order, orderProduct, incomingShipment, year, month);
			}
		return productPredictionDtoList;
		}

	private List<Product> getIndividualPatternProductList(List<Product> allProduct, List<Integer> productIdList) {
		List<Product>patternProductList=new ArrayList<Product>();
		for(int productId:productIdList) {
			patternProductList.addAll(allProduct.stream().filter(predicate->predicate.getProductId()==productId).collect(Collectors.toList()));
		}
		return patternProductList;
	}

	private List<Product> sortPatternProductSet(List<Integer> productIdList, List<Product> allProduct) {
		List<Product> productList=new ArrayList<>();
		for(int l=1;l< productIdList.size();l++ ) {
			int productComponentId =productIdList.get(l);
			Product component =allProduct.stream().filter(predicate->predicate.getProductId()==productComponentId)
					.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
						if (list.size() != 1) {
							return null;
						}
						return list.get(0);
					}));
			productList.add(component);

		}
		return productList;
	}


	private List<Product> sortProductInProductSet(List<ProductSet> productsetList, List<Product> allProduct) {
		List<Product> productList=new ArrayList<>();
		for(int l=0;l< productsetList.size();l++ ) {
			int productComponentId =productsetList.get(l).getProductComponentId();
			Product component =allProduct.stream().filter(predicate->predicate.getProductId()==productComponentId)
					.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
						if (list.size() != 1) {
							return null;
						}
						return list.get(0);
					}));
			productList.add(component);

		}
		productList.sort(Comparator.comparingInt(predicate->predicate.getSort()));
		return productList;

	}
	private String getCustomer(List<Customer> customerList, int customerId) {
		return customerList.stream()
				.filter(predicate->predicate.getCustomerId()==customerId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0).getCustomerName();
				}));
	}

	private List<PredictionData> calculateAccordingToDate(Product product, int year, int month, List<PredictionData> predictionDataList, 
			List<Order> order, List<IncomingShipment> incomingShipment, List<OrderProduct> orderProduct, List<Product> allProduct, 
			List<ProductSet> allProductSet, List<Customer> allCustomer) {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime receivedDate =LocalDateTime.of(year, month, 1, 0, 0);
		LocalDate initial = LocalDate.of(year, month, 1);

		LocalDateTime dueDateEnd =LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime finaldateTime= (today.plusMonths(2L));
		LocalDate finaldate=LocalDate.of(finaldateTime.getYear(),finaldateTime.getMonth(),1);
		LocalDateTime dueDateStart =LocalDateTime.of(finaldateTime.getYear(),finaldateTime.getMonth(), finaldate.lengthOfMonth(), 0, 0);
		if(receivedDate.isAfter(today.plusMonths(2L))) {
			dueDateEnd =LocalDateTime.of(year, month, 1, 0, 0);
			dueDateStart =LocalDateTime.of(year, month, initial.lengthOfMonth(), 0, 0);

		}
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		for(LocalDateTime dueDate=dueDateStart;dueDate.isAfter(dueDateEnd)||
				dueDate.isEqual(dueDateEnd);dueDate=dueDate.minusDays(1)) {
			boolean fixed=true;
			List<Order> fulfilledorder =getFulfilledOrder(order,dueDate);
			List<Integer>productIdList =new ArrayList<>();
			PredictionData predictionData=new PredictionData();
			int incomingQuantity =0;
			Map<Integer,List<Mappingfields>>productDetails=new HashMap<>();
			Map<Integer,Mappingfields>arrivedIncomingDetails=new HashMap<>();
			Map<Integer,List<Integer>>incomingShipmentMap=new HashMap<>();
			if(!fulfilledorder.isEmpty()) {
				for(Order individualOrder:fulfilledorder) {
					List<FetchOrderdProducts> orderdProducts= getAllProducts(individualOrder,orderProduct,allProduct,allProductSet);
					for(FetchOrderdProducts productCheck:orderdProducts) {
						checkProductStatus(productDetails,productCheck, dueDate,
								productQuantityMap, incomingShipmentMap,incomingShipment,
								allProduct,allProductSet,individualOrder,productIdList);

					}
				}
			}
			if(fulfilledorder.isEmpty()||!productIdList.contains(product.getProductId())) {
				updateArrivedIncomingOrder(incomingQuantity, product, productQuantityMap, incomingShipment, dueDate, allProduct);
			}
			int numOrdered=productNumOrdered(order,product,orderProduct,allProduct,allProductSet,dueDate);
			if(dueDate.getMonth()==initial.getMonth()) {
				updateDailyStockValues(numOrdered,productDetails,predictionData,allCustomer,
						dueDate,productQuantityMap,product,
						predictionDataList,incomingQuantity,fixed,arrivedIncomingDetails);
			}
		}
		Collections.reverse(predictionDataList);
		return predictionDataList;
	}



	private void updateArrivedIncomingOrder(int incomingQuantity, Product product, 
			Map<Integer, Mappingfields> productQuantityMap, List<IncomingShipment> incomingShipment, 
			LocalDateTime dueDate, List<Product> allProduct) {
		List<Integer>incomingOrderIdList=new ArrayList<>();
		List<String>shipmentNoList=new ArrayList<>();
		List<Integer>incomingQtyList=new ArrayList<>();
		List<Boolean>incomingFulfillList=new ArrayList<>();
		List<Boolean>incomingfixedList=new ArrayList<>();
		List<String>branchNoList=new ArrayList<>();
		List<FetchIncomingOrderdProducts> incomingProductList=getAllArrivedDueDateIncomingShipment(incomingShipment, dueDate, allProduct);
		for(int i=0;i<incomingProductList.size();i++) {
			if(incomingProductList.get(i).getProduct().getProductId()==product.getProductId()) {
				incomingOrderIdList.add(incomingProductList.get(i).getIncomingShipmentId());
				shipmentNoList.add(incomingProductList.get(i).getShipmentNo());
				branchNoList.add(incomingProductList.get(i).getBranch());
				incomingFulfillList.add(true);
				if(incomingProductList.get(i).isFixed()) {
					incomingQuantity+=incomingProductList.get(i).getConfirmedQty();
					incomingQtyList.add(incomingProductList.get(i).getConfirmedQty());
					incomingfixedList.add(true);
				}else {
					incomingQuantity+=incomingProductList.get(i).getPendingQty();
					incomingQtyList.add(incomingProductList.get(i).getPendingQty());
					incomingfixedList.add(false);
				}
			}
		}
		if(incomingQuantity!=0) {
			Mappingfields mapping=productQuantityMap.get(product.getProductId());
			if(mapping!=null) {
				int availableQunatity=mapping.getAvailableStockQuantity()==0?mapping.getCurrentQuantity():mapping.getAvailableStockQuantity();
				mapping.setAvailableStockQuantity(availableQunatity-incomingQuantity);
				mapping.setCurrentQuantity(availableQunatity);
				mapping.setIncomingQuantity(incomingQuantity);
				mapping.setIncomingFixed(incomingfixedList);
				mapping.setIncomingFulfilment(incomingFulfillList);
				mapping.setIncomingOrderId(incomingOrderIdList);
				mapping.setShipmnetNo(shipmentNoList);
				mapping.setIndividualIncomingQty(incomingQtyList);
				mapping.setBranch(branchNoList);
				productQuantityMap.put(product.getProductId(), mapping);
			}else {
				Mappingfields mappingfields =  new Mappingfields();
				mappingfields.setAvailableStockQuantity(product.getQuantity()-incomingQuantity);
				mappingfields.setCurrentQuantity(product.getQuantity());
				mappingfields.setIncomingQuantity(incomingQuantity);
				mappingfields.setIncomingFixed(incomingfixedList);
				mappingfields.setIncomingFulfilment(incomingFulfillList);
				mappingfields.setIncomingOrderId(incomingOrderIdList);
				mappingfields.setShipmnetNo(shipmentNoList);
				mappingfields.setIndividualIncomingQty(incomingQtyList);
				mappingfields.setBranch(branchNoList);
				productQuantityMap.put(product.getProductId(), mappingfields);
			}
		}else {
			Mappingfields mapping=productQuantityMap.get(product.getProductId());
			if(mapping!=null) {
				incomingQtyList.add(incomingQuantity);
				mapping.setIncomingQuantity(incomingQuantity);
				mapping.setIndividualIncomingQty(incomingQtyList);
				mapping.setIncomingOrderId(incomingOrderIdList);
				mapping.setCurrentQuantity(mapping.getAvailableStockQuantity());
				productQuantityMap.put(product.getProductId(), mapping);
			}
		}


	}
	public void updateDailyStockValues(int numOrdered,
			Map<Integer, List<Mappingfields>> productDetails, PredictionData predictionData, List<Customer> allCustomer, LocalDateTime dueDate, 
			Map<Integer, Mappingfields> productQuantityMap, Product product,
			List<PredictionData> predictionDataList, int incomingQuantity, boolean fixed, Map<Integer, Mappingfields> arrivedIncomingDetails) {
		List<IncomingOrderData>incomingOrderList = new ArrayList<>();
		int incomingFinalQuantity=0;
		if(productQuantityMap!=null&&productQuantityMap.get(product.getProductId())!=null) {
			List<Integer>incomingOrderIdList=productQuantityMap.get(product.getProductId()).getIncomingOrderId();
			if(incomingOrderIdList!=null) {
				for(int i=0;i<incomingOrderIdList.size();i++) {
					IncomingOrderData incomingOrderData =new IncomingOrderData();
					incomingOrderData.setIncomingshipmentId(incomingOrderIdList.get(i));
					incomingOrderData.setShipmentNo(productQuantityMap.get(product.getProductId()).getShipmnetNo().get(i));
					incomingOrderData.setQuantity(productQuantityMap.get(product.getProductId()).getIndividualIncomingQty().get(i));
					incomingFinalQuantity+=productQuantityMap.get(product.getProductId()).getIndividualIncomingQty().get(i);
					incomingOrderData.setFixed(productQuantityMap.get(product.getProductId()).getIncomingFixed().get(i));
					incomingOrderData.setFulfilled(productQuantityMap.get(product.getProductId()).getIncomingFulfilment().get(i));
					incomingOrderData.setBranch(productQuantityMap.get(product.getProductId()).getBranch().get(i));
					incomingOrderList.add(incomingOrderData);
				}
			}
		}
		if(arrivedIncomingDetails.containsKey(product.getProductId())) {
			for(int i=0;i<arrivedIncomingDetails.get(product.getProductId()).getIncomingOrderId().size();i++) {
				IncomingOrderData incomingOrderData =new IncomingOrderData();
				incomingOrderData.setIncomingshipmentId(arrivedIncomingDetails.get(product.getProductId()).getIncomingOrderId().get(i));
				incomingOrderData.setShipmentNo(arrivedIncomingDetails.get(product.getProductId()).getShipmnetNo().get(i));
				incomingOrderData.setQuantity(arrivedIncomingDetails.get(product.getProductId()).getIndividualIncomingQty().get(i));
				incomingFinalQuantity+=arrivedIncomingDetails.get(product.getProductId()).getIndividualIncomingQty().get(i);
				incomingOrderData.setFixed(arrivedIncomingDetails.get(product.getProductId()).getIncomingFixed().get(i));
				incomingOrderData.setFulfilled(arrivedIncomingDetails.get(product.getProductId()).getIncomingFulfilment().get(i));
				incomingOrderData.setBranch(arrivedIncomingDetails.get(product.getProductId()).getBranch().get(i));
				incomingOrderList.add(incomingOrderData);
			}
		}
		if(numOrdered > 1) {
			updateMultipleOrderStockValues(productDetails,predictionData,product,allCustomer,dueDate,incomingOrderList,incomingFinalQuantity);
		}else if(numOrdered==0) {
			updateNoOrderStockValue(predictionData,productQuantityMap,dueDate,product,incomingOrderList,incomingFinalQuantity);
		}else if(numOrdered==1){
			updateSingleOrderStockValue(predictionData,productQuantityMap,dueDate,product,allCustomer,
					incomingOrderList,incomingFinalQuantity,productDetails);
		}else {
			ProductIncomingShipmentModel incomingShipmentValues =new ProductIncomingShipmentModel();
			ProductOutgoingShipmentModel outgoingShipmentValues =new ProductOutgoingShipmentModel();
			predictionData.setDate(dueDate);
			predictionData.setCurrentQuantity(productQuantityMap.get(product.getProductId())!=null?
					productQuantityMap.get(product.getProductId()).getCurrentQuantity():product.getQuantity());
			predictionData.setQuantity(product.getQuantity());
			outgoingShipmentValues.setQuantity(0);
			outgoingShipmentValues.setFixed(fixed);
			outgoingShipmentValues.setFulfilled(0);
			predictionData.setOutgoing(outgoingShipmentValues);
			incomingShipmentValues.setQuantity(0);
			incomingShipmentValues.setFixed(true);
			incomingShipmentValues.setIncomingOrders(incomingOrderList);
			incomingQuantityUpdate(incomingFinalQuantity,incomingShipmentValues,incomingOrderList);
			predictionData.setIncoming(incomingShipmentValues);
		}

		predictionDataList.add(predictionData);
	}

	private void updateSingleOrderStockValue(PredictionData predictionData, Map<Integer, Mappingfields> productQuantityMap, LocalDateTime dueDate, Product product,
			List<Customer> allCustomer, List<IncomingOrderData> incomingOrderList, int incomingFinalQuantity, Map<Integer, List<Mappingfields>> productDetails) {

		ProductIncomingShipmentModel incomingShipmentValues =new ProductIncomingShipmentModel();
		ProductOutgoingShipmentModel outgoingShipmentValues =new ProductOutgoingShipmentModel();
		List<OrderData>orderDataList =new ArrayList<>();
		if(!productDetails.isEmpty()) {
			OrderData orderData = new OrderData();
			orderData.setOrderId(productDetails.get(product.getProductId()).get(0).getOrderId());
			orderData.setCustomer(getCustomer(allCustomer,productDetails.get(product.getProductId()).get(0).getCustomer()));
			orderData.setFixed(productDetails.get(product.getProductId()).get(0).isOrderFixed());
			orderData.setFulfilled(productDetails.get(product.getProductId()).get(0).isOutgoingFulfilment());
			orderData.setQuantity(productDetails.get(product.getProductId()).get(0).getRequiredQuantity());
			orderData.setProposalNo(productDetails.get(product.getProductId()).get(0).getProposalNo());
			outgoingShipmentValues.setQuantity(productDetails.get(product.getProductId()).get(0).getRequiredQuantity());
			outgoingShipmentValues.setFixed(productDetails.get(product.getProductId()).get(0).isOutgoingFixed());
			outgoingShipmentValues.setFulfilled(productDetails.get(product.getProductId()).get(0).isOutgoingFulfilment()?1:0);
			orderDataList.add(orderData);
			predictionData.setCurrentQuantity(productQuantityMap.get(product.getProductId())!=null?
					productQuantityMap.get(product.getProductId()).getCurrentQuantity():product.getQuantity());

		}else {

			OrderData orderData = new OrderData();
			orderData.setOrderId(productQuantityMap.get(product.getProductId()).getOrderId());
			orderData.setCustomer(getCustomer(allCustomer,productQuantityMap.get(product.getProductId()).getCustomer()));
			orderData.setFixed(productQuantityMap.get(product.getProductId()).isOrderFixed());
			orderData.setFulfilled(productQuantityMap.get(product.getProductId()).isOutgoingFulfilment());
			orderData.setQuantity(productQuantityMap.get(product.getProductId()).getRequiredQuantity());
			orderData.setProposalNo(productQuantityMap.get(product.getProductId()).getProposalNo());
			outgoingShipmentValues.setQuantity(productQuantityMap.get(product.getProductId()).getRequiredQuantity());
			outgoingShipmentValues.setFixed(productQuantityMap.get(product.getProductId()).isOutgoingFixed());
			outgoingShipmentValues.setFulfilled(productQuantityMap.get(product.getProductId()).isOutgoingFulfilment()?1:0);
			orderDataList.add(orderData);
			predictionData.setCurrentQuantity(productQuantityMap.get(product.getProductId()).getCurrentQuantity());

		}
		predictionData.setDate(dueDate);

		outgoingShipmentValues.setOrders(orderDataList);
		predictionData.setOutgoing(outgoingShipmentValues);
		predictionData.setQuantity(product.getQuantity());
		incomingShipmentValues.setQuantity(0);
		incomingShipmentValues.setFixed(true);
		incomingQuantityUpdate(incomingFinalQuantity,incomingShipmentValues,incomingOrderList);
		predictionData.setIncoming(incomingShipmentValues);



	}
	private void updateNoOrderStockValue(PredictionData predictionData, Map<Integer, Mappingfields> productQuantityMap, LocalDateTime dueDate, 
			Product product, List<IncomingOrderData> incomingOrderList, int incomingFinalQuantity) {
		ProductIncomingShipmentModel incomingShipmentValues =new ProductIncomingShipmentModel();
		ProductOutgoingShipmentModel outgoingShipmentValues =new ProductOutgoingShipmentModel();
		predictionData.setDate(dueDate);
		predictionData.setCurrentQuantity(productQuantityMap.get(product.getProductId())!=null?
				productQuantityMap.get(product.getProductId()).getCurrentQuantity():product.getQuantity());
		predictionData.setQuantity(product.getQuantity());
		outgoingShipmentValues.setQuantity(0);
		outgoingShipmentValues.setFixed(true);
		outgoingShipmentValues.setFulfilled(0);
		predictionData.setOutgoing(outgoingShipmentValues);
		incomingShipmentValues.setQuantity(0);
		incomingShipmentValues.setFixed(true);
		incomingQuantityUpdate(incomingFinalQuantity,incomingShipmentValues,incomingOrderList);
		predictionData.setIncoming(incomingShipmentValues);



	}
	private void incomingQuantityUpdate(int incomingFinalQuantity, ProductIncomingShipmentModel incomingShipmentValues, List<IncomingOrderData> incomingOrderList) {
		if(incomingFinalQuantity!=0) {
			incomingShipmentValues.setQuantity(incomingFinalQuantity);
			incomingShipmentValues.setIncomingOrders(incomingOrderList);
			List<Boolean>fixedtList=new ArrayList<>();
			List<Boolean>fulfillmentList=new ArrayList<>();
			for(int i=0;i<incomingOrderList.size();i++) {
				fixedtList.add(incomingOrderList.get(i).isFixed());
				fulfillmentList.add(incomingOrderList.get(i).isFulfilled());
			}

			if(fixedtList.contains(false)){
				incomingShipmentValues.setFixed(false);
			}else {
				incomingShipmentValues.setFixed(true);
			}
			if(fulfillmentList.contains(true)&&fulfillmentList.contains(false)) {
				incomingShipmentValues.setFulfilled(2);
			}else if(fulfillmentList.contains(true)&&!fulfillmentList.contains(false)) {
				incomingShipmentValues.setFulfilled(1);	
			}else if(fulfillmentList.contains(false)&&!fulfillmentList.contains(true)) {
				incomingShipmentValues.setFulfilled(0);	
			}
		}

	}
	private void updateMultipleOrderStockValues(Map<Integer, List<Mappingfields>> productDetails,
			PredictionData predictionData, 
			Product product, List<Customer> allCustomer, LocalDateTime dueDate, List<IncomingOrderData> incomingOrderList, int incomingFinalQuantity) {

		List<Mappingfields> orderedTimes=productDetails.get(product.getProductId());
		int requiredQuantity =0;
		int currentQuantity =0;
		boolean outgoingFixed = true;
		List<OrderData>orderDataList =new ArrayList<>();
		List<Boolean>outgoingFulfilList =new ArrayList<>();
		for(int i=0;i < orderedTimes.size();i++) {
			OrderData orderData = new OrderData();
			orderData.setOrderId(orderedTimes.get(i).getOrderId());
			orderData.setCustomer(getCustomer(allCustomer, orderedTimes.get(i).getCustomer()));
			orderData.setFixed(orderedTimes.get(i).isOrderFixed());
			orderData.setFulfilled(orderedTimes.get(i).isOutgoingFulfilment());
			outgoingFulfilList.add(orderedTimes.get(i).isOutgoingFulfilment());
			orderData.setQuantity(orderedTimes.get(i).getRequiredQuantity());
			orderData.setProposalNo(orderedTimes.get(i).getProposalNo());
			orderDataList.add(orderData);
			requiredQuantity+=orderedTimes.get(i).getRequiredQuantity();
			currentQuantity=orderedTimes.get(i).getCurrentQuantity();
			if(!orderedTimes.get(i).isOutgoingFixed()&& outgoingFixed==true) {
				outgoingFixed=false;
			}
		}

		ProductIncomingShipmentModel incomingShipmentValues =new ProductIncomingShipmentModel();
		ProductOutgoingShipmentModel outgoingShipmentValues =new ProductOutgoingShipmentModel();
		predictionData.setDate(dueDate);
		predictionData.setCurrentQuantity(currentQuantity);
		outgoingShipmentValues.setQuantity(requiredQuantity);
		outgoingShipmentValues.setFixed(outgoingFixed);
		outgoingShipmentValues.setFulfilled(0);
		outgoingShipmentValues.setOrders(orderDataList);
		if(outgoingFulfilList.contains(true)&&outgoingFulfilList.contains(false)) {
			outgoingShipmentValues.setFulfilled(2);
		}else if(outgoingFulfilList.contains(true)&&!outgoingFulfilList.contains(false)) {
			outgoingShipmentValues.setFulfilled(1);	
		}else if(outgoingFulfilList.contains(false)&&!outgoingFulfilList.contains(true)) {
			outgoingShipmentValues.setFulfilled(0);	
		}
		predictionData.setOutgoing(outgoingShipmentValues);
		predictionData.setQuantity(product.getQuantity());

		incomingShipmentValues.setQuantity(0);
		incomingShipmentValues.setFixed(true);
		incomingQuantityUpdate(incomingFinalQuantity,incomingShipmentValues,incomingOrderList);
		predictionData.setIncoming(incomingShipmentValues);

	}
	private int productNumOrdered(List<Order> order, Product product, List<OrderProduct> orderProduct, List<Product> allProduct, List<ProductSet> allProductSet, LocalDateTime dueDate) {
		List<FetchOrderdProducts> filteredProductSet=new ArrayList<>();
		List<ProductSetModel> filteredProduct=new ArrayList<>();
		List<Order> allOrder=getFulfilledOrder(order, dueDate);
		if(!allOrder.isEmpty()) {
			for(Order individualOrder:allOrder) {
				List<FetchOrderdProducts> orderdProducts= getAllProducts(individualOrder,orderProduct,allProduct,allProductSet);

				filteredProductSet.addAll(orderdProducts.stream()
						.filter(predicate->predicate.getProduct().getProductId()==product.getProductId())
						.collect(Collectors.toList()));
				for(int i=0;i<orderdProducts.size();i++) {
					if(orderdProducts.get(i).getProduct().isSet()) {
						List<ProductSetModel> individualproduct=orderdProducts.get(i).getProduct().getProducts();
						filteredProduct.addAll(individualproduct.stream()
								.filter(predicate->predicate.getProduct().getProductId()==product.getProductId())
								.collect(Collectors.toList()));
					}
				}
			}
		}
		return filteredProduct.size()+filteredProductSet.size();

	}

	public List<FetchOrderdProducts> getAllProducts(Order individualOrder,
			List<OrderProduct> orderProduct, List<Product> allProduct, List<ProductSet> allProductSet) {
		List<FetchOrderdProducts> orderProductList = new ArrayList<>();
		List<OrderProduct> orderItemsList=orderProduct.stream().filter(predicate->predicate.getOrderId()==individualOrder.getOrderId()).collect(Collectors.toList());
		for(int i=0;i<orderItemsList.size();i++) {
			FetchOrderdProducts order =new FetchOrderdProducts();
			int productId=orderItemsList.get(i).getProductId();
			FetchProductSetDto products =getProductSetById(productId,allProduct,allProductSet);
			order.setProduct(products);
			order.setQuantity(orderItemsList.get(i).getQuantity());
			orderProductList.add(order);

		}

		return orderProductList;
	}

	public FetchProductSetDto getProductSetById(int productId, List<Product> allProduct,
			List<ProductSet> allProductSet) {
		Product proCheck = allProduct.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
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
			componentSet.setCreatedAt(proCheck.getCreatedAt());
			componentSet.setUpdatedAt(proCheck.getUpdatedAt());
			componentSet.setCurrency(proCheck.getCurrency());
			componentSet.setDisplay(proCheck.isDisplay());
			if(proCheck.isSet()) {
				List<ProductSet> productsetList= allProductSet.stream().filter(predicate->predicate.getSetId()==proCheck.getProductId()).collect(Collectors.toList());
				for(int l=0;l< productsetList.size();l++ ) {
					ProductSetModel productSetModel = new ProductSetModel();
					int productComponentId =productsetList.get(l).getProductComponentId();
					Product component =allProduct.stream().filter(predicate->predicate.getProductId()==productComponentId)
							.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
								if (list.size() != 1) {
									return null;
								}
								return list.get(0);
							}));
					productSetModel.setProduct(component);
					productSetModel.setQuantity(productsetList.get(l).getQuantity());
					productList.add(productSetModel);
				}
			}
		}
		componentSet.setProducts(productList);
		return componentSet;



	}

	public FetchProductSetDto getProductById(int productId, List<Product> allProduct) {
		Product proCheck = allProduct.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
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
			componentSet.setCreatedAt(proCheck.getCreatedAt());
			componentSet.setUpdatedAt(proCheck.getUpdatedAt());
			componentSet.setCurrency(proCheck.getCurrency());
			componentSet.setSort(proCheck.getSort());
			componentSet.setDisplay(proCheck.isDisplay());
		}
		componentSet.setProducts(productList);
		return componentSet;



	}

	public void checkProductStatus(Map<Integer, List<Mappingfields>> productDetails, FetchOrderdProducts productCheck, LocalDateTime dueDate,
			Map<Integer, Mappingfields> productQuantityMap,
			Map<Integer, List<Integer>> incomingShipmentMap, List<IncomingShipment> incomingShipment,
			List<Product> allProduct, List<ProductSet> allProductSet, Order individualOrder, List<Integer> productIdList) {

		int productId = productCheck.getProduct().getProductId();
		if(!productCheck.getProduct().isSet()) {
			productIdList.add(productId);
			productStockCaluculate(productDetails,productCheck,dueDate,
					productQuantityMap,incomingShipmentMap,incomingShipment,allProduct,allProductSet,individualOrder);
		}else {
			Mappingfields mappingPackage =new Mappingfields();
			mappingPackage.setPackageProduct(productCheck.getProduct());
			for(ProductSetModel individualProduct:productCheck.getProduct().getProducts()) {
				productIdList.add(individualProduct.getProduct().getProductId());
				productSetStockCaluculate(productDetails,productCheck,individualProduct,dueDate,
						productQuantityMap,incomingShipmentMap,incomingShipment,allProduct,allProductSet,individualOrder);
			}
			mappingPackage.setPackageQuantity(productCheck.getQuantity());
			productQuantityMap.put(productId,mappingPackage);
		}
	}

	public void productSetStockCaluculate(Map<Integer, List<Mappingfields>> productDetails, FetchOrderdProducts product,
			ProductSetModel individualProduct,LocalDateTime dueDate, 
			Map<Integer, Mappingfields> productQuantityMap, 
			Map<Integer, List<Integer>> incomingShipmentMap,List<IncomingShipment> incomingShipment,
			List<Product> allProduct, List<ProductSet> allProductSet, Order individualOrder) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity=0;
		int orderdQunatity = 0;
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		mappingFields.setProduct(individualProduct.getProduct());
		mappingFields.setOrderdQuantity(individualProduct.getQuantity());
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setSet(true);
		mappingFields.setIncomingQuantity(0);
		mappingFields.setOutgoingFixed(individualOrder.isFixed());
		mappingFields.setOrderId(individualOrder.getOrderId());
		mappingFields.setCustomer(individualOrder.getCustomerId());
		mappingFields.setOrderFixed(individualOrder.isFixed());
		mappingFields.setProposalNo(individualOrder.getProposalNo());
		mappingFields.setOutgoingFulfilment(true);
		stockQuantity=productQuantityMap.get(individualProduct.getProduct().getProductId())!=null?
				productQuantityMap.get(individualProduct.getProduct().getProductId()).getAvailableStockQuantity():individualProduct.getProduct().getQuantity();
				updateStockValues(individualProduct.getProduct(),stockQuantity,orderdQunatity,
						dueDate,mappingFields,productQuantityMap,incomingShipmentMap,incomingShipment,allProduct,allProductSet);
				multipleProductOrder(productDetails,individualProduct.getProduct().getProductId(),mappingFields);
	}

	private void productStockCaluculate(Map<Integer, List<Mappingfields>> productDetails, FetchOrderdProducts productCheck,
			LocalDateTime dueDate, Map<Integer, Mappingfields> productQuantityMap, Map<Integer, List<Integer>> incomingShipmentMap,
			List<IncomingShipment> incomingShipment, List<Product> allProduct, List<ProductSet> allProductSet, Order individualOrder) {
		int stockQuantity=0;
		int orderdQunatity = 0;
		Mappingfields mappingFields =new Mappingfields();
		Product productValue =productCheck.getProduct();
		orderdQunatity=productCheck.getQuantity();
		stockQuantity =productQuantityMap.get(productValue.getProductId())!=null?
				productQuantityMap.get(productValue.getProductId()).getAvailableStockQuantity():productValue.getQuantity();
				mappingFields.setProduct(productValue);
				mappingFields.setOrderdQuantity(orderdQunatity);
				mappingFields.setRequiredQuantity(orderdQunatity);
				mappingFields.setOutgoingFixed(individualOrder.isFixed());
				mappingFields.setIncomingQuantity(0);
				mappingFields.setOrderId(individualOrder.getOrderId());
				mappingFields.setProposalNo(individualOrder.getProposalNo());
				mappingFields.setCustomer(individualOrder.getCustomerId());
				mappingFields.setOrderFixed(individualOrder.isFixed());
				mappingFields.setOutgoingFulfilment(true);
				updateStockValues(productValue,stockQuantity,orderdQunatity,dueDate,mappingFields,
						productQuantityMap,incomingShipmentMap,incomingShipment, allProduct, allProductSet);
				multipleProductOrder(productDetails,productCheck.getProduct().getProductId(),mappingFields);    
	}

	private void multipleProductOrder(Map<Integer, List<Mappingfields>> productDetails, int productId, Mappingfields mappingFields) {
		List<Mappingfields>productMappingList =new ArrayList<>();
		if(!productDetails.containsKey(productId)) {
			productMappingList.add(mappingFields);
			productDetails.put(productId,productMappingList);
		}else {
			productMappingList.addAll(productDetails.get(productId));
			productMappingList.add(mappingFields);
			productDetails.put(productId,productMappingList);
		}
	}

	public void updateStockValues(Product product, int stockQuantity, int orderdQunatity, LocalDateTime dueDate,
			Mappingfields mappingFields, Map<Integer, Mappingfields> productQuantityMap, 
			Map<Integer, List<Integer>> incomingShipmentMap, List<IncomingShipment> incomingShipment,
			List<Product> allProduct, List<ProductSet> allProductSet) {
		int tillDateQuantity=0;
		if(!productQuantityMap.containsKey(product.getProductId())) {
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap,incomingShipment,allProduct,mappingFields);
			mappingFields.setCurrentQuantity(stockQuantity);
			mappingFields.setCurrentDate(dueDate);
			mappingFields.setAvailableStockQuantity(tillDateQuantity+orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);
		}else if(productQuantityMap.get(product.getProductId()).getCurrentDate()==dueDate){
			stockQuantity = productQuantityMap.get(product.getProductId()).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap,incomingShipment, allProduct, mappingFields);
			mappingFields.setCurrentQuantity(productQuantityMap.get(product.getProductId()).getCurrentQuantity());
			mappingFields.setCurrentDate(dueDate);
			mappingFields.setAvailableStockQuantity(tillDateQuantity+orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);

		}else {
			stockQuantity = productQuantityMap.get(product.getProductId()).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap,incomingShipment, allProduct, mappingFields);
			mappingFields.setCurrentDate(dueDate);
			mappingFields.setCurrentQuantity(stockQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity+orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);
		}
	}

	public int getTillDateQuantity(Product newproduct, int stockQuantity, LocalDateTime dueDate, 
			Map<Integer, List<Integer>> incomingShipmentMap, List<IncomingShipment> incomingShipment,
			List<Product> allProduct, Mappingfields mappingFields) {
		List<Integer>incomingOrderList=new ArrayList<>();
		int tillDateQuantity = stockQuantity;
		List<FetchIncomingOrderdProducts> incomingShipmentList=getAllArrivedDueDateIncomingShipment(incomingShipment,dueDate,allProduct);
		for(FetchIncomingOrderdProducts arrivedOrder:incomingShipmentList) {
			if(newproduct.getProductId() == arrivedOrder.getProduct().getProductId()) {
				tillDateQuantity=addArrivedQuantity(tillDateQuantity,incomingShipmentMap,newproduct,arrivedOrder,incomingOrderList,mappingFields);
			}
		}
		return tillDateQuantity;

	}


	public List<FetchIncomingOrderdProducts> getAllIncomingShipment(List<IncomingShipment> incomingShipment,
			List<Product> allProduct) {
		List<FetchIncomingOrderdProducts> incomingShipmentDtoList = new ArrayList<>(); 
		for(IncomingShipment incoming :incomingShipment ) {
			FetchIncomingOrderdProducts incomingDto = new FetchIncomingOrderdProducts();
			incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());
			incomingDto.setShipmentNo(incoming.getShipmentNo());
			incomingDto.setArrived(incoming.isArrived());
			incomingDto.setActive(incoming.isActive());
			incomingDto.setProduct(getAllIncomingProduct(incoming,allProduct));
			incomingDto.setBranch(incoming.getBranch());
			incomingDto.setConfirmedQty(incoming.getConfirmedQty());
			incomingDto.setFixed(incoming.isFixed());
			incomingDto.setPartial(incoming.isPartial());		
			incomingDto.setFixedDeliveryDate(incoming.getFixedDeliveryDate());
			incomingDto.setDesiredDeliveryDate(incoming.getDesiredDeliveryDate());
			incomingDto.setOrderDate(incoming.getOrderDate());
			incomingDto.setVendor(incoming.getVendor());
			incomingDto.setPendingQty(incoming.getPendingQty());
			incomingDto.setQuantity(incoming.getQuantity());
			incomingDto.setCurrency(incoming.getCurrency());
			incomingDto.setPrice(incoming.getPrice());
			incomingShipmentDtoList.add(incomingDto);
		}

		return incomingShipmentDtoList;
	}

	public FetchProductSetDto getAllIncomingProduct(IncomingShipment incoming, 
			List<Product> allProduct) {
		FetchProductSetDto fetchProducts = new FetchProductSetDto(); 
		fetchProducts =getProductById(incoming.getProductId(),allProduct);
		fetchProducts.setQuantity(incoming.getQuantity());
		fetchProducts.setPrice(incoming.getPrice());
		return fetchProducts;
	}

	private int addArrivedQuantity(int tillDateQuantity, Map<Integer, List<Integer>> incomingShipmentMap, 
			Product newproduct, FetchIncomingOrderdProducts arrivedOrder, List<Integer> incomingOrderList, Mappingfields mappingFields) {
		List<Integer>incomingOrderIdList=new ArrayList<>();
		List<String>shipmentNoList=new ArrayList<>();
		List<Integer>incomingQtyList=new ArrayList<>();
		List<Boolean>incomingFulfillList=new ArrayList<>();
		List<Boolean>incomingfixedList=new ArrayList<>();
		List<String>branchNoList=new ArrayList<>();
		incomingOrderIdList.add(arrivedOrder.getIncomingShipmentId());
		shipmentNoList.add(arrivedOrder.getShipmentNo());
		int incomingQuantity=arrivedOrder.getConfirmedQty();
		incomingQtyList.add(incomingQuantity);
		incomingFulfillList.add(true);
		incomingfixedList.add(arrivedOrder.isFixed());
		branchNoList.add(arrivedOrder.getBranch());
		if(mappingFields.getIncomingQuantity()==0) {
			mappingFields.setIncomingQuantity(incomingQuantity);
			mappingFields.setIncomingFixed(incomingfixedList);
			mappingFields.setIncomingOrderId(incomingOrderIdList);
			mappingFields.setShipmnetNo(shipmentNoList);
			mappingFields.setIndividualIncomingQty(incomingQtyList);
			mappingFields.setIncomingFulfilment(incomingFulfillList);
			mappingFields.setBranch(branchNoList);
		}else {
			incomingOrderIdList.addAll(mappingFields.getIncomingOrderId());
			shipmentNoList.addAll(mappingFields.getShipmnetNo());
			incomingQtyList.addAll(mappingFields.getIndividualIncomingQty());
			incomingFulfillList.addAll(mappingFields.getIncomingFulfilment());
			incomingfixedList.addAll(mappingFields.getIncomingFixed());
			branchNoList.addAll(mappingFields.getBranch());
			mappingFields.setIncomingQuantity(mappingFields.getIncomingQuantity()+incomingQuantity);
			mappingFields.setIncomingFixed(incomingfixedList);
			mappingFields.setIncomingOrderId(incomingOrderIdList);
			mappingFields.setShipmnetNo(shipmentNoList);
			mappingFields.setIndividualIncomingQty(incomingQtyList);
			mappingFields.setIncomingFulfilment(incomingFulfillList);
			mappingFields.setBranch(branchNoList);
		}
		if(!incomingShipmentMap.containsKey(newproduct.getProductId())){ 
			tillDateQuantity-=arrivedOrder.getConfirmedQty();
			incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
			incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);
		}else {
			List<Integer> idList=incomingShipmentMap.get(newproduct.getProductId());
			if(!idList.contains(arrivedOrder.getIncomingShipmentId())){
				tillDateQuantity-=arrivedOrder.getConfirmedQty();
				incomingOrderList.addAll(incomingShipmentMap.get(newproduct.getProductId()));
				incomingOrderList.add(arrivedOrder.getIncomingShipmentId());
				incomingShipmentMap.put(newproduct.getProductId(),incomingOrderList);

			}
		}
		return tillDateQuantity;
	}



	private List<Order> getFulfilledOrder(List<Order> order, LocalDateTime dueDate) {
		return order.stream()
				.filter(predicate-> predicate.isFulfilled()
						&&(predicate.getDueDate().getYear()==dueDate.getYear()&& predicate.getDueDate().getDayOfMonth()==dueDate.getDayOfMonth()
						&& predicate.getDueDate().getMonth()==dueDate.getMonth()))
				.collect(Collectors.toList());
	}


	public List<FetchIncomingOrderdProducts> getAllArrivedDueDateIncomingShipment(
			List<IncomingShipment> incomingShipment, 
			LocalDateTime dueDate, List<Product> allProduct) {
		List<FetchIncomingOrderdProducts> incomingShipmentFixedList = new ArrayList<>();
		List<FetchIncomingOrderdProducts> incomingShipmentDtoList =getAllIncomingShipment(incomingShipment,allProduct);
		for(int i=0;i<incomingShipmentDtoList.size();i++) {
			if(incomingShipmentDtoList.get(i).isFixed()&&incomingShipmentDtoList.get(i).isArrived()) {
				if(incomingShipmentDtoList.get(i).getFixedDeliveryDate().getDayOfMonth()==dueDate.getDayOfMonth()&&
						incomingShipmentDtoList.get(i).getFixedDeliveryDate().getMonth()==dueDate.getMonth()&&
						incomingShipmentDtoList.get(i).getFixedDeliveryDate().getYear()==dueDate.getYear()) {
					incomingShipmentFixedList.add(incomingShipmentDtoList.get(i));

				}

			}else if(!incomingShipmentDtoList.get(i).isFixed()&&incomingShipmentDtoList.get(i).isArrived()) {
				if(incomingShipmentDtoList.get(i).getDesiredDeliveryDate().getDayOfMonth()==dueDate.getDayOfMonth()&&
						incomingShipmentDtoList.get(i).getDesiredDeliveryDate().getMonth()==dueDate.getMonth()&&
						incomingShipmentDtoList.get(i).getDesiredDeliveryDate().getYear()==dueDate.getYear()) {
					incomingShipmentFixedList.add(incomingShipmentDtoList.get(i));

				}

			}

		}
		return incomingShipmentFixedList;

	}


}
