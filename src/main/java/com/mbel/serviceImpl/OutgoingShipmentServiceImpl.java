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
import com.mbel.dao.OutgoingShipmentDao;
import com.mbel.dao.OutgoingShipmentProductDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.OutgoingShipmentDto;
import com.mbel.dto.PopulateOutgoingShipmentDto;
import com.mbel.model.OutgoingShipment;
import com.mbel.model.OutgoingShipmentProduct;


@Service("OutgoingShipmentServiceImpl")
public class OutgoingShipmentServiceImpl  {

	@Autowired
    OutgoingShipmentDao outgoingShipmentDao;
	
	@Autowired
	OutgoingShipmentProductDao outgoingShipmentProductDao;

	@Autowired
	JwtAuthenticationFilter jwt;
	
	@Autowired
	ProductServiceImpl productServiceImpl;

	@Autowired 
	UserDao userDao;

	public OutgoingShipment save(@Valid OutgoingShipmentDto newOutgoingShipment) {
		OutgoingShipment outgoingShipment = new OutgoingShipment();
		outgoingShipment.setDestination(newOutgoingShipment.getDestination());
		outgoingShipment.setCreatedAt(LocalDateTime.now());
		outgoingShipment.setDestination(newOutgoingShipment.getDestination());
		outgoingShipment.setProductId(newOutgoingShipment.getProductId());
		outgoingShipment.setUpdatedAt(LocalDateTime.now());
		outgoingShipment.setQuantity(newOutgoingShipment.getQuantity());
		outgoingShipment.setUserId(jwt.getUserdetails().getUserId());
		outgoingShipment.setShipDate(newOutgoingShipment.getShipDate());
		OutgoingShipment incomeShipment= outgoingShipmentDao.save(outgoingShipment);
		int shipmentId =incomeShipment.getOutgoingShipmentId();		
		int size = newOutgoingShipment.getProduct().size();
		for(int i=0;i<size;i++) {
			OutgoingShipmentProduct  outgoingShipmentProduct= new OutgoingShipmentProduct();
			outgoingShipmentProduct.setOutgoingShipmentOutgoingShipmentProductId(shipmentId);
			outgoingShipmentProduct.setProductProductId(newOutgoingShipment.getProduct().get(i).getProductProductId());
			outgoingShipmentProduct.setQuantity(newOutgoingShipment.getProduct().get(i).getQuantity());
			outgoingShipmentProductDao.save(outgoingShipmentProduct);
		}

		return incomeShipment;
	}

	public List<PopulateOutgoingShipmentDto> getAllOutgoingShipment() {
		List<PopulateOutgoingShipmentDto> outgoingShipmentDtoList = new ArrayList<>(); 
		List<OutgoingShipment> outgoingShipment = outgoingShipmentDao.findAll();
		for(OutgoingShipment outgoing :outgoingShipment ) {
			PopulateOutgoingShipmentDto outgoingDto = new PopulateOutgoingShipmentDto();
			outgoingDto.setCreatedAt(outgoing.getCreatedAt());
			outgoingDto.setDestination(outgoing.getDestination());
			outgoingDto.setOutgoingShipmentId(outgoing.getOutgoingShipmentId());
			outgoingDto.setProducts(getAllProduct(outgoing.getOutgoingShipmentId()));
			outgoingDto.setProductId(outgoing.getProductId());
			outgoingDto.setUpdatedAt(outgoing.getUpdatedAt());
			outgoingDto.setUser(userDao.findById(outgoing.getUserId()).get());
			outgoingDto.setShipDate(outgoing.getShipDate());
			outgoingDto.setQuantity(outgoing.getQuantity());
			
			outgoingShipmentDtoList.add(outgoingDto);
		}
		
		return outgoingShipmentDtoList;
	}

