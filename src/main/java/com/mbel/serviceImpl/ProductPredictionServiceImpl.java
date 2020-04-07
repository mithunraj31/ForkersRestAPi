package com.mbel.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.IncomingShipmentProductDao;
import com.mbel.dao.OrderDao;
import com.mbel.dao.OrderProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.PopulateIncomingShipmentDto;
import com.mbel.dto.ProductDataDto;
import com.mbel.dto.ProductPredictionDto;
import com.mbel.model.IncomingShipment;
import com.mbel.model.IncomingShipmentProduct;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;
import com.mbel.model.PredictionData;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;

@Service("ProductPredictionServiceImpl")
public class ProductPredictionServiceImpl {

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
	IncomingShipmentProductDao incomingShipmentProductDao;

	public List<ProductPredictionDto> getProductPrediction(int year,int month) {
		List<Product> allProduct = getAllSortedProducts();
		List<ProductSet> allProductSet =productSetDao.findAll();
		List<Order>order =orderDao.findAll().stream().filter(Order::isActive).collect(Collectors.toList()); 
		List<OrderProduct>orderProduct =orderProductDao.findAll(); 
		List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll();
		List<IncomingShipmentProduct> incomingProducts = incomingShipmentProductDao.findAll();
		return predictProduct(allProduct,allProductSet, order,orderProduct,incomingShipment,incomingProducts,year,month);

	}

	private List<Product> getAllSortedProducts() {
		List<Product>product =productDao.findAll();
		return productServiceImpl.arrangeProductbySortField(product);
	}

