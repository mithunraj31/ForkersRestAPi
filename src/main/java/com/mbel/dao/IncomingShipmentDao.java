package com.mbel.dao;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.IncomingShipment;

@Repository
public interface IncomingShipmentDao extends JpaRepository<IncomingShipment, Integer> {
	
	
	
	@Query(nativeQuery = true,value = "SELECT *  FROM  incoming_shipment WHERE arrived=1  AND product_id=?1")
	List<IncomingShipment> getIncomingOrdersArrivedOrdersOfProduct(@Valid int productId);

	@Query(nativeQuery = true,value = "SELECT *  FROM  incoming_shipment WHERE arrived=1")
	List<IncomingShipment> getIncomingArrivedOrders();
	
}
