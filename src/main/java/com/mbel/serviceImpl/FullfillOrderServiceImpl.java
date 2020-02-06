package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.dao.OrderDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.model.Order;
import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;

@Service("FullfillOrderServiceImpl")
public class FullfillOrderServiceImpl {

	@Autowired
	OrderServiceImpl orderServiceImpl;

	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired
	ProductDao productDao;

	@Autowired
	ProductSetDao productSetDao;

	@Autowired
	OrderDao orderDao;

	public ResponseEntity<Map<String, List<ProductSetModel>>> getFullfillOrder(@NotNull int orderId, boolean isFulfillment) {
		PopulateOrderDto order=orderServiceImpl.getOrderById(orderId);
		List<ProductSetModel> productSetModelList = new ArrayList<>();
		Map<String, List<ProductSetModel>> response = new HashMap<>();
		Map<Integer,List<ProductSetModel>>insufficientMap=new HashMap<>();
		Map<Integer,Product>quantityUpdate=new HashMap<>();
		List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
		for(FetchOrderdProducts product:orderdProducts) {
			if(isFulfillment) {
				fulfillOrder(product,productSetModelList,insufficientMap,quantityUpdate);				
			}else {
				revertFulfillOrder(product,productSetModelList,response,insufficientMap,quantityUpdate,order);

			}
		}
		return fulfillOrderStatus(productSetModelList,quantityUpdate,order,response,isFulfillment);


	}




	private ResponseEntity<Map<String, List<ProductSetModel>>> fulfillOrderStatus(List<ProductSetModel> productSetModelList, 
			Map<Integer, Product> quantityUpdate, PopulateOrderDto order, Map<String, List<ProductSetModel>> response, boolean isFulfillment) {

		if(isFulfillment) {
		if(productSetModelList.isEmpty() && (!order.isFulfilled())) {
			Set<Entry<Integer, Product>>updateCurrentQuantity =quantityUpdate.entrySet();
			for(Entry<Integer, Product> update:updateCurrentQuantity) {
				productServiceImpl.getupdateById(update.getKey(), update.getValue());
			}
			updateOdrer(order.getOrderId(),true);
			response.put("fulfilled", productSetModelList);
			return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.ACCEPTED);
		}else {
			response.put("unfulfilled", productSetModelList);
			return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.NOT_ACCEPTABLE);

		}
		}else {			
				Set<Entry<Integer, Product>>updateCurrentQuantity =quantityUpdate.entrySet();
				for(Entry<Integer, Product> update:updateCurrentQuantity) {
					productServiceImpl.getupdateById(update.getKey(), update.getValue());
				}
				updateOdrer(order.getOrderId(),false);
				response.put("reverted", productSetModelList);
				return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.RESET_CONTENT);
			
		}
		
	}




	private ResponseEntity<Map<String, List<ProductSetModel>>> revertFulfillOrder(FetchOrderdProducts product, List<ProductSetModel> productSetModelList,
			Map<String, List<ProductSetModel>> response, Map<Integer, List<ProductSetModel>> insufficientMap,
			Map<Integer, Product> quantityUpdate, PopulateOrderDto order) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			Optional<Product> productValue = productDao.findById(productId);
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProduct(productValue.get());
			stockQuantity =productValue.get().getQuantity();			
			orderdQunatity=product.getQuantity();
			revertStockQuantities(orderdQunatity,stockQuantity,currentQuantity,
					productSetModel,productId,insufficientMap,
					productSetModelList,quantityUpdate);

		}else {				
			FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
			for(ProductSetModel individualProduct:productSet.getProducts()) {
				int individualproductId =individualProduct.getProduct().getProductId();
				stockQuantity =individualProduct.getProduct().getQuantity();			
				orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
				revertStockQuantities(orderdQunatity,stockQuantity,currentQuantity,
						individualProduct,individualproductId,insufficientMap,
						productSetModelList,quantityUpdate);
			}
			quantityUpdate.put(productId,product.getProduct());

		}
		return null;

	}




	private void fulfillOrder(FetchOrderdProducts product,
			List<ProductSetModel> productSetModelList, 
			Map<Integer, List<ProductSetModel>> insufficientMap, Map<Integer, Product> quantityUpdate) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			Optional<Product> productValue = productDao.findById(productId);
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProduct(productValue.get());
			stockQuantity =productValue.get().getQuantity();			
			orderdQunatity=product.getQuantity();
			int amount=product.getQuantity();
			updateStockQuantities(orderdQunatity,stockQuantity,currentQuantity,
					productSetModel,productId,insufficientMap,
					productSetModelList,quantityUpdate,amount);

		}else {				
			FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
			for(ProductSetModel individualProduct:productSet.getProducts()) {
				int individualproductId =individualProduct.getProduct().getProductId();
				stockQuantity =individualProduct.getProduct().getQuantity();			
				orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
				int amount=individualProduct.getQuantity();
				updateStockQuantities(orderdQunatity,stockQuantity,currentQuantity,
						individualProduct,individualproductId,insufficientMap,
						productSetModelList,quantityUpdate,amount);
			}
			quantityUpdate.put(productId,product.getProduct());

		}

	}




	private void updateOdrer(@NotNull int orderId, boolean fulfillment) {
		Order order = orderDao.findById(orderId).get();
		order.setFulfilled(fulfillment);
		orderDao.save(order);
	}
	

	private void revertStockQuantities(int orderdQunatity, int stockQuantity, int currentQuantity,
			ProductSetModel individualProduct, int individualproductId,
			Map<Integer, List<ProductSetModel>> insufficientMap, List<ProductSetModel> productSetModelList,
			Map<Integer, Product> quantityUpdate) {
		currentQuantity = stockQuantity + orderdQunatity;
		individualProduct.getProduct().setQuantity(currentQuantity);
		quantityUpdate.put(individualproductId,individualProduct.getProduct());
		
	}



	private void updateStockQuantities(int orderdQunatity, int stockQuantity, int currentQuantity,
			ProductSetModel individualProduct, int individualproductId, Map<Integer, 
			List<ProductSetModel>> insufficientMap, List<ProductSetModel> productSetModelList, Map<Integer, Product> quantityUpdate, int amount) {
		if(orderdQunatity<=stockQuantity) {
			currentQuantity = stockQuantity - orderdQunatity;
			individualProduct.getProduct().setQuantity(currentQuantity);
			quantityUpdate.put(individualproductId,individualProduct.getProduct());

		}else {
			updateInsuffientProduct(insufficientMap,individualproductId, individualProduct.getProduct(),orderdQunatity,productSetModelList,amount);
		}

	}




	private void updateInsuffientProduct(Map<Integer, List<ProductSetModel>> insufficientMap,
			int productId, Product product, int orderdQunatity, List<ProductSetModel> productSetModelList, int amount) {
		ProductSetModel productSetModel = new ProductSetModel();
		productSetModel.setProduct(product);
		productSetModel.setCurrentQuantity(product.getQuantity());
		productSetModel.setForecast(false);
		productSetModel.setMod(LocalDateTime.now().minusWeeks(product.getLeadTime()+3));
		productSetModel.setQuantity(amount);
		productSetModel.setRequiredQuantity(orderdQunatity);
		productSetModelList.add(productSetModel);
	}








}
