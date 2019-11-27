package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.CustomerDao;
import com.mbel.dao.OrderDao;
import com.mbel.dao.OrderProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.FetchOrderdProducts;
import com.mbel.dto.PopulateOrderDto;
import com.mbel.dto.SaveOrderSetDto;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;
import com.mbel.model.OrderProductModel;
import com.mbel.model.Product;
import com.mbel.model.ProductSetModel;


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
		populate.setUserId(order.getUserId());
		populate.setSalesDestination(order.getSalesDestination());
		populate.setSalesRepresentative(order.getSalesRepresentative());
		populate.setSalesUserId(order.getSalesUserId());
		populate.setEditReason(order.getEditReason());
		populate.setContractorId(order.getContractorId());
		populate.setCreatedAt(order.getCreatedAt());
		populate.setUpdatedAt(order.getUpdatedAt());
		populate.setCustomer(customerDao.findById(order.getCustomerId()).get());
		populate.setSalesDestinarion(customerDao.findById(order.getSalesDestination()).get());
		populate.setContractor(customerDao.findById(order.getContractorId()).get());
		populate.setOrderedProducts(getAllProducts(orderId));
		return populate;
	}


	private List<FetchOrderdProducts> getAllProducts(int orderId) {
		List<FetchOrderdProducts>productsList = new ArrayList<>();
		List<Map<Object, Object>> orderList=orderProductDao.getByOrderId(orderId);
		for(int i=0;i<orderList.size();i++) {
			productsList.add(getOrderedProducts(((Integer)orderList.get(i).get("product_id")),(Integer)orderList.get(i).get("qty")));
			}
		return productsList;
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
		order.setSalesRepresentative(newOrderSet.getSalesRepresentative());
		order.setUpdatedAt(LocalDateTime.now());
		order.setActive(newOrderSet.isActive());
		order.setForecast(newOrderSet.isForecast());
		order.setUserId(newOrderSet.getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		order.setContractorId(newOrderSet.getContractorId());
		orderDao.save(order);
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
		return newOrderSet;
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
		order.setSalesDestination(newOrderSet.getSalesDestination());
		order.setSalesRepresentative(newOrderSet.getSalesRepresentative());
		order.setUpdatedAt(LocalDateTime.now());
		order.setCreatedAt(LocalDateTime.now());
		order.setActive(true);
		order.setForecast(true);
		order.setUserId(newOrderSet.getUserId());
		order.setSalesUserId(newOrderSet.getSalesUserId());
		order.setEditReason(newOrderSet.getEditReason());
		order.setContractorId(newOrderSet.getContractorId());
		orderDao.save(order);
		int id  = order.getOrderId();
		int noOfProducts =newOrderSet.getProductset().size();
		for(int i=0;i<noOfProducts;i++) {
			OrderProduct orderProduct =new OrderProduct(); 
			orderProduct.setOrderId(id);
			orderProduct.setProductId(newOrderSet.getProductset().get(i).getProductcomponentId());
			orderProduct.setQuantity(newOrderSet.getProductset().get(i).getQty());
			orderProductDao.save(orderProduct);

		}
		return newOrderSet;
	} 
	
	
	public FetchOrderdProducts getOrderedProducts(int productId, Integer amount) {
	Product proCheck = productServiceImpl.getProductsById(productId).get();
			List<ProductSetModel> productList = new ArrayList<>();
			FetchOrderdProducts componentSet= new FetchOrderdProducts();
			if(proCheck.isSet()) {
				OrderProductModel orderproduct =new OrderProductModel();
				orderproduct.setProductId(proCheck.getProductId());
				orderproduct.setProductName(proCheck.getProductName());
				orderproduct.setDescription(proCheck.getDescription());
				orderproduct.setPrice(proCheck.getPrice());
				orderproduct.setMoq(proCheck.getMoq());
				orderproduct.setLeadTime(proCheck.getLeadTime());
				orderproduct.setObicNo(proCheck.getObicNo());
				orderproduct.setQuantity(proCheck.getQuantity());
				orderproduct.setSet(proCheck.isSet());
				orderproduct.setActive(proCheck.isActive());
				orderproduct.setCreatedAtDateTime(proCheck.getCreatedAtDateTime());
				orderproduct.setUpdatedAtDateTime(proCheck.getUpdatedAtDateTime());
			List<Map<Object, Object>> productsetList =productSetDao.getAllBySetId(proCheck.getProductId());
			for(int l=0;l< productsetList.size();l++ ) {
			ProductSetModel productSetModel = new ProductSetModel();
			Product productComponents = new Product();
			productComponents=productDao.findById((Integer) productsetList.get(l).get("product_component_id")).get();
			productSetModel.setProducts(productComponents);
			productSetModel.setQuantity((Integer)productsetList.get(l).get("qty"));
			productList.add(productSetModel);
			}
			orderproduct.setProduct(productList);
			orderproduct.setAmount(amount);
			componentSet.setProducts(orderproduct);
			
		}else {
			ProductSetModel productSetModel = new ProductSetModel();
			productSetModel.setProducts(proCheck);
			productSetModel.setQuantity(amount);
			componentSet.setProduct(productSetModel);
			
		}
			return componentSet;
		 
	}

	


}

