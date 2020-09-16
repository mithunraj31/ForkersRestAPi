package com.mbel.dao;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mbel.model.SchedulePatternProduct;

@Repository
public interface SchedulePatternProductDao extends JpaRepository<SchedulePatternProduct, Integer> {

	@Query("FROM SchedulePatternProduct s WHERE s.schedulePatternId IN :id")
	public List<SchedulePatternProduct> findAllBySchedulePatternId (@Valid int id);

	@Transactional
	@Modifying
	@Query("Delete FROM SchedulePatternProduct s WHERE s.schedulePatternId IN :id")
	public void deleteAllBySchedulePatternId(int id);

}
