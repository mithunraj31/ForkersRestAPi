package com.mbel.dao;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mbel.model.IncomingShipment;
import com.mbel.model.ProductSet;

@Repository
public interface IncomingShipmentDao extends JpaRepository<IncomingShipment, Integer> {
	
	
	
	@Query(nativeQuery = true,value = "SELECT *  FROM  incoming_shipment WHERE arrived=1  AND product_id=?1")
	List<IncomingShipment> getIncomingOrdersArrivedOrdersOfProduct(@Valid int productId);

	@Query(nativeQuery = true,value = "SELECT *  FROM  incoming_shipment WHERE arrived=1")
	List<IncomingShipment> getIncomingArrivedOrders();

	
	@Query("FROM IncomingShipment o WHERE o.productId IN :ids AND arrived=1")
	public List<IncomingShipment> getIncomingArrivedOrdersByProductIds(@Param("ids") List<Integer> ids);
	
}
