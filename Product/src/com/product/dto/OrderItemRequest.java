package com.product.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderItemRequest")
public class OrderItemRequest {
	private int productId;
    private int quantity;
	public OrderItemRequest() {
		super();
	}
	public OrderItemRequest(int productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
}
