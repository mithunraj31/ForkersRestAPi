package com.mbel.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<String> getFullfillOrder(@NotNull int orderId) {
		PopulateOrderDto order=orderServiceImpl.getOrderById(orderId);
		List<String> mesageList =new ArrayList<>();
		Map<Integer,Product>quantityUpdate=new HashMap<>();
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			for(FetchOrderdProducts product:orderdProducts) {
				int productId = product.getProduct().getProductId();
				if(!product.getProduct().isSet()) {
					productStockCaluculate(product,mesageList,productId,quantityUpdate);
				
			}else {				
					FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
					for(ProductSetModel individualProduct:productSet.getProducts()) {
						int individualproductId =individualProduct.getProduct().getProductId();
						productSetStockCaluculate(product,mesageList,individualproductId,individualProduct,quantityUpdate);							
					}
					quantityUpdate.put(productId,product.getProduct());
				
			}
				
			}
			if(mesageList.isEmpty() && (!order.isFulfilled())) {
				updateOdrer(orderId);
				Set<Entry<Integer, Product>>updateCurrentQuantity =quantityUpdate.entrySet();
				for(Entry<Integer, Product> update:updateCurrentQuantity) {
					productServiceImpl.getupdateById(update.getKey(), update.getValue());
				}
			}
		
		return mesageList;
		
	}
	
	


	private void updateOdrer(@NotNull int orderId) {
		Order order = orderDao.findById(orderId).get();
		order.setFulfilled(true);
		orderDao.save(order);
		}




	public void productSetStockCaluculate(FetchOrderdProducts product, List<String> mesageList,
			int individualproductId, ProductSetModel individualProduct, Map<Integer, Product> quantityUpdate) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
			stockQuantity =individualProduct.getProduct().getQuantity();			
			orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		if(orderdQunatity<=stockQuantity) {
			 currentQuantity = stockQuantity - orderdQunatity;
			 individualProduct.getProduct().setQuantity(currentQuantity);
			 quantityUpdate.put(individualproductId,individualProduct.getProduct());
			
		}else {
			mesageList.add("This product "+individualProduct.getProduct().getProductName()+" is out of stock");
		}
	}



	public void productStockCaluculate(FetchOrderdProducts product, List<String> mesageList, int productId, Map<Integer, Product> quantityUpdate) {
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
			mesageList.add("This product "+productValue.get().getProductName()+" is out of stock");
		}
	}

}
