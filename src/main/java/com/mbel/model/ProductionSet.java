package com.mbel.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "ProductionSet")
public class ProductionSet {
	

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_set_id")
    private int productSetId;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "set_id")
    private int setId;
    @JoinColumn(name = "productId")
    private int productComponentId;
    @Column(name = "qty")
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