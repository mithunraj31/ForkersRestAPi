package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.Production;
import com.mbel.model.ProductionSet;

@Repository
public interface ProductionSetDao extends JpaRepository<ProductionSet, Integer> {
}
