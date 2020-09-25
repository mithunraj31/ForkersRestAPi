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

	private String pattern;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private int createdUserId;

	private int updatedUserId;

	private boolean isPrivate;


	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

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

	public int getCreatedUserId() {
		return createdUserId;
	}

	public int getUpdatedUserId() {
		return updatedUserId;
	}

	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}

	public void setUpdatedUserId(int updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}




}