	private List<FetchOrderdProducts> getAllProduct(int shipmentId) {
		List<FetchOrderdProducts> fetchProducts = new ArrayList<>(); 
		List<Map<Object, Object>> shipmentList=outgoingShipmentProductDao.getByShipmentId(shipmentId);
		for(int i=0;i<shipmentList.size();i++) {
			FetchProductSetDto products = new FetchProductSetDto();
			FetchOrderdProducts outgoingOrder =new FetchOrderdProducts();
		products =(productServiceImpl.getProductSetById((Integer)shipmentList.get(i).get("product_product_id")));
		outgoingOrder.setProduct(products);
		outgoingOrder.setQuantity((Integer)shipmentList.get(i).get("qty"));
		fetchProducts.add(outgoingOrder);
		}
		return fetchProducts;
	}

	public PopulateOutgoingShipmentDto getOutgoingShipmentById(@Valid int outgoingShipmentId) {
		OutgoingShipment outgoing = outgoingShipmentDao.findById(outgoingShipmentId).get();
		PopulateOutgoingShipmentDto outgoingDto = new PopulateOutgoingShipmentDto();
		outgoingDto.setCreatedAt(outgoing.getCreatedAt());
		outgoingDto.setDestination(outgoing.getDestination());
		outgoingDto.setOutgoingShipmentId(outgoing.getOutgoingShipmentId());
		outgoingDto.setProducts(getAllProduct(outgoing.getOutgoingShipmentId()));
		outgoingDto.setProductId(outgoing.getProductId());
		outgoingDto.setUpdatedAt(outgoing.getUpdatedAt());
		outgoingDto.setUser(userDao.findById(outgoing.getUserId()).get());
		outgoingDto.setShipDate(outgoing.getShipDate());
		outgoingDto.setQuantity(outgoing.getQuantity());
		
		return outgoingDto;
	}


	public ResponseEntity<Map<String, String>> deleteOutgoingShipmentById(@Valid int outgoingShipmentId) {
		Map<String, String> response = new HashMap<>();
		outgoingShipmentDao.deleteById(outgoingShipmentId);
		outgoingShipmentProductDao.deleteByShipmentId(outgoingShipmentId);
		response.put("message", "OutgoingShipment has been deleted");
		response.put("OutgoingShipmentId", String.valueOf(outgoingShipmentId));
		return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
	}

	public OutgoingShipment getUpdateOutgoingShipmentId(int outgoingShipmentId,
			@Valid OutgoingShipmentDto outgoingShipmentDetails) {
		OutgoingShipment outgoingShipment = outgoingShipmentDao.findById(outgoingShipmentId).get();
		outgoingShipment.setDestination(outgoingShipmentDetails.getDestination());
		outgoingShipment.setProductId(outgoingShipmentDetails.getProductId());
		outgoingShipment.setUpdatedAt(LocalDateTime.now());
		outgoingShipment.setUserId(jwt.getUserdetails().getUserId());
		outgoingShipment.setShipDate(outgoingShipmentDetails.getShipDate());
		outgoingShipment.setQuantity(outgoingShipmentDetails.getQuantity());
		OutgoingShipment outgoingShipmentUpdate= outgoingShipmentDao.save(outgoingShipment);
		outgoingShipmentProductDao.deleteByShipmentId(outgoingShipmentId);
			int size = outgoingShipmentDetails.getProduct().size();
			for(int i=0;i<size;i++) {
				OutgoingShipmentProduct  outgoingShipmentProduct= new OutgoingShipmentProduct();
				outgoingShipmentProduct.setOutgoingShipmentOutgoingShipmentProductId(outgoingShipmentId);
				outgoingShipmentProduct.setProductProductId(outgoingShipmentDetails.getProduct().get(i).getProductProductId());
				outgoingShipmentProduct.setQuantity(outgoingShipmentDetails.getProduct().get(i).getQuantity());
				outgoingShipmentProductDao.save(outgoingShipmentProduct);
			}
			return outgoingShipmentUpdate;

	}

}

