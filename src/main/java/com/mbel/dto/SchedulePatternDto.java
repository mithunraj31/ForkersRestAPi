package com.mbel.dto;

import java.time.LocalDateTime;

import com.mbel.model.UserEntity;

public class SchedulePatternDto {

	private int schedulePatternId;

	private String schedulePatternName;
	
	private String pattern;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private UserEntity createdUser;
	
	private UserEntity updatedUser;
	
	private boolean isPrivate;
	

	public int getSchedulePatternId() {
		return schedulePatternId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
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


	public String getSchedulePatternName() {
		return schedulePatternName;
	}

	public void setSchedulePatternName(String schedulePatternName) {
		this.schedulePatternName = schedulePatternName;
	}

	public boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public UserEntity getCreatedUser() {
		return createdUser;
	}

	public UserEntity getUpdatedUser() {
		return updatedUser;
	}

	public void setCreatedUser(UserEntity createdUser) {
		this.createdUser = createdUser;
	}

	public void setUpdatedUser(UserEntity updatedUser) {
		this.updatedUser = updatedUser;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


	

	
	

}
