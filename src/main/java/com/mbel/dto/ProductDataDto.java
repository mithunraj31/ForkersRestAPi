package com.mbel.dto;

import java.util.List;

import com.mbel.model.PredictionData;
import com.mbel.model.ProductIncomingShipmentModel;
import com.mbel.model.ProductOutgoingShipmentModel;

public class ProductDataDto {

	private int productId;

	private String productName;

	private String description;

	private String obicNo;

	private String color;

	List<PredictionData> values;

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

	/**
	 * @return sum of Fulfilled's incomming order's qty
	 */
	public long getTotalFulfilledIncomingQty() {
		if (this.values == null || this.values.size() == 0) {
			return 0;
		}

		long total =  this.values.stream().mapToLong(x -> {
			ProductIncomingShipmentModel incoming = x.getIncoming();
			if (incoming != null 
				&& incoming.getIncomingOrders() != null
				&& incoming.getIncomingOrders().size() > 0) {
				
				long sumOfIncomingOrders = incoming.getIncomingOrders()
					.stream()
					.mapToLong(i -> 
						(i != null && i.isFulfilled()) ? i.getQuantity() : 0)
					.sum();

				return sumOfIncomingOrders;
			}
			return 0;
		}).sum();

		return total;
	}

	/**
	 * @return sum of Fulfilled's outgoing order's qty
	 */
	public long getTotalFulfilledOutgoingQty() {
		if (this.values == null || this.values.size() == 0) {
			return 0;
		}

		long total =  this.values.stream().mapToLong(x -> {
			ProductOutgoingShipmentModel outgoing = x.getOutgoing();
			if (outgoing != null 
				&& outgoing.getOrders() != null
				&& outgoing.getOrders().size() > 0) {
				long sumOfOutgoingOrders = outgoing.getOrders()
					.stream()
					.mapToLong(i -> 
						(i != null && i.isFulfilled()) ? i.getQuantity() : 0)
					.sum();

				return sumOfOutgoingOrders;
			}
			return 0;
		}).sum();

		return total;
	}
}
