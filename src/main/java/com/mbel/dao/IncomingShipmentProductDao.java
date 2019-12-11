package com.mbel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mbel.model.IncomingShipmentProduct;

@Repository
public interface IncomingShipmentProductDao extends JpaRepository<IncomingShipmentProduct, Integer> {
	
	@Query(value="SELECT * FROM `incoming_shipment_product` WHERE  incoming_shipment_id = ?1"  , nativeQuery = true)
	public List<Map<Object, Object>> getByShipmentId(int shipmentId);

	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM `incoming_shipment_product` WHERE  incoming_shipment_id = ?1" , nativeQuery = true)
	public void deleteByShipmentId(int shipmentId);
	
}
