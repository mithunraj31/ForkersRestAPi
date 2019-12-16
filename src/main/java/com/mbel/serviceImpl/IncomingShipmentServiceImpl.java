package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.IncomingShipmentProductDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.IncomingShipmentDto;
import com.mbel.dto.PopulateIncomingShipmentDto;
import com.mbel.model.IncomingShipment;
import com.mbel.model.IncomingShipmentProduct;


@Service("IncomingShipmentServiceImpl")
public class IncomingShipmentServiceImpl  {

	@Autowired
	IncomingShipmentDao incomingShipmentDao;
	
	@Autowired
	IncomingShipmentProductDao incomingShipmentProductDao;

	@Autowired
	JwtAuthenticationFilter jwt;
	
	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired 
	UserDao userDao;

	public IncomingShipment save(@Valid IncomingShipmentDto newIncomingShipment) {
		IncomingShipment incomingShipment = new IncomingShipment();
		incomingShipment.setArrivalDate(newIncomingShipment.getArrivalDate());
		incomingShipment.setCreatedAt(LocalDateTime.now());
		incomingShipment.setIncomingShipmentIncomingShipmentId(newIncomingShipment.getIncomingShipmentIncomingShipmentId());
		incomingShipment.setShipmentNo(newIncomingShipment.getShipmentNo());
		incomingShipment.setUpdatedAt(LocalDateTime.now());
		incomingShipment.setUserId(jwt.getUserdetails().getUserId());
		IncomingShipment incomeShipment= incomingShipmentDao.save(incomingShipment);
		int shipmentId =incomeShipment.getIncomingShipmentId();		
		int size = newIncomingShipment.getProduct().size();
		for(int i=0;i<size;i++) {
			IncomingShipmentProduct  incomingShipmentProduct= new IncomingShipmentProduct();
			incomingShipmentProduct.setIncomingShipmentId(shipmentId);
			incomingShipmentProduct.setProductId(newIncomingShipment.getProduct().get(i).getProductId());
			incomingShipmentProduct.setQuantity(newIncomingShipment.getProduct().get(i).getQuantity());
			incomingShipmentProduct.setPrice(newIncomingShipment.getProduct().get(i).getPrice());
			incomingShipmentProductDao.save(incomingShipmentProduct);
		}

		return incomeShipment;
	}

	public List<PopulateIncomingShipmentDto> getAllIncomingShipment() {
		List<PopulateIncomingShipmentDto> incomingShipmentDtoList = new ArrayList<>(); 
		List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll();
		for(IncomingShipment incoming :incomingShipment ) {
			PopulateIncomingShipmentDto incomingDto = new PopulateIncomingShipmentDto();
			incomingDto.setArrivalDate(incoming.getArrivalDate());
			incomingDto.setCreatedAt(incoming.getCreatedAt());
			incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());			
			incomingDto.setProducts(getAllProduct(incoming.getIncomingShipmentId()));
			incomingDto.setUpdatedAt(incoming.getUpdatedAt());
			incomingDto.setUser(userDao.findById(incoming.getUserId()).get());
			incomingDto.setShipmentNo(incoming.getShipmentNo());
			incomingShipmentDtoList.add(incomingDto);
		}
		
		return incomingShipmentDtoList;
	}

	private List<FetchOrderdProducts> getAllProduct(int shipmentId) {
		List<FetchOrderdProducts> fetchProducts = new ArrayList<>(); 
		List<Map<Object, Object>> shipmentList=incomingShipmentProductDao.getByShipmentId(shipmentId);
		for(int i=0;i<shipmentList.size();i++) {
			FetchProductSetDto products = new FetchProductSetDto();
			FetchOrderdProducts incomingOrder =new FetchOrderdProducts();
		products =(productServiceImpl.getProductSetById((Integer)shipmentList.get(i).get("product_id")));
		incomingOrder.setProduct(products);
		incomingOrder.setQuantity((Integer)shipmentList.get(i).get("qty"));
		fetchProducts.add(incomingOrder);
		}
		return fetchProducts;
	}

	public PopulateIncomingShipmentDto getIncomingShipmentById(@Valid int incomingShipmentId) {
		IncomingShipment incoming = incomingShipmentDao.findById(incomingShipmentId).get();
		PopulateIncomingShipmentDto incomingDto = new PopulateIncomingShipmentDto();
		incomingDto.setArrivalDate(incoming.getArrivalDate());
		incomingDto.setCreatedAt(incoming.getCreatedAt());
		incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());
		incomingDto.setProducts(getAllProduct(incoming.getIncomingShipmentId()));
		incomingDto.setUpdatedAt(incoming.getUpdatedAt());
		incomingDto.setUser(userDao.findById(incoming.getUserId()).get());
		incomingDto.setShipmentNo(incoming.getShipmentNo());
		
		return incomingDto;
	}


	public ResponseEntity<Map<String, String>> deleteIncomingShipmentById(@Valid int incomingShipmentId) {
		Map<String, String> response = new HashMap<>();
		incomingShipmentDao.deleteById(incomingShipmentId);
		 incomingShipmentProductDao.deleteByShipmentId(incomingShipmentId);
		response.put("message", "IncomingShipment has been deleted");
		response.put("IncomingShipmentId", String.valueOf(incomingShipmentId));
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}

	public IncomingShipment getUpdateIncomingShipmentId(int incomingShipmentId,
			@Valid IncomingShipmentDto incomingShipmentDetails) {
		IncomingShipment incomingShipment = incomingShipmentDao.findById(incomingShipmentId).get();
		incomingShipment.setArrivalDate(incomingShipmentDetails.getArrivalDate());
		incomingShipment.setIncomingShipmentIncomingShipmentId(incomingShipmentDetails.getIncomingShipmentIncomingShipmentId());
		incomingShipment.setUpdatedAt(LocalDateTime.now());
		incomingShipment.setUserId(jwt.getUserdetails().getUserId());
		incomingShipment.setShipmentNo(incomingShipmentDetails.getShipmentNo());
		IncomingShipment incomingShipmentUpdate= incomingShipmentDao.save(incomingShipment);
		 incomingShipmentProductDao.deleteByShipmentId(incomingShipmentId);
			int size = incomingShipmentDetails.getProduct().size();
			for(int i=0;i<size;i++) {
				IncomingShipmentProduct  incomingShipmentProduct= new IncomingShipmentProduct();
				incomingShipmentProduct.setIncomingShipmentId(incomingShipmentId);
				incomingShipmentProduct.setProductId(incomingShipmentDetails.getProduct().get(i).getProductId());
				incomingShipmentProduct.setQuantity(incomingShipmentDetails.getProduct().get(i).getQuantity());
				incomingShipmentProduct.setPrice(incomingShipmentDetails.getProduct().get(i).getPrice());
				incomingShipmentProductDao.save(incomingShipmentProduct);
			}
			return incomingShipmentUpdate;

	}

}

