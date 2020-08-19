package com.mbel.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mbel.model.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
	
	
    @Query(nativeQuery=true,value ="call GET_ACTIVE_DISPLAYED_PRODUCT()")
	List<Product> getActiveDisplayedProduct();
}
