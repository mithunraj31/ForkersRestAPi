package com.mbel.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
	
	@Query(nativeQuery = true,value = "call GET_FULFILLED_ORDERS_AFTER_DATE(?1,?2)")
	List<Order> getOrdersAfterDate(LocalDateTime requiredHistoryDate, LocalDateTime tillDate);
}
