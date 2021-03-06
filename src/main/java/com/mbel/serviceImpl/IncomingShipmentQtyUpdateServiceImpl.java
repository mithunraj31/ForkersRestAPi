package com.mbel.serviceImpl;

import java.time.LocalDateTime;
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

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.model.IncomingShipment;
import com.mbel.model.Product;

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
		JwtAuthenticationFilter jwt;


		public ResponseEntity<Map<String, String>> updateQuantity(int incomingShipmentId, boolean isArrived) {
			List<IncomingShipment> incomingShipment =new ArrayList<>();
			List<Product> allProduct = productDao.findAll();
			List<IncomingShipment> incomingList =incomingShipmentDao.findAll();
			int userId=jwt.getUserdetails().getUserId();
			IncomingShipment incoming =incomingList.stream()
					.filter(predicate->predicate.getIncomingShipmentId()==incomingShipmentId)
					.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
			            if (list.size() != 1) {
			            	return null;
			            }
			            return list.get(0);
			        }));
			incomingShipment.add(incoming);
			Map<String, String> response = new HashMap<>();
			if(Objects.nonNull(incoming)&&incoming.isFixed()) {
			List<FetchIncomingOrderdProducts> allIncomingProducts=
					productPredictionServiceImpl.getAllIncomingShipment(incomingShipment, allProduct);
				updateArrivedQuantity(allIncomingProducts,allProduct,isArrived,incoming,incomingList,userId);
				response.put(Constants.MESSAGE, Constants.QUANTITY_UPDATED);
				response.put(Constants.INCOMING_SHIPMENT_ID, String.valueOf(incomingShipmentId));

				return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
			}else {
				response.put(Constants.MESSAGE, Constants.QUANTITY_NOT_FIXED);
				response.put(Constants.INCOMING_SHIPMENT_ID, String.valueOf(incomingShipmentId));

				return new ResponseEntity<Map<String,String>>(response, HttpStatus.NOT_ACCEPTABLE);
			}
		
		}

		private void updateArrivedQuantity(List<FetchIncomingOrderdProducts> allIncomingProducts,
				List<Product> allProduct, boolean isArrived, IncomingShipment incoming, List<IncomingShipment> incomingList, int userId) {
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
			product.setUpdatedAt(LocalDateTime.now());
			product.setUserId(userId);
			productDao.save(product);
			incoming.setArrived(true);
			incoming.setUpdatedAt(LocalDateTime.now());
			incoming.setEditReason(Constants.INCOMING_ARRIVED);
			incoming.setUserId(userId);
			unDisplayArrivedWithNoPartialOrder(incoming,incomingList,userId);
			}else if(Objects.nonNull(product)) {
				revertStock(incoming,incomingProduct,product,incomingList,userId);
			}
			}
			
			}

		private void unDisplayArrivedWithNoPartialOrder(IncomingShipment incoming,
				List<IncomingShipment> incomingList, int userId) {
			List<IncomingShipment>incomingUnArrivedList=incomingList.stream()
			.filter(predicate->(predicate.getShipmentNo().equals(incoming.getShipmentNo()))
					&&predicate.getBranch().equals(incoming.getBranch())&&!predicate.isArrived())
			.collect(Collectors.toList());
			if(incomingUnArrivedList.isEmpty()) {
				List<IncomingShipment>incomingUnDisplayList	=incomingList.stream()
				.filter(predicate->(predicate.getShipmentNo().equals(incoming.getShipmentNo()))
						&&predicate.getBranch().equals(incoming.getBranch())&&predicate.isArrived())
				.collect(Collectors.toList());
				incomingUnDisplayList.forEach(action->action.setActive(false));
				incomingShipmentDao.saveAll(incomingUnDisplayList);		
			}else {
				incomingShipmentDao.save(incoming);	
			}
			
			
		}

		private void revertStock(IncomingShipment incoming, FetchIncomingOrderdProducts incomingProduct,
				Product product, List<IncomingShipment> incomingList, int userId) {
			List<IncomingShipment> saveIncomingList = new ArrayList<>();
				product.setQuantity(product.getQuantity()-incomingProduct.getConfirmedQty());
				product.setPrice(product.getPrice()-incomingProduct.getPrice());
				product.setUpdatedAt(LocalDateTime.now());
				product.setUserId(userId);
				productDao.save(product);
				incoming.setArrived(false);
				incoming.setActive(true);
				incoming.setUpdatedAt(LocalDateTime.now());
				incoming.setUserId(userId);
				incoming.setEditReason(Constants.INCOMING_REVERTED);
				saveIncomingList.add(incoming);
				List<IncomingShipment> incominShipmentArrivedOrderList =incomingList.stream()
						.filter(predicate->predicate.getShipmentNo().equals(incoming.getShipmentNo())
								&&predicate.getBranch().equals(incoming.getBranch())&&predicate.isArrived())
						.collect(Collectors.toList());
				incominShipmentArrivedOrderList.forEach(action->action.setActive(true));
				saveIncomingList.addAll(incominShipmentArrivedOrderList);
			incomingShipmentDao.saveAll(saveIncomingList);	
			
		}
			
		}

