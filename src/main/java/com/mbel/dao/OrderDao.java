package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
}
