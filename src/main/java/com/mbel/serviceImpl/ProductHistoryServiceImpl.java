package com.mbel.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.IncomingShipmentDao;
import com.mbel.dao.OrderDao;
import com.mbel.dao.OrderProductDao;
import com.mbel.dao.ProductDao;
import com.mbel.dto.ProductSummaryDto;
import com.mbel.model.IncomingShipment;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;
import com.mbel.model.Product;

@Service("ProductHistoryServiceImpl")
public class ProductHistoryServiceImpl {

	@Autowired
	OrderDao orderDao;

	@Autowired
	OrderProductDao orderProductDao;

	@Autowired 
	ProductDao productDao;

	@Autowired 
	IncomingShipmentDao incomingShipmentDao;


	public List<Product> getProductHistory(@Valid int year, @Valid int month, @Valid int dayOfMonth) {
		LocalDateTime tillDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
		LocalDateTime requiredHistoryDate=LocalDateTime.of(year, month, dayOfMonth, 0, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		List<Product>productList =productDao.findAll().stream().filter(predicate->!predicate.isSet()&&predicate.isActive()).collect(Collectors.toList());
		List<Order>order =orderDao.getOrdersAfterDate(requiredHistoryDate.format(formatter),tillDate.format(formatter)); 
		List<Integer>orderIdList=order.stream().map(mapper->mapper.getOrderId()).collect(Collectors.toList());
		List<OrderProduct>orderProductList=order.isEmpty()?null:orderProductDao.findAllByOrderId(orderIdList);
		List<IncomingShipment> allIncomingShipment = incomingShipmentDao.getIncomingOrdersAfterDate(); 
		List<IncomingShipment> incomingShipment=incomingShipmentListBetweenDates(allIncomingShipment,requiredHistoryDate,tillDate);

		return calculateCurrentQuantityInProduct(productList,orderProductList,incomingShipment);

	}


	private List<IncomingShipment> incomingShipmentListBetweenDates(List<IncomingShipment> incomingShipmentDtoList, LocalDateTime requiredHistoryDate,
			LocalDateTime tillDate) {
		List<IncomingShipment>incomingShipmentsList=new ArrayList<>();
		for(int i=0;i<incomingShipmentDtoList.size();i++) {
			if(incomingShipmentDtoList.get(i).isFixed()) {
				if(incomingShipmentDtoList.get(i).getFixedDeliveryDate().isAfter(requiredHistoryDate)&&
						incomingShipmentDtoList.get(i).getFixedDeliveryDate().isBefore(tillDate)) {
					incomingShipmentsList.add(incomingShipmentDtoList.get(i));

				}

			}else if(!incomingShipmentDtoList.get(i).isFixed()) {
				if(incomingShipmentDtoList.get(i).getDesiredDeliveryDate().isAfter(requiredHistoryDate)&&
						incomingShipmentDtoList.get(i).getDesiredDeliveryDate().isBefore(tillDate)) {
					incomingShipmentsList.add(incomingShipmentDtoList.get(i));

				}

			}

		}
		return incomingShipmentsList;
	}


	private List<Product> calculateCurrentQuantityInProduct(List<Product> productList,
			List<OrderProduct> orderProductList, List<IncomingShipment> incomingShipmentList) {
		int quantityFulfilledAfterRequestedDate=0;
		for(Product product:productList) {
			if(Objects.nonNull(orderProductList)) {
				List<OrderProduct>individualOrderProductsList=orderProductList.stream()
						.filter(predicate->predicate.getProductId()==product.getProductId()).collect(Collectors.toList());
				if(!individualOrderProductsList.isEmpty()) {
					for(OrderProduct orderProduct:individualOrderProductsList) {
						quantityFulfilledAfterRequestedDate+=orderProduct.getQuantity();
					}
					product.setQuantity(product.getQuantity()+quantityFulfilledAfterRequestedDate);
				}
			}

			updateArrivedShipmentsToCurrentQuantity(product,incomingShipmentList);

		}

		return productList;

	}


	private void updateArrivedShipmentsToCurrentQuantity(Product product, List<IncomingShipment> incomingShipmentList) {

		int quantityArrivedAfterRequestedDate=0;
		if(!incomingShipmentList.isEmpty()) {
			List<IncomingShipment>individualIncomingShipmentList=incomingShipmentList.stream()
					.filter(predicate->predicate.getProductId()==product.getProductId()).collect(Collectors.toList());
			if(!individualIncomingShipmentList.isEmpty()) {
				for(IncomingShipment incomingShipment:individualIncomingShipmentList) {
					if(incomingShipment.isFixed()) {
						quantityArrivedAfterRequestedDate+=incomingShipment.getConfirmedQty();

					}else  {
						quantityArrivedAfterRequestedDate+=incomingShipment.getPendingQty();
					}
					product.setQuantity(product.getQuantity()-quantityArrivedAfterRequestedDate);
				}
			}
		}
	}


	public ProductSummaryDto getProductSummaryByProductId(@Valid int year, @Valid int month, @Valid int productId) {
		LocalDate initial = LocalDate.of(year, month, 1);
		LocalDateTime requiredSummaryDate=LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime tillDate = LocalDateTime.of(year, month, initial.lengthOfMonth(), 0, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		Product product =productDao.findById(productId).orElse(null);
		if(Objects.nonNull(product)) {
			List<Order>order =orderDao.getOrdersAfterDate(requiredSummaryDate.format(formatter),tillDate.format(formatter)); 
			List<Integer>orderIdList=order.stream().map(mapper->mapper.getOrderId()).collect(Collectors.toList());
			List<OrderProduct>orderProductList=order.isEmpty()?null:orderProductDao.findAllByOrderId(orderIdList);
			List<IncomingShipment> allIncomingShipment = incomingShipmentDao.getIncomingOrdersAfterDate(); 
			List<IncomingShipment> incomingShipment=incomingShipmentListBetweenDates(allIncomingShipment,requiredSummaryDate,tillDate);

			return calculateQuantitySummaryInProduct(product,orderProductList,incomingShipment);
		}
		return null;

	}


	private ProductSummaryDto calculateQuantitySummaryInProduct(Product product,
			List<OrderProduct> orderProductList, List<IncomingShipment> incomingShipmentList) {

		ProductSummaryDto productSummaryDto= new ProductSummaryDto();
		int totalOutgoingQty=0;

		productSummaryDto.setProductId(product.getProductId());
		productSummaryDto.setProductName(product.getProductName());
		productSummaryDto.setDescription(product.getDescription());
		productSummaryDto.setObicNo(product.getObicNo());
		productSummaryDto.setColor(product.getColor());
		productSummaryDto.setCurrentQty(product.getQuantity());
		if(Objects.nonNull(orderProductList)) {
			List<OrderProduct>individualOrderProductsList=orderProductList.stream()
					.filter(predicate->predicate.getProductId()==product.getProductId()).collect(Collectors.toList());
			if(!individualOrderProductsList.isEmpty()) {
				for(OrderProduct orderProduct:individualOrderProductsList) {
					totalOutgoingQty+=orderProduct.getQuantity();
				}
				productSummaryDto.setTotalOutgoingQty(totalOutgoingQty);
			}
		}

		updateArrivedShipmentsTotalQuantity(productSummaryDto,product,incomingShipmentList);


		return productSummaryDto;

	}

	private void updateArrivedShipmentsTotalQuantity(ProductSummaryDto productSummaryDto, Product product, List<IncomingShipment> incomingShipmentList) {

		int totalIncomingQty=0;
		if(!incomingShipmentList.isEmpty()) {
			List<IncomingShipment>individualIncomingShipmentList=incomingShipmentList.stream()
					.filter(predicate->predicate.getProductId()==product.getProductId()).collect(Collectors.toList());
			if(!individualIncomingShipmentList.isEmpty()) {
				for(IncomingShipment incomingShipment:individualIncomingShipmentList) {
					if(incomingShipment.isFixed()) {
						totalIncomingQty+=incomingShipment.getConfirmedQty();

					}else  {
						totalIncomingQty+=incomingShipment.getPendingQty();
					}
					productSummaryDto.setTotalIncomingQty(totalIncomingQty);
				}

			}
		}
	}


}
