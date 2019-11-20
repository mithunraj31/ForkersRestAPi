package com.mbel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.ProductSet;

@Repository
public interface ProductSetDao extends JpaRepository<ProductSet, Integer> {
	
	
	
	@Query(value="SELECT pst.product_set_id,pst.set_id,pst.qty as quantity,pst.product_component_id,ps.product_id as package_id,"
			+ "ps.product_name as package_name,ps.description as package_desc,ps.price as package_price,"
			+ "ps.moq as package_moq,ps.lead_time as package_lead,ps.obic_no as package_obic,"
			+ "ps.qty as package_qty,ps.is_set as package_set,ps.active as package_active,"
			+ "ps.created_at_date_time as package_created,ps.updated_at_date_time as package_update,ps.user_id as package_user,"
			+ "pc.product_id,pc.product_name,pc.description,pc.price,pc.moq,pc.lead_time,pc.obic_no,pc.qty,pc.is_set,"
			+ "pc.active,pc.created_at_date_time,pc.updated_at_date_time,pc.user_id "
			+ "FROM `product_set` pst "
			+ "JOIN product ps ON pst.set_id = ps.product_id "
			+ "JOIN product pc ON pst.product_component_id = pc.product_id "
			+ "ORDER BY pst.set_id", nativeQuery = true
			
			)
	
	public List<Map<Object,Object>> getAll();
	
}