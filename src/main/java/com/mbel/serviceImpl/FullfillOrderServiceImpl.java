package com.mbel.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			List<FetchOrderdProducts> orderdProducts = order.getOrderedProducts();
			for(FetchOrderdProducts product:orderdProducts) {
				int productId = product.getProduct().getProductId();
				if(!product.getProduct().isSet()) {
					productStockCaluculate(product,mesageList,productId);
				
			}else {
				int packageStockQuantity = 0,packageOrderdQunatity=0,packagecurrentQuantity=0;
				packageStockQuantity =product.getProduct().getQuantity();
				packageOrderdQunatity=product.getQuantity();
				if(packageOrderdQunatity<=packageStockQuantity) {
					packagecurrentQuantity=packageStockQuantity - packageOrderdQunatity;
					product.getProduct().setQuantity(packagecurrentQuantity);
					productServiceImpl.getupdateById(productId, product.getProduct());
					
				}else {
					mesageList.add(product.getProduct().getProductName());
				}
				FetchProductSetDto productSet = productServiceImpl.getProductSetById(productId);
				for(ProductSetModel individualProduct:productSet.getProducts()) {
					int individualproductId =individualProduct.getProduct().getProductId();
					productSetStockCaluculate(product,mesageList,individualproductId,individualProduct);
					
					
				}
			}
				
			}
			if(mesageList.isEmpty() && (!order.isFullfilled())) {
				updateOdrer(orderId);
			}
		
		return mesageList;
		
	}
	
	


	private void updateOdrer(@NotNull int orderId) {
		Order order = orderDao.findById(orderId).get();
		order.setFullfilled(true);
		orderDao.save(order);
		}




	private void productSetStockCaluculate(FetchOrderdProducts product, List<String> mesageList,
			int individualproductId, ProductSetModel individualProduct) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
			stockQuantity =individualProduct.getProduct().getQuantity();			
			orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
		if(orderdQunatity<=stockQuantity) {
			 currentQuantity = stockQuantity - orderdQunatity;
			 individualProduct.getProduct().setQuantity(currentQuantity);
			productServiceImpl.getupdateById(individualproductId, individualProduct.getProduct());
			
		}else {
			mesageList.add(individualProduct.getProduct().getProductName());
		}
	}




	public void productStockCaluculate(FetchOrderdProducts product, List<String> mesageList, int productId) {
		int stockQuantity = 0,orderdQunatity = 0,currentQuantity = 0;
		Optional<Product> productValue = productDao.findById(productId);
		if(productValue.isPresent()) {
			stockQuantity =productValue.get().getQuantity();
		}				
			orderdQunatity=product.getQuantity();
		
		if(orderdQunatity<=stockQuantity) {
			currentQuantity = stockQuantity - orderdQunatity;
			productValue.get().setQuantity(currentQuantity);
			productServiceImpl.getupdateById(productId, productValue.get());
			
		}else {
			mesageList.add(productValue.get().getProductName());
		}
	}

}
