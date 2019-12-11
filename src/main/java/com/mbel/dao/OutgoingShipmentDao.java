package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.OutgoingShipment;

@Repository
public interface OutgoingShipmentDao extends JpaRepository<OutgoingShipment, Integer> {
	
}
