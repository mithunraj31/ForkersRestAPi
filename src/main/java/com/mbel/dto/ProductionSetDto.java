package com.mbel.dto;
import java.util.List;

import com.mbel.model.Production;


public class ProductionSetDto {

    private int productSetId;
    private int setId;
    private int productComponentId;
    private int quantity;
    

	public int getProductSetId() {
		return productSetId;
	}

	public void setProductSetId(int productSetId) {
		this.productSetId = productSetId;
	}

	public int getSetId() {
		return setId;
	}

	public void setSetId(int setId) {
		this.setId = setId;
	}

	public int getproductComponentId() {
		return productComponentId;
	}

	public void setproductComponentId(int productComponentId) {
		this.productComponentId = productComponentId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

    
    
}