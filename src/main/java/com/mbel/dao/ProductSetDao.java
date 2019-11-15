package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.ProductSet;

@Repository
public interface ProductSetDao extends JpaRepository<ProductSet, Integer> {
}
