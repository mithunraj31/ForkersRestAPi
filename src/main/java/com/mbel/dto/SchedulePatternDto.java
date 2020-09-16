package com.mbel.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mbel.model.SchedulePatternProduct;

public class SchedulePatternDto {

	private int schedulePatternId;

	private String schedulePatternName;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private int userId;
	
	private List<SchedulePatternProduct> schedulePatternProduct =new ArrayList<>();

	public int getSchedulePatternId() {
		return schedulePatternId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public int getUserId() {
		return userId;
	}

	public void setSchedulePatternId(int schedulePatternId) {
		this.schedulePatternId = schedulePatternId;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<SchedulePatternProduct> getSchedulePatternProduct() {
		return schedulePatternProduct;
	}

	public void setSchedulePatternProduct(List<SchedulePatternProduct> schedulePatternProduct) {
		this.schedulePatternProduct = schedulePatternProduct;
	}

	public String getSchedulePatternName() {
		return schedulePatternName;
	}

	public void setSchedulePatternName(String schedulePatternName) {
		this.schedulePatternName = schedulePatternName;
	}


}
