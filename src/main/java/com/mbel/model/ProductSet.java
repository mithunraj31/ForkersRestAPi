package com.mbel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ProductSet")
public class ProductSet {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_set_id")
	private int productSetId;
	
	@Column(name = "set_id")
	private int setId;
	
	@Column(name = "qty")
	private int quantity;
	
	@Column(name = "product_component_id")
	private int  productComponentId;
	

	public ProductSet(){

	}

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


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getProductComponentId() {
		return productComponentId;
	}

	public void setProductComponentId(int productComponentId) {
		this.productComponentId = productComponentId;
	}



}