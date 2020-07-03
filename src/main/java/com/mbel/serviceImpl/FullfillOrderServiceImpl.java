package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
import com.mbel.dao.OrderDao;
import com.mbel.dao.OrderProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;

@Service("FullfillOrderServiceImpl")
public class FullfillOrderServiceImpl {

	@Autowired
	ProductPredictionServiceImpl productPredictionServiceImpl;

	@Autowired
	ProductDao productDao;

	@Autowired
	ProductSetDao productSetDao;

	@Autowired
	OrderDao orderDao;
	
	@Autowired
	OrderProductDao orderProductDao;

	@Autowired
	JwtAuthenticationFilter jwt;

	public ResponseEntity<Map<String, List<ProductSetModel>>> getFullfillOrder(@NotNull int orderId, boolean isFulfillment) {
		List<OrderProduct>orderProduct =orderProductDao.findAll(); 
		List<Product>allProduct = productDao.findAll();
		List<ProductSet>allProductSet=productSetDao.findAll();
		Order order=orderDao.findById(orderId).orElse(null);
		int userId=jwt.getUserdetails().getUserId();
		List<FetchOrderdProducts> orderdProducts=productPredictionServiceImpl.getAllProducts(order,orderProduct,allProduct,allProductSet);
		List<ProductSetModel> productSetModelList = new ArrayList<>();
		Map<String, List<ProductSetModel>> response = new HashMap<>();
		Map<Integer,Product>quantityUpdate=new HashMap<>();
		if(Objects.nonNull(order)&&order.isFixed()) {
		for(FetchOrderdProducts product:orderdProducts) {
			fulfillOrder(product,productSetModelList,quantityUpdate,allProduct,allProductSet,isFulfillment,userId);				
		}
		return fulfillOrderStatus(productSetModelList,quantityUpdate,order,response,isFulfillment,allProduct,userId);
		}else {
			response.put(Constants.UNFULFILLED, productSetModelList);
			return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.NOT_ACCEPTABLE);
		}


	}




	private ResponseEntity<Map<String, List<ProductSetModel>>> fulfillOrderStatus(List<ProductSetModel> productSetModelList, 
			Map<Integer, Product> quantityUpdate, Order order, Map<String, List<ProductSetModel>> response, boolean isFulfillment, List<Product> allProduct, int userId) {
		List<Product> productList =new ArrayList<>();
		if(isFulfillment) {
			if(productSetModelList.isEmpty() && (!order.isFulfilled())) {
				Set<Entry<Integer, Product>>updateCurrentQuantity =quantityUpdate.entrySet();
				for(Entry<Integer, Product> update:updateCurrentQuantity) {
					productList= getupdateById(update.getKey(), update.getValue(),allProduct,userId);
				}
				updateOdrer(productList,order,true,userId);
				response.put(Constants.FULFILLED, productSetModelList);
				return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.ACCEPTED);
			}else {
				response.put(Constants.UNFULFILLED, productSetModelList);
				return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.NOT_ACCEPTABLE);

			}
		}else {			
			Set<Entry<Integer, Product>>updateCurrentQuantity =quantityUpdate.entrySet();
			for(Entry<Integer, Product> update:updateCurrentQuantity) {
				productList= getupdateById(update.getKey(), update.getValue(),allProduct,userId);
			}
			updateOdrer(productList,order,false,userId);
			response.put(Constants.REVERTED, productSetModelList);
			return new ResponseEntity<Map<String,List<ProductSetModel>>>(response, HttpStatus.RESET_CONTENT);

		}

	}


	private List<Product> getupdateById(int productId, Product productionDetails, List<Product> allProduct, int userId) {
		List<Product>productList = new ArrayList<>();
		Product product = allProduct.stream()
				.filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list->{
					if(list.isEmpty()) {
						return null;
					}else {
						return list.get(0);
					}
				}));
		if(product!=null) {
			product.setProductName(productionDetails.getProductName());
			product.setDescription(productionDetails.getDescription());
			product.setPrice(productionDetails.getPrice());
			product.setMoq(productionDetails.getMoq());
			product.setLeadTime(productionDetails.getLeadTime());
			product.setObicNo(productionDetails.getObicNo());
			product.setQuantity(productionDetails.getQuantity());
			product.setUpdatedAtDateTime(LocalDateTime.now());
			product.setUserId(userId);
			product.setCurrency(productionDetails.getCurrency());
			productList.add(product);
		}
		return productList;
	}




	private void fulfillOrder(FetchOrderdProducts product,
			List<ProductSetModel> productSetModelList, 
			Map<Integer, Product> quantityUpdate, List<Product> allProduct, List<ProductSet> allProductSet, boolean isFulfillment, int userId) {
		int stockQuantity = 0;
		int	orderdQunatity = 0;
		int productId = product.getProduct().getProductId();
		if(!product.getProduct().isSet()) {
			Product productValue =allProduct.stream()
					.filter(predicate->predicate.getProductId()==productId)
					.collect(Collectors.collectingAndThen(Collectors.toList(), list->{
						if(list.isEmpty()) {
							return null;
						}else {
							return list.get(0);
						}
					}));
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProduct(productValue);
			stockQuantity =productValue!=null?productValue.getQuantity():0;			
			orderdQunatity=product.getQuantity();
			int amount=product.getQuantity();
			if(isFulfillment) {
				updateStockQuantities(orderdQunatity,stockQuantity,
						productSetModel,productId,productSetModelList,quantityUpdate,amount,userId);
			}else {
				revertStockQuantities(orderdQunatity,stockQuantity,
						productSetModel,productId,quantityUpdate,userId);
			}

		}else {				

			List<ProductSet> productsetList= allProductSet.stream().filter(predicate->predicate.getSetId()==productId).collect(Collectors.toList());
			if(productsetList != null) {
				for(int l=0;l< productsetList.size();l++ ) {
					ProductSetModel productSetModel = new ProductSetModel();
					int productComponentId=productsetList.get(l).getProductComponentId();
					Product component =allProduct.stream().filter(predicate->predicate.getProductId()==productComponentId)
							.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
								if (list.size() != 1) {
									return null;
								}
								return list.get(0);
							}));
					productSetModel.setProduct(component);
					productSetModel.setQuantity(productsetList.get(l).getQuantity());
					ProductSetModel individualProduct=productSetModel;
					int individualproductId =individualProduct.getProduct().getProductId();
					stockQuantity =individualProduct.getProduct().getQuantity();			
					orderdQunatity=product.getQuantity()*individualProduct.getQuantity();
					int amount=individualProduct.getQuantity();
					if(isFulfillment) {
						updateStockQuantities(orderdQunatity,stockQuantity,
								individualProduct,individualproductId,productSetModelList,quantityUpdate,amount, userId);
					}else {
						revertStockQuantities(orderdQunatity,stockQuantity,
								productSetModel,individualproductId,quantityUpdate, userId);
					}
				}
				quantityUpdate.put(productId,product.getProduct());
			}
		}

	}



	private void updateOdrer(List<Product> productList,Order order, boolean fulfillment, int userId) {
		productDao.saveAll(productList);
		if(Objects.nonNull(order)) {
			order.setFulfilled(fulfillment);
			order.setActive(true);
			order.setUpdatedAt(LocalDateTime.now());
			order.setUserId(userId);
			order.setEditReason(fulfillment?Constants.ORDER_FULFILLED:Constants.ORDER_REVERTED);
			orderDao.save(order);
		}
	}


	private void revertStockQuantities(int orderdQunatity, int stockQuantity, 
			ProductSetModel individualProduct, int individualproductId,
			Map<Integer, Product> quantityUpdate,int userId) {
		int currentQuantity=0;
		currentQuantity = stockQuantity + orderdQunatity;
		individualProduct.getProduct().setQuantity(currentQuantity);
		individualProduct.getProduct().setUpdatedAtDateTime(LocalDateTime.now());
		individualProduct.getProduct().setUserId(userId);
		quantityUpdate.put(individualproductId,individualProduct.getProduct());

	}



	private void updateStockQuantities(int orderdQunatity, int stockQuantity,
			ProductSetModel individualProduct, int individualproductId, 
			List<ProductSetModel> productSetModelList, Map<Integer, Product> quantityUpdate, int amount, int userId) {
		int	currentQuantity = 0;
		if(orderdQunatity<=stockQuantity) {
			currentQuantity = stockQuantity - orderdQunatity;
			individualProduct.getProduct().setQuantity(currentQuantity);
			individualProduct.getProduct().setUpdatedAtDateTime(LocalDateTime.now());
			individualProduct.getProduct().setUserId(userId);
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
