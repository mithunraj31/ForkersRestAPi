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

	public ResponseEntity<Map<String, List<ProductSetModel>>> getFullfillOrder(@NotNull int orderId) {
		PopulateOrderDto order=orderServiceImpl.getOrderById(orderId);
		List<ProductSetModel> productSetModelList = new ArrayList<>();
		Map<String, List<ProductSetModel>> response = new HashMap<>();
		Map<Integer,List<ProductSetModel>>insufficientMap=new HashMap<>();
		Map<Integer,Product>quantityUpdate=new HashMap<>();
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			for(FetchOrderdProducts product:orderdProducts) {
				int productId = product.getProduct().getProductId();
				if(!product.getProduct().isSet()) {
					productStockCaluculate(product,insufficientMap,productId,quantityUpdate,productSetModelList);
				
			}else {				
					FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
					for(ProductSetModel individualProduct:productSet.getProducts()) {
						int individualproductId =individualProduct.getProduct().getProductId();
						productSetStockCaluculate(product,insufficientMap,individualproductId,individualProduct,quantityUpdate,productSetModelList);							
					}
					quantityUpdate.put(productId,product.getProduct());
				
			}
				
			}
			if(insufficientMap.isEmpty() && (!order.isFulfilled())) {
				Set<Entry<Integer, Product>>updateCurrentQuantity =quantityUpdate.entrySet();
				for(Entry<Integer, Product> update:updateCurrentQuantity) {
					productServiceImpl.getupdateById(update.getKey(), update.getValue());
				}
				updateOdrer(orderId);
				response.put("Order Fullfilled", productSetModelList);
				 return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.ACCEPTED);
			}else {
		 response.put("Cannot Fullfill due to the following Products Unavailablity", productSetModelList);
		 return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.NOT_ACCEPTABLE);
	
			}
		
	}
	
	


	private void updateOdrer(@NotNull int orderId) {
		Order order = orderDao.findById(orderId).get();
		order.setFulfilled(true);
		orderDao.save(order);
		}




	public void productSetStockCaluculate(FetchOrderdProducts product, Map<Integer, List<ProductSetModel>> insufficientMap,
			int individualproductId, ProductSetModel individualProduct, Map<Integer, Product> quantityUpdate, List<ProductSetModel> productSetModelList) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
			stockQuantity =individualProduct.getProduct().getQuantity();			
			orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		if(orderdQunatity<=stockQuantity) {
			 currentQuantity = stockQuantity - orderdQunatity;
			 individualProduct.getProduct().setQuantity(currentQuantity);
			 quantityUpdate.put(individualproductId,individualProduct.getProduct());
			
		}else {
			updateInsuffientProduct(insufficientMap,individualproductId, individualProduct.getProduct(),orderdQunatity,productSetModelList);
		}
	}



	public void productStockCaluculate(FetchOrderdProducts product, Map<Integer, List<ProductSetModel>> insufficientMap, int productId,
			Map<Integer, Product> quantityUpdate, List<ProductSetModel> productSetModelList) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
		Optional<Product> productValue = productDao.findById(productId);
		if(productValue.isPresent()) {
			stockQuantity =productValue.get().getQuantity();
		}				
			orderdQunatity=product.getQuantity();
		
		if(orderdQunatity<=stockQuantity) {
			currentQuantity = stockQuantity - orderdQunatity;
			productValue.get().setQuantity(currentQuantity);
			quantityUpdate.put(productId, productValue.get());
			
		}else {
			updateInsuffientProduct(insufficientMap,productId, productValue.get(),orderdQunatity,productSetModelList);
		}
	}




	private void updateInsuffientProduct(Map<Integer, List<ProductSetModel>> insufficientMap,
			int productId, Product product, int orderdQunatity, List<ProductSetModel> productSetModelList) {
		if(!insufficientMap.containsKey(productId)) {
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProduct(product);
			productSetModel.setCurrentQuantity(product.getQuantity());
			productSetModel.setForecast(false);
			productSetModel.setMod(LocalDateTime.now().minusWeeks(product.getLeadTime()+3));
			productSetModel.setQuantity(orderdQunatity);
			productSetModel.setRequiredQuantity(orderdQunatity);
			productSetModelList.add(productSetModel);
		}
		
		
	}

}
