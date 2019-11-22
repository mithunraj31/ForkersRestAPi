package com.mbel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
	
	@Query(value="SELECT * FROM `Customer` WHERE  customer_id = ?1"  , nativeQuery = true)
	public List<Map<Object, Object>> getCustomer(int customerId, int destinarionId, int contractId);
}
