package com.mbel.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.mbel.dao.ProductSetDao;
import com.mbel.dto.ProductSetSummaryDto;
import com.mbel.dto.ProductSummaryDto;
import com.mbel.model.IncomingShipment;
import com.mbel.model.Order;
import com.mbel.model.OrderProduct;
import com.mbel.model.Product;
import com.mbel.model.ProductSet;

@Service("ProductSetHistoryServiceImpl")
public class ProductSetHistoryServiceImpl {

	@Autowired
	OrderDao orderDao;

	@Autowired
	OrderProductDao orderProductDao;

	@Autowired 
	ProductDao productDao;

	@Autowired 
	IncomingShipmentDao incomingShipmentDao;

	@Autowired 
	ProductServiceImpl productServiceImpl;

	@Autowired 
	ProductSetDao productSetDao;

	@Autowired 
	ProductHistoryServiceImpl productHistoryServiceImpl;



	public ProductSetSummaryDto getProductSummaryByProductId(@Valid int year, @Valid int month, @Valid int productId) {
		LocalDate initial = LocalDate.of(year, month, 1);
		LocalDateTime requiredSummaryDate=LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime tillDate = LocalDateTime.of(year, month, initial.lengthOfMonth(), 0, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime tillUtcDate =DateTimeUtil.toUtc(tillDate);
		LocalDateTime requiredSummaryUtcDate=DateTimeUtil.toUtc(requiredSummaryDate);
		List<Product> allProduct =productDao.getActiveProducts();

		Product product =getProductById(allProduct,productId);
		if(Objects.nonNull(product)) {
			List<ProductSet> allProductSetList=productSetDao.findAll();
			List<ProductSet> productSetList=getBySetId(allProductSetList,productId);
			List<Order>order =orderDao.getFulfilledOrdersBetweenDueDates(requiredSummaryUtcDate.format(formatter),tillUtcDate.format(formatter)); 
			List<Integer>orderIdList=order.stream().map(Order::getOrderId).collect(Collectors.toList());
			List<OrderProduct>orderProductList=order.isEmpty()?null:orderProductDao.findAllByOrderId(orderIdList);
			List<Integer>productIdList=orderProductList==null?null:productSetList.stream().map(ProductSet::getProductComponentId).collect(Collectors.toList());
			List<IncomingShipment> allIncomingShipment = incomingShipmentDao.getIncomingArrivedOrdersByProductIds(productIdList);
			List<IncomingShipment> incomingShipment=incomingShipmentListBetweenDates(allIncomingShipment,requiredSummaryDate,tillDate);
			return calculateQuantitySummaryInProductSet(product,orderProductList,incomingShipment,productSetList,allProduct,allProductSetList);
		}
		return null;

	}

	private List<ProductSet> getBySetId(List<ProductSet> allProductSetList, @Valid int productId) {
		return allProductSetList.stream().filter(predicate->predicate.getSetId()==productId).collect(Collectors.toList());
	}

	private Product getProductById(List<Product> allProduct, @Valid int productId) {
		return allProduct.stream().filter(predicate->predicate.getProductId()==productId).collect(Collectors.collectingAndThen(Collectors.toList(), list-> {
			if (list.size() != 1) {
				return null;
			}
			return list.get(0);
		}));
	}

	private ProductSetSummaryDto calculateQuantitySummaryInProductSet(Product product, List<OrderProduct> orderProductList,
			List<IncomingShipment> incomingShipment, List<ProductSet> productSetList, List<Product> allProduct, List<ProductSet> allProductSetList) {
		ProductSetSummaryDto productSetSummaryDto= new ProductSetSummaryDto();
		productSetSummaryDto.setProductId(product.getProductId());
		productSetSummaryDto.setProductName(product.getProductName());
		productSetSummaryDto.setDescription(product.getDescription());
		productSetSummaryDto.setObicNo(product.getObicNo());
		productSetSummaryDto.setColor(product.getColor());
		List<ProductSummaryDto> productSummaryDtoList =new ArrayList<>();
		for(ProductSet productSet:productSetList) {
			Product indProduct =getProductById(allProduct,productSet.getProductComponentId());
			ProductSummaryDto productSummaryDto=productHistoryServiceImpl.calculateQuantitySummaryInProduct(indProduct, 
					orderProductList, incomingShipment, allProductSetList, allProduct);
			productSummaryDtoList.add(productSummaryDto);
		}
		productSetSummaryDto.setProduct(productSummaryDtoList);
		return productSetSummaryDto;

	}

	private List<IncomingShipment> incomingShipmentListBetweenDates(List<IncomingShipment> incomingShipmentDtoList, LocalDateTime requiredHistoryDate,
			LocalDateTime tillDate) {
		List<IncomingShipment>incomingShipmentsList=new ArrayList<>();
		for(int i=0;i<incomingShipmentDtoList.size();i++) {
			if(incomingShipmentDtoList.get(i).isFixed()) {
				if((incomingShipmentDtoList.get(i).getFixedDeliveryDate().isAfter(requiredHistoryDate)
						||incomingShipmentDtoList.get(i).getFixedDeliveryDate().isEqual(requiredHistoryDate))&&
						(incomingShipmentDtoList.get(i).getFixedDeliveryDate().isBefore(tillDate)||
								incomingShipmentDtoList.get(i).getFixedDeliveryDate().isEqual(tillDate))) {
					incomingShipmentsList.add(incomingShipmentDtoList.get(i));

				}

			}else if(!incomingShipmentDtoList.get(i).isFixed()) {
				if((incomingShipmentDtoList.get(i).getDesiredDeliveryDate().isAfter(requiredHistoryDate)
						||incomingShipmentDtoList.get(i).getDesiredDeliveryDate().isEqual(requiredHistoryDate))&&
						(incomingShipmentDtoList.get(i).getDesiredDeliveryDate().isBefore(tillDate)||
								incomingShipmentDtoList.get(i).getDesiredDeliveryDate().isEqual(tillDate))) {
					incomingShipmentsList.add(incomingShipmentDtoList.get(i));

				}

			}

		}
		return incomingShipmentsList;
	}




}
