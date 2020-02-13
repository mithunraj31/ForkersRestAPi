package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
		Map<Integer,Product>quantityUpdate=new HashMap<>();
		List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
		for(FetchOrderdProducts product:orderdProducts) {
			if(isFulfillment) {
				fulfillOrder(product,productSetModelList,quantityUpdate);				
			}else {
				revertFulfillOrder(product,quantityUpdate);

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




	private void revertFulfillOrder(FetchOrderdProducts product,
			Map<Integer, Product> quantityUpdate) {
		int stockQuantity = 0;
		int	orderdQunatity = 0;
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			Product productValue = productDao.findById(productId).orElse(null);
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProduct(productValue);
			stockQuantity =productValue!=null?productValue.getQuantity():0;			
			orderdQunatity=product.getQuantity();
			revertStockQuantities(orderdQunatity,stockQuantity,
					productSetModel,productId,quantityUpdate);

		}else {				
			FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
			for(ProductSetModel individualProduct:productSet.getProducts()) {
				int individualproductId =individualProduct.getProduct().getProductId();
				stockQuantity =individualProduct.getProduct().getQuantity();			
				orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
				revertStockQuantities(orderdQunatity,stockQuantity,
						individualProduct,individualproductId,quantityUpdate);
			}
			quantityUpdate.put(productId,product.getProduct());

		}

	}




	private void fulfillOrder(FetchOrderdProducts product,
			List<ProductSetModel> productSetModelList, 
			 Map<Integer, Product> quantityUpdate) {
		int stockQuantity = 0;
		int	orderdQunatity = 0;
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			Product productValue = productDao.findById(productId).orElse(null);
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProduct(productValue);
			stockQuantity =productValue!=null?productValue.getQuantity():0;			
			orderdQunatity=product.getQuantity();
			int amount=product.getQuantity();
			updateStockQuantities(orderdQunatity,stockQuantity,
					productSetModel,productId,productSetModelList,quantityUpdate,amount);

		}else {				
			FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
			for(ProductSetModel individualProduct:productSet.getProducts()) {
				int individualproductId =individualProduct.getProduct().getProductId();
				stockQuantity =individualProduct.getProduct().getQuantity();			
				orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
				int amount=individualProduct.getQuantity();
				updateStockQuantities(orderdQunatity,stockQuantity,
						individualProduct,individualproductId,productSetModelList,quantityUpdate,amount);
			}
			quantityUpdate.put(productId,product.getProduct());

		}

	}




	private void updateOdrer(@NotNull int orderId, boolean fulfillment) {
		Order order = orderDao.findById(orderId).orElse(null);
		if(Objects.nonNull(order)) {
		order.setFulfilled(fulfillment);
		orderDao.save(order);
		}
	}


	private void revertStockQuantities(int orderdQunatity, int stockQuantity, 
			ProductSetModel individualProduct, int individualproductId,
			Map<Integer, Product> quantityUpdate) {
		int currentQuantity=0;
		currentQuantity = stockQuantity + orderdQunatity;
		individualProduct.getProduct().setQuantity(currentQuantity);
		quantityUpdate.put(individualproductId,individualProduct.getProduct());

	}



	private void updateStockQuantities(int orderdQunatity, int stockQuantity,
			ProductSetModel individualProduct, int individualproductId, 
			List<ProductSetModel> productSetModelList, Map<Integer, Product> quantityUpdate, int amount) {
		int	currentQuantity = 0;
		if(orderdQunatity<=stockQuantity) {
			currentQuantity = stockQuantity - orderdQunatity;
			individualProduct.getProduct().setQuantity(currentQuantity);
			quantityUpdate.put(individualproductId,individualProduct.getProduct());

		}else {
			updateInsuffientProduct(individualProduct.getProduct(),orderdQunatity,productSetModelList,amount);
		}

	}




	private void updateInsuffientProduct(
			 Product product, int orderdQunatity, List<ProductSetModel> productSetModelList, int amount) {
		ProductSetModel productSetModel = new ProductSetModel();
		productSetModel.setProduct(product);
		productSetModel.setCurrentQuantity(product.getQuantity());
		productSetModel.setForecast(false);
		productSetModel.setMod(LocalDateTime.now().minusWeeks(product.getLeadTime()+3L));
		productSetModel.setQuantity(amount);
		productSetModel.setRequiredQuantity(orderdQunatity);
		productSetModelList.add(productSetModel);
	}



}
