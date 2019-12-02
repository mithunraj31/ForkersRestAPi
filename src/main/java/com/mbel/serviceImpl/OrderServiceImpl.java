package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.config.JwtAuthenticationFilter;
import com.mbel.dao.CustomerDao;
import com.mbel.dao.OrderDao;
import com.mbel.dao.OrderProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dao.UserDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.FetchProductSetDto;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.dto.SaveOrderSetDto;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;




@Service("OrderServiceImpl")
public class OrderServiceImpl  {

	@Autowired
	OrderDao orderDao;

	@Autowired
	OrderProductDao orderProductDao;

	@Autowired
	CustomerDao customerDao;

	@Autowired
	ProductServiceImpl productServiceImpl;
	
	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;
	
	@Autowired 
	UserDao userDao;
	
	@Autowired
	 JwtAuthenticationFilter jwt;
	

	public List<Order> getActiveOrders() {
		List<Order>order =orderDao.findAll();
		List<Order>activeOrder = new ArrayList<>();
		for(Order od:order) {
			if(od.isActive()) {
				activeOrder.add(od);
			}
		}
		return activeOrder;
	}

	public List<PopulateOrderDto> getAllOrders() {
		List<Order>activeOrder =getActiveOrders();
		List<PopulateOrderDto>populateList =new ArrayList<>();
		for(Order order:activeOrder) {
			PopulateOrderDto populate = new PopulateOrderDto();
			populate.setOrderId(order.getOrderId());
			populate.setProposalNo(order.getProposalNo());
			populate.setReceivedDate(order.getReceivedDate());
			populate.setDueDate(order.getDueDate());
			populate.setActive(order.isActive());
			populate.setForecast(order.isForecast());
			populate.setUserId(jwt.getUserdetails());
			populate.setSalesUserId(userDao.findById(order.getSalesUserId()).get());
			populate.setEditReason(order.getEditReason());
			populate.setCreatedAt(order.getCreatedAt());
			populate.setUpdatedAt(order.getUpdatedAt());
			populate.setCustomer(customerDao.findById(order.getCustomerId()).get());
			populate.setSalesDestinarion(customerDao.findById(order.getSalesDestination()).get());
			populate.setContractor(customerDao.findById(order.getContractorId()).get());
			populate.setOrderedProducts(getAllProducts(order.getOrderId()));
			populateList.add(populate);
		}

		return populateList;

	}

	public PopulateOrderDto getOrderById(int orderId) {
		Order order = orderDao.findById(orderId).get();
		PopulateOrderDto populate = new PopulateOrderDto();
		populate.setOrderId(order.getOrderId());
		populate.setProposalNo(order.getProposalNo());
		populate.setReceivedDate(order.getReceivedDate());
		populate.setDueDate(order.getDueDate());
		populate.setActive(order.isActive());
		populate.setForecast(order.isForecast());
		populate.setUserId(jwt.getUserdetails());
		populate.setSalesUserId(userDao.findById(order.getSalesUserId()).get());
		populate.setEditReason(order.getEditReason());
		populate.setCreatedAt(order.getCreatedAt());
		populate.setUpdatedAt(order.getUpdatedAt());
		populate.setCustomer(customerDao.findById(order.getCustomerId()).get());
		populate.setSalesDestinarion(customerDao.findById(order.getSalesDestination()).get());
		populate.setContractor(customerDao.findById(order.getContractorId()).get());
		populate.setOrderedProducts(getAllProducts(orderId));
		return populate;
	}


	private List<FetchOrderdProducts> getAllProducts(int orderId) {
		List<FetchOrderdProducts> orderProductList = new ArrayList<>();
		List<Map<Object, Object>> orderList=orderProductDao.getByOrderId(orderId);
		for(int i=0;i<orderList.size();i++) {
			FetchProductSetDto products = new FetchProductSetDto();
			FetchOrderdProducts order =new FetchOrderdProducts();
			products =(productServiceImpl.getProductSetById((Integer)orderList.get(i).get("product_id")));
			order.setProduct(products);
			order.setQuantity((Integer)orderList.get(i).get("qty"));
			orderProductList.add(order);
			
			}
		
		return orderProductList;
		}
		
	

