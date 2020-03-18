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
import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.IncomingShipmentDto;
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

	public IncomingShipment save(@Valid IncomingShipmentDto newIncomingShipment) {
		IncomingShipment incomingShipment = new IncomingShipment();
		incomingShipment.setIncomingShipmentId(newIncomingShipment.getIncomingShipmentId()!=0?
				newIncomingShipment.getIncomingShipmentId():0);
		incomingShipment.setCreatedAt(LocalDateTime.now());
		incomingShipment.setShipmentNo(newIncomingShipment.getShipmentNo());
		incomingShipment.setUpdatedAt(LocalDateTime.now());
		incomingShipment.setUserId(jwt.getUserdetails().getUserId());
		incomingShipment.setArrived(false);
		incomingShipment.setProductId(newIncomingShipment.getProductId());
		incomingShipment.setQuantity(newIncomingShipment.getQuantity());
		incomingShipment.setCurrency(newIncomingShipment.getCurrency());
		incomingShipment.setPrice(newIncomingShipment.getPrice());
		incomingShipment.setBranch(newIncomingShipment.getBranch());
		incomingShipment.setConfirmedQty(newIncomingShipment.getConfirmedQty());
		incomingShipment.setDesiredDeliveryDate(newIncomingShipment.getDesiredDeliveryDate());
		incomingShipment.setFixedDeliveryDate(newIncomingShipment.getFixedDeliveryDate());
		incomingShipment.setOrderDate(LocalDateTime.now());
		incomingShipment.setVendor(newIncomingShipment.getVendor());
		incomingShipment.setPendingQty(newIncomingShipment.getPendingQty());
		incomingShipment.setFixed((Boolean)newIncomingShipment.isFixed()==null?false:newIncomingShipment.isFixed());
		incomingShipment.setPartial((Boolean)newIncomingShipment.isFixed()==null?false:newIncomingShipment.isFixed());
		return incomingShipmentDao.save(incomingShipment);

	}

	public List<FetchIncomingOrderdProducts> getAllIncomingShipment() {
		List<FetchIncomingOrderdProducts> incomingShipmentDtoList = new ArrayList<>(); 
		List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll();
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
			incomingDto.setProduct(getProduct(incoming,allProducts));
			incomingDto.setBranch(incoming.getBranch());
			incomingDto.setConfirmedQty(incoming.getConfirmedQty());
			incomingDto.setFixed(incoming.isFixed());
			incomingDto.setPartial(incoming.isPartial());		
			incomingDto.setFixedDeliveryDate(incoming.getFixedDeliveryDate());
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
			incomingDto.setUpdatedAt(LocalDateTime.now());
			incomingDto.setUser(userDao.findById(incoming.getUserId()).orElse(null));
			incomingDto.setArrived(incoming.isArrived());
			incomingDto.setProduct(getProduct(incoming,allProducts));
			incomingDto.setBranch(incoming.getBranch());
			incomingDto.setConfirmedQty(incoming.getConfirmedQty());
			incomingDto.setFixed(incoming.isFixed());
			incomingDto.setPartial(incoming.isPartial());
			incomingDto.setFixedDeliveryDate(incoming.getFixedDeliveryDate());
			incomingDto.setOrderDate(incoming.getOrderDate());
			incomingDto.setVendor(incoming.getVendor());
			incomingDto.setPendingQty(incoming.getPendingQty());
			incomingDto.setQuantity(incoming.getQuantity());
			incomingDto.setCurrency(incoming.getCurrency());
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
		Map<String, String> response = new HashMap<>();
		incomingShipmentDao.deleteById(incomingShipmentId);
		response.put("message", "IncomingShipment has been deleted");
		response.put("IncomingShipmentId", String.valueOf(incomingShipmentId));
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}

	public IncomingShipment getUpdateIncomingShipmentId(int incomingShipmentId,
			@Valid IncomingShipmentDto newIncomingShipment) {
		IncomingShipment incomingShipment = incomingShipmentDao.findById(incomingShipmentId).orElse(null);
		if(Objects.nonNull(incomingShipment)) {
			incomingShipment.setFixedDeliveryDate(newIncomingShipment.getFixedDeliveryDate());
			incomingShipment.setCreatedAt(newIncomingShipment.getCreatedAt());
			incomingShipment.setShipmentNo(newIncomingShipment.getShipmentNo());
			incomingShipment.setUpdatedAt(LocalDateTime.now());
			incomingShipment.setUserId(jwt.getUserdetails().getUserId());
			incomingShipment.setArrived(false);
			incomingShipment.setProductId(newIncomingShipment.getProductId());
			incomingShipment.setQuantity(newIncomingShipment.getQuantity());
			incomingShipment.setPrice(newIncomingShipment.getPrice());
			incomingShipment.setBranch(newIncomingShipment.getBranch());
			incomingShipment.setConfirmedQty(newIncomingShipment.getConfirmedQty());
			incomingShipment.setDesiredDeliveryDate(newIncomingShipment.getDesiredDeliveryDate());
			incomingShipment.setFixed(newIncomingShipment.isFixed());
			incomingShipment.setPartial(newIncomingShipment.isPartial());
			incomingShipment.setFixedDeliveryDate(newIncomingShipment.getFixedDeliveryDate());
			incomingShipment.setOrderDate(LocalDateTime.now());
			incomingShipment.setVendor(newIncomingShipment.getVendor());
			incomingShipment.setPendingQty(newIncomingShipment.getPendingQty());
			incomingShipment.setCurrency(newIncomingShipment.getCurrency());
			return incomingShipmentDao.save(incomingShipment);
		}
		return incomingShipment;

		}

		public List<FetchIncomingOrderdProducts> getAllUnarrivedDueDateIncomingShipment(LocalDateTime dueDate) {
			List<FetchIncomingOrderdProducts> incomingShipmentDtoList =getAllIncomingShipment();
			return incomingShipmentDtoList.stream()
					.filter(predicate->!predicate.isArrived()
							&&(predicate.getFixedDeliveryDate().isBefore(dueDate)
									||predicate.getFixedDeliveryDate().isEqual(dueDate)))
					.collect(Collectors.toList());

		}

	}

