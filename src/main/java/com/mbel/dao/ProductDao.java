package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
}