	public Order getupdateOrderById(int orderId, @Valid SaveOrderSetDto newOrderSet) {
		Order order = orderDao.findById(orderId).get();
		order.setContractorId(newOrderSet.getContractorId());
		order.setSalesDestination(newOrderSet.getSalesDestination());
		order.setDueDate(newOrderSet.getDueDate());
		order.setCustomerId(newOrderSet.getCustomerId());
		order.setProposalNo(newOrderSet.getProposalNo());
		order.setReceivedDate(newOrderSet.getReceivedDate());
		order.setSalesDestination(newOrderSet.getSalesDestination());
		order.setUpdatedAt(LocalDateTime.now());
		order.setActive(newOrderSet.isActive());
		order.setForecast(newOrderSet.isForecast());
		order.setUserId(jwt.getUserdetails().getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		order.setContractorId(newOrderSet.getContractorId());
		Order orderupdate=orderDao.save(order);
		int id  = order.getOrderId();
		orderProductDao.deleteByOrderId(id);
		int noOfProducts =newOrderSet.getProductset().size();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(id);
			orderProduct.setProductId(newOrderSet.getProductset().get(i).getProductcomponentId());
			orderProduct.setQuantity(newOrderSet.getProductset().get(i).getQty());
			orderProductDao.save(orderProduct);

		}
		return orderupdate;
	}

	public Order deleteOrderById(int orderId) {
		Order order=orderDao.findById(orderId).get();
		order.setActive(false);
		return orderDao.save(order);
	}

	public Order save(SaveOrderSetDto newOrderSet) {
		Order order = new Order();
		order.setContractorId(newOrderSet.getContractorId());
		order.setSalesDestination(newOrderSet.getSalesDestination());
		order.setDueDate(newOrderSet.getDueDate());
		order.setCustomerId(newOrderSet.getCustomerId());
		order.setProposalNo(newOrderSet.getProposalNo());
		order.setReceivedDate(newOrderSet.getReceivedDate());
		order.setUpdatedAt(LocalDateTime.now());
		order.setCreatedAt(LocalDateTime.now());
		order.setActive(true);
		order.setForecast(true);
		order.setUserId(jwt.getUserdetails().getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		order.setContractorId(newOrderSet.getContractorId());
		Order ordersave=orderDao.save(order);
		int id  = order.getOrderId();
		int noOfProducts =newOrderSet.getProductset().size();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(id);
			orderProduct.setProductId(newOrderSet.getProductset().get(i).getProductcomponentId());
			orderProduct.setQuantity(newOrderSet.getProductset().get(i).getQty());
			orderProductDao.save(orderProduct);

		}
		return ordersave;
	} 
	
	
//	public FetchOrderdProducts getOrderedProducts(int productId, Integer amount) {
//	Product proCheck = productServiceImpl.getProductsById(productId).get();
//			List<ProductSetModel> productList = new ArrayList<>();
//			OrderProductModel componentSet= new OrderProductModel();
//			FetchOrderdProducts orderproduct =new FetchOrderdProducts();
//				orderproduct.setProductId(proCheck.getProductId());
//				orderproduct.setProductName(proCheck.getProductName());
//				orderproduct.setDescription(proCheck.getDescription());
//				orderproduct.setPrice(proCheck.getPrice());
//				orderproduct.setMoq(proCheck.getMoq());
//				orderproduct.setLeadTime(proCheck.getLeadTime());
//				orderproduct.setObicNo(proCheck.getObicNo());
//				orderproduct.setQuantity(proCheck.getQuantity());
//				orderproduct.setSet(proCheck.isSet());
//				orderproduct.setActive(proCheck.isActive());
//				orderproduct.setCreatedAtDateTime(proCheck.getCreatedAtDateTime());
//				orderproduct.setUpdatedAtDateTime(proCheck.getUpdatedAtDateTime());
//				if(proCheck.isSet()) {
//			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proCheck.getProductId());
//			for(int l=0;l< productsetList.size();l++ ) {
//			ProductSetModel productSetModel = new ProductSetModel();
//			Product productComponents = new Product();
//			productComponents=productDao.findById((Integer) productsetList.get(l).get("product_component_id")).get();
//			productSetModel.setProduct(productComponents);
//			productSetModel.setQuantity((Integer)productsetList.get(l).get("qty"));
//			productList.add(productSetModel);
//			}
//			orderproduct.setProduct(productList);
//			orderproduct.setQuantity(amount);
//			componentSet.setProduct(orderproduct);
//			
//		}
//			return componentSet;
//		 
//	}

	


}

