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
			populate.setUser(jwt.getUserdetails());
			populate.setSalesUser(userDao.findById(order.getSalesUserId()).get());
			populate.setEditReason(order.getEditReason());
			populate.setCreatedAt(order.getCreatedAt());
			populate.setUpdatedAt(order.getUpdatedAt());
			populate.setCustomer(customerDao.findById(order.getCustomerId()).get());
			populate.setSalesDestination(customerDao.findById(order.getSalesDestinationId()).get());
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
		populate.setUser(jwt.getUserdetails());
		populate.setSalesUser(userDao.findById(order.getSalesUserId()).get());
		populate.setEditReason(order.getEditReason());
		populate.setCreatedAt(order.getCreatedAt());
		populate.setUpdatedAt(order.getUpdatedAt());
		populate.setCustomer(customerDao.findById(order.getCustomerId()).get());
		populate.setSalesDestination(customerDao.findById(order.getSalesDestinationId()).get());
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
		order.setSalesDestinationId(newOrderSet.getSalesDestinationId());
		order.setDueDate(newOrderSet.getDueDate());
		order.setCustomerId(newOrderSet.getCustomerId());
		order.setProposalNo(newOrderSet.getProposalNo());
		order.setReceivedDate(newOrderSet.getReceivedDate());
		order.setSalesDestinationId(newOrderSet.getSalesDestinationId());
		order.setUpdatedAt(LocalDateTime.now());
		order.setActive(true);
		order.setForecast(newOrderSet.isForecast());
		order.setUserId(jwt.getUserdetails().getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		order.setContractorId(newOrderSet.getContractorId());
		Order orderupdate=orderDao.save(order);
		int id  = order.getOrderId();
		orderProductDao.deleteByOrderId(id);
		int noOfProducts =newOrderSet.getOrderedProducts().size();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(id);
			orderProduct.setProductId(newOrderSet.getOrderedProducts().get(i).getProductId());
			orderProduct.setQuantity(newOrderSet.getOrderedProducts().get(i).getQuantity());
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
		order.setSalesDestinationId(newOrderSet.getSalesDestinationId());
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
		int noOfProducts =newOrderSet.getOrderedProducts().size();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(id);
			orderProduct.setProductId(newOrderSet.getOrderedProducts().get(i).getProductId());
			orderProduct.setQuantity(newOrderSet.getOrderedProducts().get(i).getQuantity());
			orderProductDao.save(orderProduct);

		}
		return ordersave;
	} 
	
	
}

