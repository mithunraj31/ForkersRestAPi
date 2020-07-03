package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.model.IncomingShipment;
import com.mbel.model.Product;
import com.mbel.model.UserEntity;


@Service("IncomingShipmentServiceImpl")
public class IncomingShipmentServiceImpl  {

	@Autowired
	IncomingShipmentDao incomingShipmentDao;

	@Autowired
	JwtAuthenticationFilter jwt;

	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired 
	UserDao userDao;

	@Autowired 
	ProductDao productDao;
	
	

	public @Valid List<IncomingShipment> save(@Valid List<IncomingShipment> incomingShipmentList) {
		String branch = null;
		List<IncomingShipment> newIncomingShipmentList = new ArrayList<>();
		for(IncomingShipment newIncomingShipment:incomingShipmentList) {
		IncomingShipment incomingShipment = new IncomingShipment();
		incomingShipment.setIncomingShipmentId(newIncomingShipment.getIncomingShipmentId()!=0?
				newIncomingShipment.getIncomingShipmentId():0);
		incomingShipment.setCreatedAt(LocalDateTime.now());
		incomingShipment.setShipmentNo(newIncomingShipment.getShipmentNo());
		incomingShipment.setUpdatedAt(LocalDateTime.now());
		incomingShipment.setUserId(jwt.getUserdetails().getUserId());
		incomingShipment.setArrived(false);
		incomingShipment.setActive(true);
		incomingShipment.setProductId(newIncomingShipment.getProductId());
		incomingShipment.setQuantity(newIncomingShipment.getQuantity());
		incomingShipment.setCurrency(newIncomingShipment.getCurrency());
		incomingShipment.setPrice(newIncomingShipment.getPrice());
		incomingShipment.setConfirmedQty(newIncomingShipment.getConfirmedQty());
		incomingShipment.setDesiredDeliveryDate(newIncomingShipment.getDesiredDeliveryDate());
		incomingShipment.setFixedDeliveryDate(newIncomingShipment.getFixedDeliveryDate());
		incomingShipment.setOrderDate(newIncomingShipment.getOrderDate());
		incomingShipment.setVendor(newIncomingShipment.getVendor());
		incomingShipment.setPendingQty(newIncomingShipment.getPendingQty());
		incomingShipment.setFixed((Boolean)newIncomingShipment.isFixed()==null?false:newIncomingShipment.isFixed());
		incomingShipment.setPartial((Boolean)newIncomingShipment.isPartial()==null?false:newIncomingShipment.isPartial());
		if(branch!=null) {
			incomingShipment.setBranch(branch);
		}else {
		incomingShipment.setBranch(newIncomingShipment.isPartial()||!newIncomingShipment.getBranch().equals("")?newIncomingShipment.getBranch():getCurrentBranchNumber(newIncomingShipment));
		}
		branch =String.valueOf(Integer.valueOf(incomingShipment.getBranch())+1);
		newIncomingShipmentList.add(incomingShipment);
		}
		incomingShipmentDao.saveAll(newIncomingShipmentList);
		return incomingShipmentList;

	}

	private String getCurrentBranchNumber(@Valid IncomingShipment newIncomingShipment) {
		String branch ="1";
		List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll().stream()
				.filter(predicate->predicate.getShipmentNo().equals(newIncomingShipment.getShipmentNo())
						&&!predicate.isPartial())
				.collect(Collectors.toList());
		if(!incomingShipment.isEmpty()) {
			Map<Integer,String>branchValue = new HashMap<>();
			for(IncomingShipment incomingProductShipment:incomingShipment) {
				if(!branchValue.containsKey(incomingProductShipment.getProductId())) {
					branchValue.put(incomingProductShipment.getProductId(),incomingProductShipment.getBranch());
				}else if(branchValue.isEmpty()) {
					branchValue.put(incomingProductShipment.getProductId(),branch);
				}
				
			}
			if(branchValue.containsKey(newIncomingShipment.getProductId())&&!newIncomingShipment.isPartial()) {
			branch=String.valueOf(incomingShipment.size()+1);
			}
			else if(!branchValue.containsKey(newIncomingShipment.getProductId())){
				branch=String.valueOf(incomingShipment.size()+1);
			}
			else if(branchValue.containsKey(newIncomingShipment.getProductId())){
				branch=branchValue.get(newIncomingShipment.getProductId());
			}
			
		}
		return branch;
	}

