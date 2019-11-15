package com.mbel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_component_id")
	private Product production;

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


	public Product getProduction() {
		return production;
	}

	public void setProduction(Product production) {
		this.production = production;

	}




}