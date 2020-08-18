package com.mbel.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.IncomingShipment;

@Repository
public interface IncomingShipmentDao extends JpaRepository<IncomingShipment, Integer> {
	
	
	@Query(nativeQuery = true,value = "call GET_ARRIVED_INCOMING_ORDERS_AFTER_DATE()")
	List<IncomingShipment> getIncomingOrdersAfterDate();
	
}
