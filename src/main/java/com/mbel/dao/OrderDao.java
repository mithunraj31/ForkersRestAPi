package com.mbel.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
	
	@Query(nativeQuery = true,value = "call GET_FULFILLED_ORDERS_AFTER_DATE(?1,?2)")
	List<Order> getOrdersAfterDate(String requiredHistoryDate, String tillDate);

	
	@Query(nativeQuery = true,value = "call GET_ACTIVE_ORDER_BETWEEN_DELIVERY_DATES(?1,?2)")
	List<Order> getActiveOrdersBetweenDeliveryDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = "call GET_DISPLAYED_ACTIVE_ORDER_BETWEEN_DELIVERY_DATES(?1,?2)")
	List<Order> getActiveDisplayedOrdersBetweenDeliveryDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = "call GET_ACTIVE_FIXED_ORDER_BETWEEN_DUE_DATES(?1,?2)")
	List<Order> getActiveFixedOrdersBetweenDueDates(String dueDateStart, String dueDateEnd);

	@Query(nativeQuery = true,value = "call GET_FULFILLED_ORDER_BETWEEN_DUE_DATES(?1,?2)")
	List<Order> getFulfilledOrdersBetweenDueDates(String dueDateStart, String dueDateEnd);
}

