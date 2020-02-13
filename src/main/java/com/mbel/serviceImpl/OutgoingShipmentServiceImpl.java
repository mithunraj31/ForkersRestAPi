package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.constants.Constants;
import com.mbel.dao.CustomerDao;
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
	
	@Autowired
	CustomerDao customerDao;
	
	

	public OutgoingShipment save(@Valid OutgoingShipmentDto newOutgoingShipment) {
		OutgoingShipment outgoingShipment = new OutgoingShipment();
		outgoingShipment.setCreatedAt(LocalDateTime.now());
		outgoingShipment.setSalesDestinationId(newOutgoingShipment.getSalesDestinationId());
		outgoingShipment.setShipmentNo(newOutgoingShipment.getShipmentNo());
		outgoingShipment.setUpdatedAt(LocalDateTime.now());
		outgoingShipment.setUserId(jwt.getUserdetails().getUserId());
		outgoingShipment.setShipmentDate(newOutgoingShipment.getShipmentDate());
		OutgoingShipment incomeShipment= outgoingShipmentDao.save(outgoingShipment);
		int shipmentId =incomeShipment.getOutgoingShipmentId();		
		int size = newOutgoingShipment.getProducts().size();
		for(int i=0;i<size;i++) {
			OutgoingShipmentProduct  outgoingShipmentProduct= new OutgoingShipmentProduct();
			outgoingShipmentProduct.setOutgoingShipmentId(shipmentId);
			outgoingShipmentProduct.setProductId(newOutgoingShipment.getProducts().get(i).getProductId());
			outgoingShipmentProduct.setQuantity(newOutgoingShipment.getProducts().get(i).getQuantity());
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
			outgoingDto.setSalesDestination(customerDao.findById(outgoing.getSalesDestinationId()).orElse(null));
			outgoingDto.setOutgoingShipmentId(outgoing.getOutgoingShipmentId());
			outgoingDto.setProducts(getAllProduct(outgoing.getOutgoingShipmentId()));
			outgoingDto.setUpdatedAt(outgoing.getUpdatedAt());
			outgoingDto.setUser(userDao.findById(outgoing.getUserId()).orElse(null));
			outgoingDto.setShipmentDate(outgoing.getShipmentDate());
			outgoingDto.setShipmentNo(outgoing.getShipmentNo());
			
			outgoingShipmentDtoList.add(outgoingDto);
		}
		
		return outgoingShipmentDtoList;
	}

	private List<FetchOrderdProducts> getAllProduct(int shipmentId) {
		List<FetchOrderdProducts> fetchProducts = new ArrayList<>(); 
		List<Map<Object, Object>> shipmentList=outgoingShipmentProductDao.getByShipmentId(shipmentId);
		for(int i=0;i<shipmentList.size();i++) {
			FetchOrderdProducts outgoingOrder =new FetchOrderdProducts();
			FetchProductSetDto products =(productServiceImpl.getProductSetById((Integer)shipmentList.get(i).get(Constants.PRODUCT_ID)));
		outgoingOrder.setProduct(products);
		outgoingOrder.setQuantity((Integer)shipmentList.get(i).get(Constants.QTY));
		fetchProducts.add(outgoingOrder);
		}
		return fetchProducts;
	}

	public PopulateOutgoingShipmentDto getOutgoingShipmentById(@Valid int outgoingShipmentId) {
		PopulateOutgoingShipmentDto outgoingDto = new PopulateOutgoingShipmentDto();
		OutgoingShipment outgoing = outgoingShipmentDao.findById(outgoingShipmentId).orElse(null);
		if(Objects.nonNull(outgoing)) {
		outgoingDto.setCreatedAt(outgoing.getCreatedAt());
		outgoingDto.setSalesDestination(customerDao.findById(outgoing.getSalesDestinationId()).orElse(null));
		outgoingDto.setOutgoingShipmentId(outgoing.getOutgoingShipmentId());
		outgoingDto.setProducts(getAllProduct(outgoing.getOutgoingShipmentId()));
		outgoingDto.setShipmentNo(outgoing.getShipmentNo());
		outgoingDto.setUpdatedAt(outgoing.getUpdatedAt());
		outgoingDto.setUser(userDao.findById(outgoing.getUserId()).orElse(null));
		outgoingDto.setShipmentDate(outgoing.getShipmentDate());
		}
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
		OutgoingShipment outgoingShipmentUpdate = new OutgoingShipment();
		OutgoingShipment outgoingShipment = outgoingShipmentDao.findById(outgoingShipmentId).orElse(null);
		if(Objects.nonNull(outgoingShipment)) {
		outgoingShipment.setSalesDestinationId(outgoingShipmentDetails.getSalesDestinationId());
		outgoingShipment.setShipmentNo(outgoingShipmentDetails.getShipmentNo());
		outgoingShipment.setUpdatedAt(LocalDateTime.now());
		outgoingShipment.setUserId(jwt.getUserdetails().getUserId());
		outgoingShipment.setShipmentDate(outgoingShipmentDetails.getShipmentDate());
		outgoingShipmentUpdate= outgoingShipmentDao.save(outgoingShipment);
		outgoingShipmentProductDao.deleteByShipmentId(outgoingShipmentId);
		}
			int size = outgoingShipmentDetails.getProducts().size();
			for(int i=0;i<size;i++) {
				OutgoingShipmentProduct  outgoingShipmentProduct= new OutgoingShipmentProduct();
				outgoingShipmentProduct.setOutgoingShipmentId(outgoingShipmentId);
				outgoingShipmentProduct.setProductId(outgoingShipmentDetails.getProducts().get(i).getProductId());
				outgoingShipmentProduct.setQuantity(outgoingShipmentDetails.getProducts().get(i).getQuantity());
				outgoingShipmentProductDao.save(outgoingShipmentProduct);
			}
			return outgoingShipmentUpdate;

	}

}

