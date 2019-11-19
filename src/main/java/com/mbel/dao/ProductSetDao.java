package com.mbel.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.dto.SaveComponentDto;
import com.mbel.dto.SaveProductSetDto;
import com.mbel.model.ProductSet;
import com.mbel.model.ProductSetModel;

@Repository
public interface ProductSetDao extends JpaRepository<ProductSet, Integer> {
	
	
	
	@Query(value="SELECT pst.product_set_id,pst.set_id,pst.qty as quantity,pst.product_component_id,ps.product_id,ps.product_name,ps.description,ps.price,"
			+ "ps.moq,ps.lead_time,ps.obic_no,ps.qty,ps.is_set,ps.active,ps.created_at_date_time,ps.updated_at_date_time,ps.user_id "
			+ "FROM `product_set` pst "
			+ "JOIN product ps ON pst.set_id = ps.product_id "
			+ "JOIN product pc ON pst.product_component_id = pc.product_id "
			+ "ORDER BY pst.set_id", nativeQuery = true
			
			)
	
	public List<ProductSet> getAll();
	
}
