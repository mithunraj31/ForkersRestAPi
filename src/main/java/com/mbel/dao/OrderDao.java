package com.mbel.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {

	
	@Query(nativeQuery = true,value = "SELECT *  FROM  `order` WHERE active =1 \n" + 
			"  AND (delivery_date >= ?1 AND delivery_date <= ?2)")
	List<Order> getActiveOrdersBetweenDeliveryDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = " SELECT *  FROM  `order` WHERE active =1 AND display=1\n" + 
			"  AND (delivery_date >= ?1 AND delivery_date <= ?2)")
	List<Order> getActiveDisplayedOrdersBetweenDeliveryDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = "SELECT *  FROM  `order` WHERE active =1 AND fixed=1\n" + 
			"  AND (due_date >= ?1 AND due_date <= ?2)")
	List<Order> getActiveFixedOrdersBetweenDueDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = "SELECT *  FROM  `order` WHERE fulfilled =1 \n" + 
			"  AND (due_date >= ?1 AND due_date <= ?2)")
	List<Order> getFulfilledOrdersBetweenDueDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = "SELECT *  FROM  `order` WHERE fulfilled =0 \n" + 
			" AND active=1 AND fixed=1 AND delivery_date<?1")
	List<Order> getActiveFixedUnfulfilledOrdersAfterDeliveryDate(String today);
}

