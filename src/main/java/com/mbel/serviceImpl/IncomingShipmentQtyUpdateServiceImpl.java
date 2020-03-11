package com.mbel.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.IncomingShipmentProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.model.IncomingShipment;
import com.mbel.model.IncomingShipmentProduct;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;

@Service("IncomingShipmentQtyUpdateServiceImpl")
public class IncomingShipmentQtyUpdateServiceImpl {
		
		
		@Autowired
		IncomingShipmentServiceImpl incomingShipmentServiceImpl;
		
		@Autowired
		ProductPredictionServiceImpl productPredictionServiceImpl;
		
		@Autowired
		IncomingShipmentDao incomingShipmentDao;
		
		@Autowired 
		ProductDao productDao;

		@Autowired 
		ProductSetDao productSetDao;

		@Autowired 
		IncomingShipmentProductDao incomingShipmentProductDao;
		
		public void updateQuantity(int incomingShipmentId, boolean isArrived) {
			List<Product> allProduct = productDao.findAll();
			List<ProductSet> allProductSet =productSetDao.findAll();
			List<IncomingShipmentProduct> incomingProducts = incomingShipmentProductDao.findAll();
			
			List<FetchIncomingOrderdProducts> allIncomingProducts=
					productPredictionServiceImpl.getAllIncomingProduct(incomingShipmentId, incomingProducts, allProduct, allProductSet);
				updateArrivedQuantity(incomingShipmentId,allIncomingProducts,allProduct,isArrived);
		
		}

		private void updateArrivedQuantity(int shipmentId, List<FetchIncomingOrderdProducts> allIncomingProducts, List<Product> allProduct, boolean isArrived) {
			List<Product> productList =new ArrayList<>();
			for(FetchIncomingOrderdProducts incomingProduct: allIncomingProducts) {
			Product product =allProduct.stream()
					.filter(predicate->predicate.getProductId()==incomingProduct.getProduct().getProductId())
					.collect(Collectors.collectingAndThen(Collectors.toList(), list->{
						if(list.isEmpty()) {
							return null;
						}else {
							return list.get(0);
						}
					}));
			if(Objects.nonNull(product)&&isArrived) {
			product.setQuantity(product.getQuantity()+incomingProduct.getQuantity());
			product.setPrice(product.getPrice()+incomingProduct.getPrice());
			productList.add(product);
			}else if(Objects.nonNull(product)) {
				product.setQuantity(product.getQuantity()-incomingProduct.getQuantity());
				product.setPrice(product.getPrice()-incomingProduct.getPrice());
				productList.add(product);
			}
			}
			productDao.saveAll(productList);
			IncomingShipment incomingShipmentUpdate = incomingShipmentDao.findById(shipmentId).orElse(null);
			if(Objects.nonNull(incomingShipmentUpdate) && isArrived) {
			incomingShipmentUpdate.setArrived(true);
			}else if(Objects.nonNull(incomingShipmentUpdate)) {
			incomingShipmentUpdate.setArrived(false);
			}
			incomingShipmentDao.save(incomingShipmentUpdate);	
			
			}
			
		}