	public List<FetchIncomingOrderdProducts> getAllIncomingShipment(Map<String, String> allParams) {
		List<FetchIncomingOrderdProducts> incomingShipmentDtoList = new ArrayList<>(); 
		List<IncomingShipment> sortedIncomingShipment =getSortedIncomingShipment(allParams);
		List<UserEntity> userEntityList = userDao.findAll();
		List<Product> allProducts = productDao.findAll();
		for(IncomingShipment incoming :sortedIncomingShipment ) {
			FetchIncomingOrderdProducts incomingDto = new FetchIncomingOrderdProducts();
			incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());
			incomingDto.setCreatedAt(incoming.getCreatedAt());
			incomingDto.setShipmentNo(incoming.getShipmentNo());
			incomingDto.setUpdatedAt(LocalDateTime.now());
			incomingDto.setUser(getUserDetails(userEntityList,incoming.getUserId()));
			incomingDto.setArrived(incoming.isArrived());
			incomingDto.setActive(incoming.isActive());
			incomingDto.setProduct(getProduct(incoming,allProducts));
			incomingDto.setBranch(incoming.getBranch());
			incomingDto.setConfirmedQty(incoming.getConfirmedQty());
			incomingDto.setFixed(incoming.isFixed());
			incomingDto.setPartial(incoming.isPartial());		
			incomingDto.setFixedDeliveryDate(incoming.getFixedDeliveryDate());
			incomingDto.setDesiredDeliveryDate(incoming.getDesiredDeliveryDate());
			incomingDto.setOrderDate(incoming.getOrderDate());
			incomingDto.setVendor(incoming.getVendor());
			incomingDto.setPendingQty(incoming.getPendingQty());
			incomingDto.setQuantity(incoming.getQuantity());
			incomingDto.setCurrency(incoming.getCurrency());
			incomingDto.setPrice(incoming.getPrice());
			incomingShipmentDtoList.add(incomingDto);
		}

