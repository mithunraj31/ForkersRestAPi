package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.Production;

@Repository
public interface ProductionDao extends JpaRepository<Production, Integer> {
}
