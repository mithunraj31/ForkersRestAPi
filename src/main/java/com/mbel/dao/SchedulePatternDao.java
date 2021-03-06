package com.mbel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbel.model.SchedulePattern;

@Repository
public interface SchedulePatternDao extends JpaRepository<SchedulePattern, Integer> {
	
}
