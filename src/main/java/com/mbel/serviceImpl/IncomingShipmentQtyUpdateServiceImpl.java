package com.mbel.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.model.IncomingShipment;
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

		public ResponseEntity<Map<String, String>> updateQuantity(int incomingShipmentId, boolean isArrived) {
			List<IncomingShipment> incomingShipment =new ArrayList<>();
			List<Product> allProduct = productDao.findAll();
			IncomingShipment incoming =incomingShipmentDao.findById(incomingShipmentId).orElse(null);
			incomingShipment.add(incoming);
			Map<String, String> response = new HashMap<>();
			if(Objects.nonNull(incoming)&&incoming.isFixed()) {
			List<FetchIncomingOrderdProducts> allIncomingProducts=
					productPredictionServiceImpl.getAllIncomingShipment(incomingShipment, allProduct);
				updateArrivedQuantity(allIncomingProducts,allProduct,isArrived,incoming);
				response.put("message", "Incoming Quantity Updated");
				response.put("incomingShipmentId", String.valueOf(incomingShipmentId));

				return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
			}else {
				response.put("message", "Incoming Quantity is not fixed");
				response.put("incomingShipmentId", String.valueOf(incomingShipmentId));

				return new ResponseEntity<Map<String,String>>(response, HttpStatus.NOT_ACCEPTABLE);
			}
		
		}

		private void updateArrivedQuantity(List<FetchIncomingOrderdProducts> allIncomingProducts,
				List<Product> allProduct, boolean isArrived, IncomingShipment incoming) {
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
			product.setQuantity(product.getQuantity()+incomingProduct.getConfirmedQty());
			product.setPrice(product.getPrice()+incomingProduct.getPrice());
			productDao.save(product);
			incoming.setArrived(true);
			}else if(Objects.nonNull(product)) {
				product.setQuantity(product.getQuantity()-incomingProduct.getConfirmedQty());
				product.setPrice(product.getPrice()-incomingProduct.getPrice());
				productDao.save(product);
				incoming.setArrived(false);
			}
			}
			incomingShipmentDao.save(incoming);	
			
			}
			
		}

