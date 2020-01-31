package com.mbel.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dto.FetchIncomingOrderdProducts;
import com.mbel.dto.PopulateIncomingShipmentDto;
import com.mbel.model.Product;

@Service("IncomingShipmentQtyUpdateServiceImpl")
public class IncomingShipmentQtyUpdateServiceImpl {
		
		
		@Autowired
		IncomingShipmentServiceImpl incomingShipmentServiceImpl;
		
		@Autowired
		ProductServiceImpl productServiceImpl;
		
		public void updateQuantity(int shipmentId) {
			PopulateIncomingShipmentDto incomingShipment = incomingShipmentServiceImpl.getIncomingShipmentById(shipmentId);
			for(FetchIncomingOrderdProducts incomingProduct: incomingShipment.getProducts()) {
			Product product =productServiceImpl.getProductsById(incomingProduct.getProduct().getProductId()).get();
			product.setQuantity(product.getQuantity()+incomingProduct.getQuantity());
			product.setPrice(incomingProduct.getPrice());
			productServiceImpl.getupdateById(incomingProduct.getProduct().getProductId(), product);
			
			}
		
	}

}