	private List<ProductPredictionDto> predictProduct(List<Product> allProduct, List<ProductSet> allProductSet,
			List<Order> order, List<OrderProduct> orderProduct, List<IncomingShipment> incomingShipment,
			List<IncomingShipmentProduct> incomingProducts, int year, int month) {
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
				List<ProductDataDto>productDataDtoList=new ArrayList<>();
				for(int l=0;l< productsetList.size();l++ ) {
					ProductDataDto productDataDto =new ProductDataDto();
					predictionDataList = new ArrayList<>();
					int productComponentId =productsetList.get(l).getProductComponentId();
					Product component =allProduct.stream().filter(predicate->predicate.getProductId()==productComponentId)
							.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
								if (list.size() != 1) {
									return null;
								}
								return list.get(0);
							}));
					productDataDto.setDescription(component.getDescription());
					productDataDto.setProductId(component.getProductId());
					productDataDto.setObicNo(component.getObicNo());
					productDataDto.setProductName(component.getProductName());
					List<PredictionData> data =calculateAccordingToDate(component, year,month,predictionDataList,order,
							incomingShipment,orderProduct,allProduct,allProductSet,incomingProducts);
					productDataDto.setValues(data);
					productDataDtoList.add(productDataDto);
				}
				productPredictionDto.setProducts(productDataDtoList);
				productPredictionDtoList.add(productPredictionDto);
			}
	
		
			ProductPredictionDto productPredictionDto =new ProductPredictionDto();
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
					,predictionDataList,order,incomingShipment,orderProduct,allProduct,allProductSet,incomingProducts);
			productDataDto.setValues(data);
			productDataDtoList.add(productDataDto);
			}
			
			productPredictionDto.setProducts(productDataDtoList);
			productPredictionDtoList.add(productPredictionDto);
		
		return productPredictionDtoList;
		} 


	private List<PredictionData> calculateAccordingToDate(Product product, int year, int month, List<PredictionData> predictionDataList, 
			List<Order> order, List<IncomingShipment> incomingShipment, List<OrderProduct> orderProduct, List<Product> allProduct, 
			List<ProductSet> allProductSet, List<IncomingShipmentProduct> incomingProducts) {
		LocalDateTime today = LocalDateTime.now();
		LocalDate initial = LocalDate.of(year, month, 1);
		LocalDateTime dueDateStart =LocalDateTime.of(year, month, 1, 0, 0);
		if(month==1) {
			dueDateStart=LocalDateTime.of(year-1, 12, 1, 0, 0);
		}else if(dueDateStart.getMonthValue()<=today.getMonthValue()&&
				dueDateStart.getYear()==today.getYear()){
			dueDateStart=LocalDateTime.of(year, month-1, 1, 0, 0);
		}else if(dueDateStart.getYear()<today.getYear()) {
			dueDateStart=LocalDateTime.of(year, month-1, 1, 0, 0);
		}else {
			dueDateStart=LocalDateTime.of(year, today.getMonth().minus(1), 1, 0, 0);
		}
		LocalDateTime dueDateEnd =LocalDateTime.of(year, month, initial.lengthOfMonth(), 0, 0);
		Map<Integer,Mappingfields>productQuantityMap=new HashMap<>();
		for(LocalDateTime dueDate=dueDateStart;dueDate.isBefore(dueDateEnd)||
				dueDate.isEqual(dueDateEnd);dueDate=dueDate.plusDays(1)) {
			List<Order> unfulfilledorder =getUnfulfilledActiveOrder(order,dueDate);
			PredictionData predictionData=new PredictionData();
			Map<Integer,List<Mappingfields>>productDetails=new HashMap<>();
			Map<Integer,List<Integer>>incomingShipmentMap=new HashMap<>();
			if(!unfulfilledorder.isEmpty()) {
				for(Order individualOrder:unfulfilledorder) {
					List<FetchOrderdProducts> orderdProducts= getAllProducts(individualOrder,orderProduct,allProduct,allProductSet);
					for(FetchOrderdProducts productCheck:orderdProducts) {
						checkProductStatus(productDetails,productCheck, dueDate, productQuantityMap, incomingShipmentMap,incomingShipment,incomingProducts,allProduct,allProductSet);

					}
				}
			}
			int numOrdered=productNumOrdered(unfulfilledorder,product,orderProduct,allProduct,allProductSet);
			if(dueDate.getMonth()==initial.getMonth()) {
				updateDailyStockValues(numOrdered,productDetails,predictionData,
						dueDate,productQuantityMap,product,predictionDataList);
			}
		}
		return predictionDataList;
	}

		public void updateDailyStockValues(int numOrdered,
				Map<Integer, List<Mappingfields>> productDetails, PredictionData predictionData, LocalDateTime dueDate, 
				Map<Integer, Mappingfields> productQuantityMap, Product product, List<PredictionData> predictionDataList) {
		if(numOrdered > 1) {
			List<Mappingfields> orderedTimes=productDetails.get(product.getProductId());
			int requiredQuantity =0;
			int currentQuantity =0;
			for(int i=0;i < orderedTimes.size();i++) {
				requiredQuantity+=orderedTimes.get(i).getRequiredQuantity();
				currentQuantity=orderedTimes.get(0).getCurrentQuantity();
			}
			predictionData.setDate(dueDate);
			predictionData.setCurrentQuantity(currentQuantity);
			predictionData.setRequiredQuantity(requiredQuantity);
			predictionData.setIncomingQuantity(productQuantityMap.get(product.getProductId()).getIncomingQuantity());
			predictionData.setQuantity(product.getQuantity());
		
		}else if(numOrdered==0&&productQuantityMap.containsKey(product.getProductId())) {
			predictionData.setDate(dueDate);
			predictionData.setCurrentQuantity(productQuantityMap.get(product.getProductId()).getAvailableStockQuantity());
			predictionData.setQuantity(product.getQuantity());
			predictionData.setRequiredQuantity(0);
			if(productQuantityMap.get(product.getProductId()).getIncomingQuantity()!=0) {
			predictionData.setIncomingQuantity(productQuantityMap.get(product.getProductId()).getIncomingQuantity());
			}

		}else if(productQuantityMap.containsKey(product.getProductId())){
			predictionData.setDate(dueDate);
			predictionData.setCurrentQuantity(productQuantityMap.get(product.getProductId()).getAvailableStockQuantity());
			predictionData.setRequiredQuantity(productQuantityMap.get(product.getProductId()).getRequiredQuantity());
			predictionData.setQuantity(product.getQuantity());
			if(productQuantityMap.get(product.getProductId()).getIncomingQuantity()!=0) {
				predictionData.setIncomingQuantity(productQuantityMap.get(product.getProductId()).getIncomingQuantity());
				}

		}else {
			predictionData.setDate(dueDate);
			predictionData.setCurrentQuantity(product.getQuantity());
			predictionData.setQuantity(product.getQuantity());
		    predictionData.setRequiredQuantity(0);
			predictionData.setIncomingQuantity(0);
		}
		predictionDataList.add(predictionData);
		}


	private int productNumOrdered(List<Order> unfulfilledorder, Product product, List<OrderProduct> orderProduct, List<Product> allProduct, List<ProductSet> allProductSet) {
		List<FetchOrderdProducts> filteredProductSet=new ArrayList<>();
		List<ProductSetModel> filteredProduct=new ArrayList<>();
		if(!unfulfilledorder.isEmpty()) {
			for(Order individualOrder:unfulfilledorder) {
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

	public List<FetchOrderdProducts> getAllProducts(Order individualOrder, List<OrderProduct> orderProduct, List<Product> allProduct, List<ProductSet> allProductSet) {
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
			componentSet.setCreatedAtDateTime(proCheck.getCreatedAtDateTime());
			componentSet.setUpdatedAtDateTime(proCheck.getUpdatedAtDateTime());
			componentSet.setCurrency(proCheck.getCurrency());
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

	public void checkProductStatus(Map<Integer, List<Mappingfields>> productDetails, FetchOrderdProducts product, LocalDateTime dueDate,
			Map<Integer, Mappingfields> productQuantityMap,
			Map<Integer, List<Integer>> incomingShipmentMap, List<IncomingShipment> incomingShipment,
			List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet) {

		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {

			productStockCaluculate(productDetails,product,dueDate,
					productQuantityMap,incomingShipmentMap,incomingShipment,incomingProducts,allProduct,allProductSet);
		}else {
			Mappingfields mappingPackage =new Mappingfields();
			mappingPackage.setPackageProduct(product.getProduct());
			for(ProductSetModel individualProduct:product.getProduct().getProducts()) {
				productSetStockCaluculate(productDetails,product,individualProduct,dueDate,
						productQuantityMap,incomingShipmentMap,incomingShipment,incomingProducts,allProduct,allProductSet);
			}
			mappingPackage.setPackageQuantity(product.getQuantity());
			productQuantityMap.put(productId,mappingPackage);
		}
	}

	public void productSetStockCaluculate(Map<Integer, List<Mappingfields>> productDetails, FetchOrderdProducts product,
			ProductSetModel individualProduct,LocalDateTime dueDate, 
			Map<Integer, Mappingfields> productQuantityMap, 
			Map<Integer, List<Integer>> incomingShipmentMap,List<IncomingShipment> incomingShipment,
			List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet) {
		Mappingfields mappingFields =new Mappingfields();
		int stockQuantity=0;
		int orderdQunatity = 0;
		orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		mappingFields.setProduct(individualProduct.getProduct());
		mappingFields.setOrderdQuantity(individualProduct.getQuantity());
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setSet(true);
		mappingFields.setIncomingQuantity(0);
		stockQuantity=individualProduct.getProduct().getQuantity();
		updateStockValues(individualProduct.getProduct(),stockQuantity,orderdQunatity,
				dueDate,mappingFields,productQuantityMap,incomingShipmentMap,incomingShipment,incomingProducts,allProduct,allProductSet);
		multipleProductOrder(productDetails,individualProduct.getProduct().getProductId(),mappingFields);
	}

	private void productStockCaluculate(Map<Integer, List<Mappingfields>> productDetails, FetchOrderdProducts productCheck,
			LocalDateTime dueDate, Map<Integer, Mappingfields> productQuantityMap, Map<Integer, List<Integer>> incomingShipmentMap,
			List<IncomingShipment> incomingShipment, List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet) {
		int stockQuantity=0;
		int orderdQunatity = 0;
		Mappingfields mappingFields =new Mappingfields();
		Product productValue =productCheck.getProduct();
		orderdQunatity=productCheck.getQuantity();
		stockQuantity =productValue.getQuantity();
		mappingFields.setProduct(productValue);
		mappingFields.setOrderdQuantity(orderdQunatity);
		mappingFields.setRequiredQuantity(orderdQunatity);
		mappingFields.setIncomingQuantity(0);
		updateStockValues(productValue,stockQuantity,orderdQunatity,dueDate,mappingFields,
				productQuantityMap,incomingShipmentMap,incomingShipment,incomingProducts, allProduct, allProductSet);
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
			List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet) {
		int tillDateQuantity=0;
		if(!productQuantityMap.containsKey(product.getProductId())) {
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap,incomingShipment, incomingProducts,allProduct,allProductSet,mappingFields);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);
		}else {
			stockQuantity = productQuantityMap.get(product.getProductId()).getAvailableStockQuantity();
			tillDateQuantity =getTillDateQuantity(product,stockQuantity,dueDate,incomingShipmentMap,incomingShipment, incomingProducts, allProduct, allProductSet,mappingFields);
			mappingFields.setCurrentQuantity(tillDateQuantity);
			mappingFields.setAvailableStockQuantity(tillDateQuantity-orderdQunatity);
			productQuantityMap.put(product.getProductId(), mappingFields);

		}
	}

	public int getTillDateQuantity(Product newproduct, int stockQuantity, LocalDateTime dueDate, 
			Map<Integer, List<Integer>> incomingShipmentMap, List<IncomingShipment> incomingShipment,
			List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet, Mappingfields mappingFields) {
		List<Integer>incomingOrderList=new ArrayList<>();
		int tillDateQuantity = stockQuantity;
		List<PopulateIncomingShipmentDto> incomingShipmentList=getAllUnarrivedDueDateIncomingShipment(incomingShipment,incomingProducts,dueDate,allProduct,allProductSet);
		for(PopulateIncomingShipmentDto arrivedOrder:incomingShipmentList) {
			for(FetchIncomingOrderdProducts incomingProduct: arrivedOrder.getProducts()) {
				if(newproduct.getProductId() == incomingProduct.getProduct().getProductId()) {
					tillDateQuantity=addArrivedQuantity(tillDateQuantity,incomingShipmentMap,newproduct,arrivedOrder,incomingOrderList,incomingProduct,mappingFields);
				}
			}
		}
		return tillDateQuantity;

	}

	public List<PopulateIncomingShipmentDto> getAllUnarrivedDueDateIncomingShipment(
			List<IncomingShipment> incomingShipment, List<IncomingShipmentProduct> incomingProducts,
			LocalDateTime dueDate, List<Product> allProduct, List<ProductSet> allProductSet) {
		List<PopulateIncomingShipmentDto> incomingShipmentDtoList =getAllIncomingShipment(incomingShipment,incomingProducts,allProduct,allProductSet);
		return incomingShipmentDtoList.stream()
				.filter(predicate->!predicate.isArrived()
						&&(predicate.getArrivalDate().getDayOfMonth()==dueDate.getDayOfMonth()
								&& predicate.getArrivalDate().getMonth()==dueDate.getMonth()))
				.collect(Collectors.toList());

	}

	public List<PopulateIncomingShipmentDto> getAllIncomingShipment(List<IncomingShipment> incomingShipment,
			List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet) {
		List<PopulateIncomingShipmentDto> incomingShipmentDtoList = new ArrayList<>(); 
		for(IncomingShipment incoming :incomingShipment ) {
			PopulateIncomingShipmentDto incomingDto = new PopulateIncomingShipmentDto();
			incomingDto.setArrivalDate(incoming.getArrivalDate());
			incomingDto.setCreatedAt(incoming.getCreatedAt());
			incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());			
			incomingDto.setProducts(getAllIncomingProduct(incoming.getIncomingShipmentId(),incomingProducts,allProduct,allProductSet));
			incomingDto.setUpdatedAt(incoming.getUpdatedAt());
			incomingDto.setShipmentNo(incoming.getShipmentNo());
			incomingDto.setArrived(incoming.isArrived());
			incomingShipmentDtoList.add(incomingDto);
		}

		return incomingShipmentDtoList;
	}

	public List<FetchIncomingOrderdProducts> getAllIncomingProduct(int incomingShipmentId, 
			List<IncomingShipmentProduct> incomingProducts, List<Product> allProduct, List<ProductSet> allProductSet) {
		List<FetchIncomingOrderdProducts> fetchProducts = new ArrayList<>(); 
		List<IncomingShipmentProduct> shipmentList=incomingProducts.stream().filter(predicate->predicate.getIncomingShipmentId()==incomingShipmentId).collect(Collectors.toList());
		for(int i=0;i<shipmentList.size();i++) {
			FetchIncomingOrderdProducts incomingOrder =new FetchIncomingOrderdProducts();
			FetchProductSetDto products =getProductSetById(shipmentList.get(i).getProductId(),allProduct,allProductSet);
			incomingOrder.setProduct(products);
			incomingOrder.setQuantity(shipmentList.get(i).getQuantity());
			incomingOrder.setPrice(shipmentList.get(i).getPrice());
			fetchProducts.add(incomingOrder);
		}
		return fetchProducts;
	}

	private int addArrivedQuantity(int tillDateQuantity, Map<Integer, List<Integer>> incomingShipmentMap, 
			Product newproduct, PopulateIncomingShipmentDto arrivedOrder, List<Integer> incomingOrderList, FetchIncomingOrderdProducts incomingProduct, Mappingfields mappingFields) {
		mappingFields.setIncomingQuantity(incomingProduct.getQuantity());
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




	private List<Order> getUnfulfilledActiveOrder(List<Order> order, LocalDateTime dueDate) {
		return order.stream()
				.filter(predicate->predicate.isActive() && !predicate.isFulfilled() 
						&&( predicate.getDueDate().getDayOfMonth()==dueDate.getDayOfMonth()
						&& predicate.getDueDate().getMonth()==dueDate.getMonth()))
				.collect(Collectors.toList());

	}


}
