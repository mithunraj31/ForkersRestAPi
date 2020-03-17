package com.mbel.dto;

import java.util.List;

import com.mbel.model.PredictionData;

public class ProductDataDto {
	
	private int productId;

	private String productName;

	private String description;

	private String obicNo;
	
	private String color;
	
	List<PredictionData>values;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObicNo() {
		return obicNo;
	}

	public void setObicNo(String obicNo) {
		this.obicNo = obicNo;
	}

	public List<PredictionData> getValues() {
		return values;
	}

	public void setValues(List<PredictionData> values) {
		this.values = values;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
