package com.mbel.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "SchedulePatternProduct")
public class SchedulePatternProduct {
	

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int schedulePatternProductId;
    
    private int schedulePatternId;
    
    private int productId;

	public int getProductId() {
		return productId;
	}

	public int getSchedulePatternProductId() {
		return schedulePatternProductId;
	}

	public int getSchedulePatternId() {
		return schedulePatternId;
	}

	public void setSchedulePatternProductId(int schedulePatternProductId) {
		this.schedulePatternProductId = schedulePatternProductId;
	}

	public void setSchedulePatternId(int schedulePatternId) {
		this.schedulePatternId = schedulePatternId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
    


  
}