package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.mbel.dto.PopulateOrderDto;
import com.mbel.dto.SaveOrderSetDto;
import com.mbel.model.Customer;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;
import com.mbel.model.UserEntity;




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
	ProductPredictionServiceImpl productPredictionServiceImpl;
	
	@Autowired 
	ProductDao productDao;

	@Autowired 
	ProductSetDao productSetDao;
	
	@Autowired 
	UserDao userDao;
	
	@Autowired
	 JwtAuthenticationFilter jwt;
	

	public List<Order> getActiveUnfulfilledOrders() {
		List<Order>order =orderDao.findAll(); 
		return order.stream()
				.filter(predicate->predicate.isActive()&&!predicate.isFulfilled())
				.collect(Collectors.toList());
	}

	public List<PopulateOrderDto> getAllOrders() {
		List<Order>activeOrder =getActiveUnfulfilledOrders();
		List<UserEntity> userList = userDao.findAll();
		List<Customer> customerList = customerDao.findAll();
		List<Product> allProduct = productDao.findAll();
		List<ProductSet> allProductSet =productSetDao.findAll();
		List<OrderProduct>orderProduct =orderProductDao.findAll(); 
		List<PopulateOrderDto>populateList =new ArrayList<>();
		for(Order order:activeOrder) {
			PopulateOrderDto populate = new PopulateOrderDto();
			populate.setOrderId(order.getOrderId());
			populate.setProposalNo(order.getProposalNo());
			populate.setReceivedDate(order.getReceivedDate());
			populate.setDueDate(order.getDueDate());
			populate.setDeliveryDate(order.getDeliveryDate());
			populate.setActive(order.isActive());
			populate.setForecast(order.isForecast());
			populate.setFulfilled(order.isFulfilled());
			populate.setFixed(order.isFixed());
			populate.setUser(getUser(userList,order.getUserId()));
			populate.setSalesUser(getUser(userList,order.getSalesUserId()));
			populate.setEditReason(order.getEditReason());
			populate.setCreatedAt(order.getCreatedAt());
			populate.setUpdatedAt(order.getUpdatedAt());
			populate.setCustomer(getCustomer(customerList,order.getCustomerId()));
			populate.setSalesDestination(getCustomer(customerList,order.getSalesDestinationId()));
			populate.setContractor(getCustomer(customerList,order.getContractorId()));
			populate.setDisplay(order.isDisplay());
			populate.setOrderedProducts(productPredictionServiceImpl.getAllProducts(order,orderProduct,allProduct,allProductSet));
			populateList.add(populate);
		}

		return populateList;

	}

	public PopulateOrderDto getOrderById(int orderId) {
		PopulateOrderDto populate = new PopulateOrderDto();
		Order order = orderDao.findById(orderId).orElse(null);
		List<UserEntity> userList = userDao.findAll();
		List<Customer> customerList = customerDao.findAll();
		List<Product> allProduct = productDao.findAll();
		List<ProductSet> allProductSet =productSetDao.findAll();
		List<OrderProduct>orderProduct =orderProductDao.findAll(); 
		if(Objects.nonNull(order)) {
		populate.setOrderId(order.getOrderId());
		populate.setProposalNo(order.getProposalNo());
		populate.setReceivedDate(order.getReceivedDate());
		populate.setDueDate(order.getDueDate());
		populate.setDeliveryDate(order.getDeliveryDate());
		populate.setActive(order.isActive());
		populate.setForecast(order.isForecast());
		populate.setFulfilled(order.isFulfilled());
		populate.setFixed(order.isFixed());
		populate.setDisplay(order.isDisplay());
		populate.setUser(getUser(userList,order.getUserId()));
		populate.setSalesUser(getUser(userList,order.getSalesUserId()));
		populate.setEditReason(order.getEditReason());
		populate.setCreatedAt(order.getCreatedAt());
		populate.setUpdatedAt(order.getUpdatedAt());
		populate.setCustomer(getCustomer(customerList,order.getCustomerId()));
		populate.setSalesDestination(getCustomer(customerList,order.getSalesDestinationId()));
		populate.setContractor(getCustomer(customerList,order.getContractorId()));
		populate.setOrderedProducts(productPredictionServiceImpl.getAllProducts(order,orderProduct,allProduct,allProductSet));
		}
		return populate;
	}
	
	private UserEntity getUser(List<UserEntity> userList, int userId) {
		return userList.stream()
		.filter(predicate->predicate.getUserId()==userId)
		.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
            if (list.size() != 1) {
                return null;
            }
            return list.get(0);
        }));
	}
	
	private Customer getCustomer(List<Customer> customerList, int customerId) {
		return customerList.stream()
		.filter(predicate->predicate.getCustomerId()==customerId)
		.collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
            if (list.size() != 1) {
            	return null;
            }
            return list.get(0);
        }));
	}


		
	

	public Order getupdateOrderById(int orderId, @Valid SaveOrderSetDto newOrderSet) {
		int orderedId =0;
		Order orderupdate= new Order();
		Order order = orderDao.findById(orderId).orElse(null);
		if(Objects.nonNull(order)) {
		order.setContractorId(newOrderSet.getContractorId());
		order.setSalesDestinationId(newOrderSet.getSalesDestinationId());
		order.setDueDate(newOrderSet.getDueDate());
		order.setDeliveryDate(newOrderSet.getDeliveryDate());
		order.setCustomerId(newOrderSet.getCustomerId());
		order.setProposalNo(newOrderSet.getProposalNo());
		order.setReceivedDate(newOrderSet.getReceivedDate());
		order.setUpdatedAt(LocalDateTime.now());
		order.setActive(true);
		order.setForecast(newOrderSet.isForecast());
		order.setFixed(newOrderSet.isFixed());
		order.setFulfilled(newOrderSet.isFulfilled());
		order.setUserId(jwt.getUserdetails().getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		orderupdate=orderDao.save(order);
		orderedId  = order.getOrderId();
		}
		orderProductDao.deleteByOrderId(orderedId);
		int noOfProducts =newOrderSet.getOrderedProducts().size();
		List<OrderProduct> orderProductList =new ArrayList<>();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(orderedId);
			orderProduct.setProductId(newOrderSet.getOrderedProducts().get(i).getProductId());
			orderProduct.setQuantity(newOrderSet.getOrderedProducts().get(i).getQuantity());
			orderProductList.add(orderProduct);
		}
		orderProductDao.saveAll(orderProductList);
		return orderupdate;
	}

	public Order deleteOrderById(int orderId) {
		Order order=orderDao.findById(orderId).orElse(null);
		if(Objects.nonNull(order)) {
		order.setActive(false);
		return orderDao.save(order);
		}
		return order;
	}

	public Order save(SaveOrderSetDto newOrderSet) {
		Order order = new Order();
		order.setContractorId(newOrderSet.getContractorId());
		order.setSalesDestinationId(newOrderSet.getSalesDestinationId());
		order.setDueDate(newOrderSet.getDueDate());
		order.setCustomerId(newOrderSet.getCustomerId());
		order.setProposalNo(newOrderSet.getProposalNo());
		order.setReceivedDate(newOrderSet.getReceivedDate());
		order.setDeliveryDate(newOrderSet.getDeliveryDate());
		order.setUpdatedAt(LocalDateTime.now());
		order.setCreatedAt(LocalDateTime.now());
		order.setActive(true);
		order.setForecast(true);
		order.setFulfilled(false);
		order.setDisplay(true);
		order.setFixed(newOrderSet.isFixed());
		order.setUserId(jwt.getUserdetails().getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		Order ordersave=orderDao.save(order);
		int id  = order.getOrderId();
		int noOfProducts =newOrderSet.getOrderedProducts().size();
		List<OrderProduct> orderProductList = new ArrayList<>();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(id);
			orderProduct.setProductId(newOrderSet.getOrderedProducts().get(i).getProductId());
			orderProduct.setQuantity(newOrderSet.getOrderedProducts().get(i).getQuantity());
			orderProductList.add(orderProduct);
		}
		orderProductDao.saveAll(orderProductList);
		return ordersave;
	} 
	
	public double estimation(SaveOrderSetDto newOrderSet){
		double estimationValue=0;
		int noOfProducts =newOrderSet.getOrderedProducts().size();
		for(int i=0;i<noOfProducts;i++) {
			Product products;
		int productId=	newOrderSet.getOrderedProducts().get(i).getProductId();
		products =productServiceImpl.getProductSetById(productId);
		estimationValue+=products.getPrice();
		}
		return estimationValue;
		
	}

	public List<PopulateOrderDto> getAllFulfilledOrders() {
		List<Order>activeOrder =getInActivefulfilledOrders();
		List<UserEntity> userList = userDao.findAll();
		List<Customer> customerList = customerDao.findAll();
		List<Product> allProduct = productDao.findAll();
		List<ProductSet> allProductSet =productSetDao.findAll();
		List<OrderProduct>orderProduct =orderProductDao.findAll(); 
		List<PopulateOrderDto>populateList =new ArrayList<>();
		for(Order order:activeOrder) {
			PopulateOrderDto populate = new PopulateOrderDto();
			populate.setOrderId(order.getOrderId());
			populate.setProposalNo(order.getProposalNo());
			populate.setReceivedDate(order.getReceivedDate());
			populate.setDueDate(order.getDueDate());
			populate.setDeliveryDate(order.getDeliveryDate());
			populate.setActive(order.isActive());
			populate.setForecast(order.isForecast());
			populate.setFulfilled(order.isFulfilled());
			populate.setFixed(order.isFixed());
			populate.setDisplay(order.isDisplay());
			populate.setUser(getUser(userList,order.getUserId()));
			populate.setSalesUser(getUser(userList,order.getSalesUserId()));
			populate.setEditReason(order.getEditReason());
			populate.setCreatedAt(order.getCreatedAt());
			populate.setUpdatedAt(order.getUpdatedAt());
			populate.setCustomer(getCustomer(customerList,order.getCustomerId()));
			populate.setSalesDestination(getCustomer(customerList,order.getSalesDestinationId()));
			populate.setContractor(getCustomer(customerList,order.getContractorId()));
			populate.setOrderedProducts(productPredictionServiceImpl.getAllProducts(order,orderProduct,allProduct,allProductSet));
			populateList.add(populate);
		}

		return populateList;

	}

	private List<Order> getInActivefulfilledOrders() {
		List<Order>order =orderDao.findAll(); 
		return order.stream()
				.filter(predicate->!predicate.isActive()||predicate.isFulfilled())
				.collect(Collectors.toList());
	
	}

	public Order orderDisplay(int orderId,boolean display) {
		Order order = orderDao.findById(orderId).orElse(null);
		if(Objects.nonNull(order)) {
			order.setDisplay(display);
			orderDao.save(order);
		}
		return order;
	}
	
	
}

