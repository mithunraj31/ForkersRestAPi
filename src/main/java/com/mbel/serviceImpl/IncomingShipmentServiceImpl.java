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
import com.mbel.dao.IncomingShipmentProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.IncomingShipmentDto;
import com.mbel.dto.PopulateIncomingShipmentDto;
import com.mbel.model.IncomingShipment;
import com.mbel.model.IncomingShipmentProduct;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;
import com.mbel.model.UserEntity;


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
	
	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;

	public IncomingShipment save(@Valid IncomingShipmentDto newIncomingShipment) {
		IncomingShipment incomingShipment = new IncomingShipment();
		incomingShipment.setArrivalDate(newIncomingShipment.getArrivalDate());
		incomingShipment.setCreatedAt(LocalDateTime.now());
		incomingShipment.setShipmentNo(newIncomingShipment.getShipmentNo());
		incomingShipment.setUpdatedAt(LocalDateTime.now());
		incomingShipment.setUserId(jwt.getUserdetails().getUserId());
		incomingShipment.setArrived(false);
		IncomingShipment incomeShipment= incomingShipmentDao.save(incomingShipment);
		int shipmentId =incomeShipment.getIncomingShipmentId();		
		int size = newIncomingShipment.getProducts().size();
		for(int i=0;i<size;i++) {
			IncomingShipmentProduct  incomingShipmentProduct= new IncomingShipmentProduct();
			incomingShipmentProduct.setIncomingShipmentId(shipmentId);
			incomingShipmentProduct.setProductId(newIncomingShipment.getProducts().get(i).getProductId());
			incomingShipmentProduct.setQuantity(newIncomingShipment.getProducts().get(i).getQuantity());
			incomingShipmentProduct.setPrice(newIncomingShipment.getProducts().get(i).getPrice());
			incomingShipmentProductDao.save(incomingShipmentProduct);
		}

		return incomeShipment;
	}

	public List<PopulateIncomingShipmentDto> getAllIncomingShipment() {
		List<PopulateIncomingShipmentDto> incomingShipmentDtoList = new ArrayList<>(); 
		List<IncomingShipment> incomingShipment = incomingShipmentDao.findAll();
		List<IncomingShipmentProduct> incomingShipmentProduct =incomingShipmentProductDao.findAll();
		List<UserEntity> userEntityList = userDao.findAll();
		List<Product> allProducts = productDao.findAll();
		List<ProductSet> allProductSet=productSetDao.findAll();
		for(IncomingShipment incoming :incomingShipment ) {
			PopulateIncomingShipmentDto incomingDto = new PopulateIncomingShipmentDto();
			incomingDto.setArrivalDate(incoming.getArrivalDate());
			incomingDto.setCreatedAt(incoming.getCreatedAt());
			incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());			
			incomingDto.setProducts(getAllProduct(incoming.getIncomingShipmentId(),incomingShipmentProduct,allProducts,allProductSet));
			incomingDto.setUpdatedAt(incoming.getUpdatedAt());
			incomingDto.setUser(getUserDetails(userEntityList,incoming.getUserId()));
			incomingDto.setShipmentNo(incoming.getShipmentNo());
			incomingDto.setArrived(incoming.isArrived());
			incomingShipmentDtoList.add(incomingDto);
		}
		
		return incomingShipmentDtoList;
	}

	private UserEntity getUserDetails(List<UserEntity> userEntityList, int userId) {
		return userEntityList.stream()
		.filter(predicate->predicate.getUserId()==userId)
		.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
            if (list.size() != 1) {
                throw new IllegalStateException();
            }
            return list.get(0);
        }));
	}

	private List<FetchIncomingOrderdProducts> getAllProduct(int shipmentId, List<IncomingShipmentProduct> incomingShipmentProduct, List<Product> allProducts, List<ProductSet> allProductSet) {
		List<FetchIncomingOrderdProducts> fetchProducts = new ArrayList<>(); 
		List<IncomingShipmentProduct> shipmentList=incomingShipmentProduct.stream()
		.filter(predicate->predicate.getIncomingShipmentId() == shipmentId)
		.collect(Collectors.toList());
		for(int i=0;i<shipmentList.size();i++) {
			FetchIncomingOrderdProducts incomingOrder =new FetchIncomingOrderdProducts();
			FetchProductSetDto products =(getProductSetById(shipmentList.get(i).getProductId(),allProducts,allProductSet));
		incomingOrder.setProduct(products);
		incomingOrder.setQuantity((Integer)shipmentList.get(i).getQuantity());
		incomingOrder.setPrice((Double)shipmentList.get(i).getPrice());
		fetchProducts.add(incomingOrder);
		}
		return fetchProducts;
	}

	public PopulateIncomingShipmentDto getIncomingShipmentById(@Valid int incomingShipmentId) {
		IncomingShipment incoming = incomingShipmentDao.findById(incomingShipmentId).orElse(null);
		List<Product> allProducts = productDao.findAll();
		List<ProductSet> allProductSet=productSetDao.findAll();
		List<IncomingShipmentProduct> incomingShipmentProduct = incomingShipmentProductDao.findAll();
		PopulateIncomingShipmentDto incomingDto = new PopulateIncomingShipmentDto();
		if(Objects.nonNull(incoming)) {
		incomingDto.setArrivalDate(incoming.getArrivalDate());
		incomingDto.setCreatedAt(incoming.getCreatedAt());
		incomingDto.setIncomingShipmentId(incoming.getIncomingShipmentId());
		incomingDto.setProducts(getAllProduct(incoming.getIncomingShipmentId(),incomingShipmentProduct,allProducts,allProductSet));
		incomingDto.setUpdatedAt(incoming.getUpdatedAt());
		incomingDto.setUser(userDao.findById(incoming.getUserId()).orElse(null));
		incomingDto.setShipmentNo(incoming.getShipmentNo());
		incomingDto.setArrived(incoming.isArrived());
		}
		return incomingDto;
	}

	
	public FetchProductSetDto getProductSetById(int productId, List<Product> allProducts, List<ProductSet> allProductSet) {
		List<Product> notsetProducts=allProducts.stream().filter(predicate->!predicate.isSet()).collect(Collectors.toList());
		Product proCheck =notsetProducts.stream().filter(predicate->predicate.getProductId()==productId)
				.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
					if (list.size() != 1) {
						return null;
					}
					return list.get(0);
				}));
		List<ProductSetModel> productList = new ArrayList<>();
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
		if(proCheck.isSet()) {
			List<ProductSet> productsetList= allProductSet.stream().filter(predicate->predicate.getSetId()==productId).collect(Collectors.toList());
			for(int l=0;l< productsetList.size();l++ ) {
				ProductSetModel productSetModel = new ProductSetModel();
				int productComponentId=productsetList.get(l).getProductComponentId();
				Product component =notsetProducts.stream().filter(predicate->predicate.getProductId()==productComponentId)
						.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
							if (list.size() != 1) {
								return null;
							}
							return list.get(0);
						}));
				productSetModel.setProduct(component);
				productSetModel.setQuantity(productsetList.get(l).getQuantity());
				productList.add(productSetModel);
			}
		}
		}
		componentSet.setProducts(productList);
		return componentSet;



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
		IncomingShipment incomingShipmentUpdate=new IncomingShipment();
		IncomingShipment incomingShipment = incomingShipmentDao.findById(incomingShipmentId).orElse(null);
		if(Objects.nonNull(incomingShipment)) {
		incomingShipment.setArrivalDate(incomingShipmentDetails.getArrivalDate());
		incomingShipment.setUpdatedAt(LocalDateTime.now());
		incomingShipment.setUserId(jwt.getUserdetails().getUserId());
		incomingShipment.setShipmentNo(incomingShipmentDetails.getShipmentNo());
		incomingShipment.setArrived(incomingShipmentDetails.isArrived());
		 incomingShipmentUpdate= incomingShipmentDao.save(incomingShipment);
		}
		 incomingShipmentProductDao.deleteByShipmentId(incomingShipmentId);
			int size = incomingShipmentDetails.getProducts().size();
			for(int i=0;i<size;i++) {
				IncomingShipmentProduct  incomingShipmentProduct= new IncomingShipmentProduct();
				incomingShipmentProduct.setIncomingShipmentId(incomingShipmentId);
				incomingShipmentProduct.setProductId(incomingShipmentDetails.getProducts().get(i).getProductId());
				incomingShipmentProduct.setQuantity(incomingShipmentDetails.getProducts().get(i).getQuantity());
				incomingShipmentProduct.setPrice(incomingShipmentDetails.getProducts().get(i).getPrice());
				incomingShipmentProductDao.save(incomingShipmentProduct);
			}
			return incomingShipmentUpdate;

	}

	public List<PopulateIncomingShipmentDto> getAllUnarrivedDueDateIncomingShipment(LocalDateTime dueDate) {
		List<PopulateIncomingShipmentDto> incomingShipmentDtoList =getAllIncomingShipment();
		return incomingShipmentDtoList.stream()
		.filter(predicate->!predicate.isArrived()
				&&(predicate.getArrivalDate().isBefore(dueDate)
				||predicate.getArrivalDate().isEqual(dueDate)))
		.collect(Collectors.toList());
		 
	}

}