		return incomingShipmentDtoList;
	}


	private UserEntity getUserDetails(List<UserEntity> userEntityList, int userId) {
		return userEntityList.stream()
				.filter(predicate->predicate.getUserId()==userId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
	}

	private FetchProductSetDto getProduct(IncomingShipment incoming, List<Product> allProducts) {
		FetchProductSetDto fetchProducts = new FetchProductSetDto(); 
		fetchProducts =getProductById(incoming.getProductId(),allProducts);
		fetchProducts.setQuantity(incoming.getQuantity());
		fetchProducts.setPrice(incoming.getPrice());
		return fetchProducts;
	}

	public FetchIncomingOrderdProducts getIncomingShipmentById(@Valid int incomingShipmentId) {
		IncomingShipment incoming = incomingShipmentDao.findById(incomingShipmentId).orElse(null);
		List<Product> allProducts = productDao.findAll();
		FetchIncomingOrderdProducts incomingDto = new FetchIncomingOrderdProducts();
		if(Objects.nonNull(incoming)) {
			incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());
			incomingDto.setCreatedAt(incoming.getCreatedAt());
			incomingDto.setShipmentNo(incoming.getShipmentNo());
			incomingDto.setUpdatedAt(incoming.getUpdatedAt());
			incomingDto.setUser(userDao.findById(incoming.getUserId()).orElse(null));
			incomingDto.setArrived(incoming.isArrived());
			incomingDto.setActive(incoming.isActive());
			incomingDto.setProduct(getProduct(incoming,allProducts));
			incomingDto.setBranch(incoming.getBranch());
			incomingDto.setConfirmedQty(incoming.getConfirmedQty());
			incomingDto.setFixed(incoming.isFixed());
			incomingDto.setPartial(incoming.isPartial());
			incomingDto.setFixedDeliveryDate(incoming.getFixedDeliveryDate());
			incomingDto.setDesiredDeliveryDate(incoming.getDesiredDeliveryDate());
			incomingDto.setOrderDate(incoming.getOrderDate());
			incomingDto.setVendor(incoming.getVendor());
			incomingDto.setPendingQty(incoming.getPendingQty());
			incomingDto.setQuantity(incoming.getQuantity());
			incomingDto.setCurrency(incoming.getCurrency());
			incomingDto.setEditReason(incoming.getEditReason());
			incomingDto.setPrice(incoming.getPrice());
		}
		return incomingDto;
	}


	public FetchProductSetDto getProductById(int productId, List<Product> allProducts) {
		List<Product> notsetProducts=allProducts.stream().filter(predicate->!predicate.isSet()).collect(Collectors.toList());
		Product proCheck =notsetProducts.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
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
			componentSet.setSort(proCheck.getSort());
			componentSet.setDisplay(proCheck.isDisplay());
		}
		return componentSet;
	}


	public ResponseEntity<Map<String, String>> deleteIncomingShipmentById(@Valid int incomingShipmentId) {
		int userId=jwt.getUserdetails().getUserId();
		List<IncomingShipment>saveIncomingList =new ArrayList<>(); 
		Map<String, String> response = new HashMap<>();
		List<IncomingShipment> incomingShipmentList = incomingShipmentDao.findAll();
		
		IncomingShipment incomingShipment=incomingShipmentList.stream()
		.filter(predicate->predicate.getIncomingShipmentId()==incomingShipmentId)
		.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
            if (list.size() != 1) {
            	return null;
            }
            return list.get(0);
        }));
		if(incomingShipment!=null&&incomingShipment.isArrived()) {
			updatingArrivedIncomingShipment(incomingShipmentList,incomingShipment,saveIncomingList,userId);
			
		}else {
			updationgUnArrivedIncomingShipment(incomingShipmentList,incomingShipment,saveIncomingList,userId);
		}
		incomingShipmentDao.saveAll(saveIncomingList);
		response.put(Constants.MESSAGE, "IncomingShipment has been deleted");
		response.put(Constants.INCOMING_SHIPMENT_ID, String.valueOf(incomingShipmentId));
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}
	private void updatingArrivedIncomingShipment(List<IncomingShipment> incomingShipmentList, 
			IncomingShipment incomingShipment, List<IncomingShipment> saveIncomingList, int userId) {
	
		List<IncomingShipment> incomingShipmentPartial=incomingShipmentList.stream()
				.filter(predicate->predicate.getShipmentNo().equals(incomingShipment.getShipmentNo())
						&& predicate.isArrived()&&predicate.getProductId()==incomingShipment.getProductId()
						&& predicate.getBranch().equals(incomingShipment.getBranch()))
				.collect(Collectors.toList());
		
		if(incomingShipment!=null && !incomingShipment.isPartial()) {
			for(IncomingShipment incoming:incomingShipmentPartial) {
				incoming.setActive(false);
				incoming.setUpdatedAt(LocalDateTime.now());
				incoming.setUserId(userId);
				incoming.setEditReason(Constants.DELETED);
				saveIncomingList.add(incoming);
			}
		}else if(incomingShipment!=null && incomingShipment.isPartial()){
					incomingShipment.setActive(false);
					incomingShipment.setUpdatedAt(LocalDateTime.now());
					incomingShipment.setUserId(userId);
					incomingShipment.setEditReason(Constants.DELETED);
					saveIncomingList.add(incomingShipment);
		}
		
	}

	private void updationgUnArrivedIncomingShipment(List<IncomingShipment> incomingShipmentList,
			IncomingShipment incomingShipment, List<IncomingShipment> saveIncomingList, int userId) {

		List<IncomingShipment> incomingShipmentPartial=incomingShipmentList.stream()
				.filter(predicate->predicate.getShipmentNo().equals(incomingShipment.getShipmentNo())
						&& !predicate.isArrived()&&predicate.getProductId()==incomingShipment.getProductId()
						&& predicate.getBranch().equals(incomingShipment.getBranch()))
				.collect(Collectors.toList());
		
		if(incomingShipment!=null && !incomingShipment.isPartial()) {
			for(IncomingShipment incoming:incomingShipmentPartial) {
				incoming.setActive(false);
				incoming.setUpdatedAt(LocalDateTime.now());
				incoming.setUserId(userId);
				incoming.setEditReason(Constants.DELETED);
				saveIncomingList.add(incoming);
				
			}
		}else if(incomingShipment!=null && incomingShipment.isPartial()){
			IncomingShipment addIncomingShipment=incomingShipmentPartial.stream()
			.filter(predicate->!predicate.isFixed())
			.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
	            if (list.size() != 1) {
	            	return null;
	            }
	            return list.get(0);
	        }));
			addIncomingShipment.setPendingQty(addIncomingShipment.getPendingQty()+incomingShipment.getConfirmedQty());
			saveIncomingList.add(addIncomingShipment);
			incomingShipment.setActive(false);
			incomingShipment.setUpdatedAt(LocalDateTime.now());
			incomingShipment.setUserId(userId);
			incomingShipment.setEditReason(Constants.DELETED);
			saveIncomingList.add(incomingShipment);
		}
		
		
	}


	public IncomingShipment getUpdateIncomingShipmentId(int incomingShipmentId,
			@Valid IncomingShipment newIncomingShipment) {
		IncomingShipment incomingShipment = incomingShipmentDao.findById(incomingShipmentId).orElse(null);
		if(Objects.nonNull(incomingShipment)) {
			incomingShipment.setFixedDeliveryDate(newIncomingShipment.getFixedDeliveryDate());
			incomingShipment.setCreatedAt(newIncomingShipment.getCreatedAt());
			incomingShipment.setShipmentNo(newIncomingShipment.getShipmentNo());
			incomingShipment.setUpdatedAt(LocalDateTime.now());
			incomingShipment.setUserId(jwt.getUserdetails().getUserId());
			incomingShipment.setArrived(newIncomingShipment.isArrived());
			incomingShipment.setActive(true);
			incomingShipment.setProductId(newIncomingShipment.getProductId());
			incomingShipment.setQuantity(newIncomingShipment.getQuantity());
			incomingShipment.setPrice(newIncomingShipment.getPrice());
			incomingShipment.setBranch(newIncomingShipment.getBranch());
			incomingShipment.setConfirmedQty(newIncomingShipment.getConfirmedQty());
			incomingShipment.setDesiredDeliveryDate(newIncomingShipment.getDesiredDeliveryDate());
			incomingShipment.setFixed(newIncomingShipment.isFixed());
			incomingShipment.setPartial(newIncomingShipment.isPartial());
			incomingShipment.setFixedDeliveryDate(newIncomingShipment.getFixedDeliveryDate());
			incomingShipment.setOrderDate(newIncomingShipment.getOrderDate());
			incomingShipment.setVendor(newIncomingShipment.getVendor());
			incomingShipment.setPendingQty(newIncomingShipment.getPendingQty());
			incomingShipment.setEditReason(Constants.SHIPMENT_EDITED);
			incomingShipment.setCurrency(newIncomingShipment.getCurrency());
			return incomingShipmentDao.save(incomingShipment);
		}
		return incomingShipment;

		}


		public List<FetchIncomingOrderdProducts> getAllArrivedIncomingShipment() {

			List<FetchIncomingOrderdProducts> incomingShipmentDtoList = new ArrayList<>(); 
			List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll().stream()
					.filter(IncomingShipment::isArrived)
					.collect(Collectors.toList());
			List<UserEntity> userEntityList = userDao.findAll();
			List<Product> allProducts = productDao.findAll();
			for(IncomingShipment incoming :incomingShipment ) {
				FetchIncomingOrderdProducts incomingDto = new FetchIncomingOrderdProducts();
				incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());
				incomingDto.setCreatedAt(incoming.getCreatedAt());
				incomingDto.setShipmentNo(incoming.getShipmentNo());
				incomingDto.setUpdatedAt(LocalDateTime.now());
				incomingDto.setUser(getUserDetails(userEntityList,incoming.getUserId()));
				incomingDto.setArrived(incoming.isArrived());
				incomingDto.setActive(incoming.isActive());
				incomingDto.setProduct(getProduct(incoming,allProducts));
				incomingDto.setBranch(incoming.getBranch());
				incomingDto.setConfirmedQty(incoming.getConfirmedQty());
				incomingDto.setFixed(incoming.isFixed());
				incomingDto.setPartial(incoming.isPartial());		
				incomingDto.setFixedDeliveryDate(incoming.getFixedDeliveryDate());
				incomingDto.setDesiredDeliveryDate(incoming.getDesiredDeliveryDate());
				incomingDto.setOrderDate(incoming.getOrderDate());
				incomingDto.setVendor(incoming.getVendor());
				incomingDto.setPendingQty(incoming.getPendingQty());
				incomingDto.setQuantity(incoming.getQuantity());
				incomingDto.setCurrency(incoming.getCurrency());
				incomingDto.setPrice(incoming.getPrice());
				incomingShipmentDtoList.add(incomingDto);
			}

			return incomingShipmentDtoList;
		
		}

		public IncomingShipment undoConfirmedIncomingOrder(int incomingShipmentId, boolean confirm) {
			IncomingShipment incomingShipment = incomingShipmentDao.findById(incomingShipmentId).orElse(null);
			if(Objects.nonNull(incomingShipment)) {
				incomingShipment.setFixed(confirm);
				incomingShipment.setPendingQty(incomingShipment.getConfirmedQty());
				incomingShipment.setConfirmedQty(0);
				incomingShipment.setUpdatedAt(LocalDateTime.now());
				incomingShipment.setUserId(jwt.getUserdetails().getUserId());
				incomingShipment.setEditReason(confirm?Constants.ORDER_CONFIRMED:Constants.ORDER_NOT_CONFIRMED);
				incomingShipmentDao.save(incomingShipment);
			}
			return incomingShipment;
		}
		
		private List<IncomingShipment> getSortedIncomingShipment(Map<String, String> allParams) {
			List<IncomingShipment> incomingShipmentList =	incomingShipmentDao.findAll().stream()
					.filter(predicate->predicate.isActive())
					.collect(Collectors.toList());
			List<IncomingShipment> sortedIncomingShipmentList  = new ArrayList<>();
			if(isSortAllParamTrue(allParams)) {
				sortedIncomingShipmentList.addAll(incomingShipmentList);
			}else if(isSortAllParamFalse(allParams)) {
				return sortedIncomingShipmentList;
				
			}
			else {
				sortAccordingToParam(allParams,incomingShipmentList,sortedIncomingShipmentList);
			}
			return sortedIncomingShipmentList;
		}

		private void sortAccordingToParam(Map<String, String> allParams, 
				List<IncomingShipment> incomingShipmentList,
				List<IncomingShipment> sortedIncomingShipmentList) {
			if(Boolean.parseBoolean(allParams.get(Constants.NOT_CONFIRMED))) {
				sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
						.filter(predicate->!predicate.isFixed()&&!predicate.isArrived())
						.collect(Collectors.toList()));
			}
				
				if(Boolean.parseBoolean(allParams.get(Constants.NOT_IN_STOCK))){
					if(sortedIncomingShipmentList.isEmpty()) {
					sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
							.filter(predicate->predicate.isFixed()&&!predicate.isArrived())
							.collect(Collectors.toList()));
					}else {
						sortedIncomingShipmentList.clear();
						sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
								.filter(predicate->(predicate.isFixed()||!predicate.isFixed())
										&&!predicate.isArrived())
								.collect(Collectors.toList()));
						
					}
					
				}
				
				if(Boolean.parseBoolean(allParams.get(Constants.ARRIVED))){
					if(sortedIncomingShipmentList.isEmpty()) {
					sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
							.filter(predicate->predicate.isArrived())
							.collect(Collectors.toList()));
					}else {
						sortedIncomingShipmentList.clear();
						arrivedSortedOrders(incomingShipmentList,sortedIncomingShipmentList,allParams);
						
					}
				}
				
				
				
			
			
		}
		
		
		private void arrivedSortedOrders(List<IncomingShipment> incomingShipmentList,
				List<IncomingShipment> sortedIncomingShipmentList, Map<String, String> allParams) {
			if(Boolean.parseBoolean(allParams.get(Constants.NOT_CONFIRMED))
					&&Boolean.parseBoolean(allParams.get(Constants.NOT_IN_STOCK))) {
				sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
						.filter(predicate->(predicate.isFixed()||!predicate.isFixed())
								&&predicate.isArrived())
						.collect(Collectors.toList()));
			}else if(Boolean.parseBoolean(allParams.get(Constants.NOT_CONFIRMED))) {
				sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
						.filter(predicate->!predicate.isFixed()||predicate.isArrived())
						.collect(Collectors.toList()));
				
			}else if(Boolean.parseBoolean(allParams.get(Constants.NOT_IN_STOCK))) {
				sortedIncomingShipmentList.addAll(incomingShipmentList.stream()
						.filter(predicate->predicate.isFixed()||predicate.isArrived())
						.collect(Collectors.toList()));
			}
			
		}

		private boolean isSortAllParamTrue(Map<String, String> allParams) {
			return((Boolean.parseBoolean(allParams.get(Constants.NOT_CONFIRMED))
		    		&&Boolean.parseBoolean(allParams.get(Constants.NOT_IN_STOCK))
		    		&&Boolean.parseBoolean(allParams.get(Constants.ARRIVED))));
			 
		}
		private boolean isSortAllParamFalse(Map<String, String> allParams) {
			return((!Boolean.parseBoolean(allParams.get(Constants.NOT_CONFIRMED))
		    		&&!Boolean.parseBoolean(allParams.get(Constants.NOT_IN_STOCK))
		    		&&!Boolean.parseBoolean(allParams.get(Constants.ARRIVED))));
		}



	}

