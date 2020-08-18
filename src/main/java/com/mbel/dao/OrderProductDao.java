package com.mbel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mbel.model.OrderProduct;

@Repository
public interface OrderProductDao extends JpaRepository<OrderProduct, Integer> {

	@Query(value="SELECT * FROM `order_product` WHERE  order_id = ?1"  , nativeQuery = true)
	public List<Map<Object, Object>> getByOrderId(int orderId);

	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM `order_product` WHERE  order_id = ?1" , nativeQuery = true)
	public void deleteByOrderId(int id);


	@Query("FROM OrderProduct o WHERE o.orderId IN :ids")
	public List<OrderProduct> findAllByOrderId(@Param("ids") List<Integer> ids);
}
