package com.mbel.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbel.dao.SchedulePatternDao;
import com.mbel.dao.SchedulePatternProductDao;
import com.mbel.dto.SchedulePatternDto;
import com.mbel.model.SchedulePattern;
import com.mbel.model.SchedulePatternProduct;

@Service("SchedulePatternServiceImpl")
public class SchedulePatternServiceImpl {

	@Autowired
	SchedulePatternDao schedulePatternDao;

	@Autowired
	SchedulePatternProductDao schedulePatternProductDao;



	public void save(@Valid SchedulePatternDto schedulePatternDto) throws Exception {
		SchedulePattern schedulePattern =new SchedulePattern();
		schedulePattern.setSchedulePatternName(schedulePatternDto.getSchedulePatternName()); 
		schedulePattern.setCreatedAt(LocalDateTime.now());
		schedulePattern.setUpdatedAt(LocalDateTime.now());
		schedulePattern.setUserId(schedulePatternDto.getUserId());
		try {
			schedulePatternDao.save(schedulePattern);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
		int schedulePatternId =schedulePattern.getSchedulePatternId();
		List<SchedulePatternProduct> schedulePatternProductList= new ArrayList<SchedulePatternProduct>();
		for(SchedulePatternProduct individualSchedulePatternProduct:schedulePatternDto.getSchedulePatternProduct()) {
			SchedulePatternProduct schedulePatternProduct =new SchedulePatternProduct();
			schedulePatternProduct.setSchedulePatternId(schedulePatternId);
			schedulePatternProduct.setProductId(individualSchedulePatternProduct.getProductId());
			schedulePatternProductList.add(schedulePatternProduct);
		}
		try {
			schedulePatternProductDao.saveAll(schedulePatternProductList);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}

	}


	public SchedulePatternDto getPatternById(@Valid int patternId) {
		SchedulePattern schedulePattern =schedulePatternDao.findById(patternId).orElse(null);
		SchedulePatternDto schedulePatternDto = new SchedulePatternDto();
		if(Objects.nonNull(schedulePattern)) {
			List<SchedulePatternProduct> schedulePatternProductList=schedulePatternProductDao.findAllBySchedulePatternId(patternId);
			schedulePatternDto.setSchedulePatternId(schedulePattern.getSchedulePatternId());
			schedulePatternDto.setSchedulePatternName(schedulePattern.getSchedulePatternName());
			schedulePatternDto.setCreatedAt(schedulePattern.getCreatedAt());
			schedulePatternDto.setUpdatedAt(schedulePattern.getUpdatedAt());
			schedulePatternDto.setUserId(schedulePattern.getUserId());
			schedulePatternDto.setSchedulePatternProduct(schedulePatternProductList);
		}

		return schedulePatternDto;
	}

	public void getUpdatePatternById(int patternId, @Valid SchedulePatternDto schedulePatternDto) throws Exception {
		SchedulePattern schedulePattern =schedulePatternDao.findById(patternId).orElse(null);
		if(Objects.nonNull(schedulePattern)) {
			schedulePattern.setSchedulePatternName(schedulePatternDto.getSchedulePatternName()); 
			schedulePattern.setUpdatedAt(LocalDateTime.now());
			schedulePattern.setUserId(schedulePatternDto.getUserId());
			try {
				schedulePatternDao.save(schedulePattern);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());

			}
		}
		try {
			schedulePatternProductDao.deleteAllBySchedulePatternId(patternId);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
		List<SchedulePatternProduct> schedulePatternProductList=schedulePatternDto.getSchedulePatternProduct();
		try {
			schedulePatternProductList.forEach(action->action.setSchedulePatternId(patternId));
			schedulePatternProductDao.saveAll(schedulePatternProductList);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
	}

	public void deletePatternById(@Valid int patternId) throws Exception {
		try {
			schedulePatternDao.deleteById(patternId);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
		try {
			schedulePatternProductDao.deleteAllBySchedulePatternId(patternId);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());

		}
	}

}
