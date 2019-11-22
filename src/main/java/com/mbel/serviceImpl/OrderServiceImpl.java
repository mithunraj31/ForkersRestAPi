package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.CustomerDao;
import com.mbel.dao.OrderDao;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.dto.SaveOrderSetDto;
import com.mbel.model.Customer;
import com.mbel.model.Order;


@Service("OrderServiceImpl")
public class OrderServiceImpl  {
	
	@Autowired
	 OrderDao orderDao;
	
	@Autowired
	 CustomerDao customerDao;

	public Order save(Order newOrder) {
		newOrder.setCreatedAt(LocalDateTime.now());
		newOrder.setUpdatedAt(LocalDateTime.now());
		newOrder.setActive(true);
		newOrder.setForecast(true);
		return orderDao.save(newOrder);
	}

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
		 populate.setUserId(order.getUserId());
		 populate.setSalesDestination(order.getSalesDestination());
		 populate.setSalesRepresentative(order.getSalesRepresentative());
		 populate.setSalesUserId(order.getSalesUserId());
		 populate.setEditReason(order.getEditReason());
		 populate.setContractorId(order.getContractorId());
		 populate.setCreatedAt(order.getCreatedAt());
		 populate.setUpdatedAt(order.getUpdatedAt());
		 populate.setCustomerId(order.getCustomerId());
		 populate.setCustomers(getCustomerList(order.getCustomerId(),order.getSalesDestination(),order.getContractorId()));
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
		 populate.setUserId(order.getUserId());
		 populate.setSalesDestination(order.getSalesDestination());
		 populate.setSalesRepresentative(order.getSalesRepresentative());
		 populate.setSalesUserId(order.getSalesUserId());
		 populate.setEditReason(order.getEditReason());
		 populate.setContractorId(order.getContractorId());
		 populate.setCreatedAt(order.getCreatedAt());
		 populate.setUpdatedAt(order.getUpdatedAt());
		 populate.setCustomerId(order.getCustomerId());
		 populate.setCustomers(getCustomerList(order.getCustomerId(),order.getSalesDestination(),order.getContractorId()));
		 return populate;
	}

	private List<Customer> getCustomerList(int customerId, int destinarionId, int contractId) {
		List<Customer> customerList = new ArrayList<>();
		customerList.add(customerDao.findById(customerId).get());
		customerList.add(customerDao.findById(destinarionId).get());
		customerList.add(customerDao.findById(contractId).get());
		return customerList;
		
	}

	public Order getupdateOrderById(int orderId, @Valid Order orderDetails) {
		Order order = orderDao.findById(orderId).get();
		order.setContractorId(orderDetails.getContractorId());
		order.setSalesDestination(orderDetails.getSalesDestination());
		order.setDueDate(orderDetails.getDueDate());
		order.setCustomerId(orderDetails.getCustomerId());
		order.setProposalNo(orderDetails.getProposalNo());
		order.setReceivedDate(orderDetails.getReceivedDate());
		order.setSalesDestination(orderDetails.getSalesDestination());
		order.setSalesRepresentative(orderDetails.getSalesRepresentative());
		order.setUpdatedAt(LocalDateTime.now());
		order.setActive(orderDetails.isActive());
		order.setForecast(orderDetails.isForecast());
		order.setUserId(orderDetails.getUserId());
		order.setSalesUserId(orderDetails.getSalesUserId());
		order.setEditReason(orderDetails.getEditReason());
		order.setContractorId(orderDetails.getContractorId());
		return orderDao.save(order);
	}

	public Order deleteOrderById(int orderId) {
		 Order order=orderDao.findById(orderId).get();
		 order.setActive(false);
		 return orderDao.save(order);
	}

	public Order saveOrderSet(SaveOrderSetDto newOrderSet) {
		Order order = new Order();
		order.setContractorId(newOrderSet.getContractorId());
		order.setSalesDestination(newOrderSet.getSalesDestination());
		order.setDueDate(newOrderSet.getDueDate());
		order.setCustomerId(newOrderSet.getCustomerId());
		order.setProposalNo(newOrderSet.getProposalNo());
		order.setReceivedDate(newOrderSet.getReceivedDate());
		order.setSalesDestination(newOrderSet.getSalesDestination());
		order.setSalesRepresentative(newOrderSet.getSalesRepresentative());
		order.setUpdatedAt(LocalDateTime.now());
		order.setActive(newOrderSet.isActive());
		order.setForecast(newOrderSet.isForecast());
		order.setUserId(newOrderSet.getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		order.setContractorId(newOrderSet.getContractorId());
		 orderDao.save(order);
		return null;
	} 
	
	

}

