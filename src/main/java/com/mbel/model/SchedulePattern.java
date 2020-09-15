package com.mbel.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "SchedulePattern")
public class SchedulePattern {
	

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int schedulePatternId;
    
    private String schedulePatternName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private int userId;

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

	public String getSchedulePatternName() {
		return schedulePatternName;
	}

	public void setSchedulePatternName(String schedulePatternName) {
		this.schedulePatternName = schedulePatternName;
	}
  
}