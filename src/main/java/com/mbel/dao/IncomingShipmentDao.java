package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.IncomingShipment;

@Repository
public interface IncomingShipmentDao extends JpaRepository<IncomingShipment, Integer> {
	
}
