package com.product.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderRequest")
public class OrderRequest {
	 private String customerName;
	 private String customerPhoneNumber;
	 private String address;
	 private List<OrderItemRequest> orderItems;
	public OrderRequest() {
		super();
	}
	public OrderRequest(String customerName, String customerPhoneNumber, String address,
			List<OrderItemRequest> orderItems) {
		super();
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.address = address;
		this.orderItems = orderItems;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<OrderItemRequest> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemRequest> orderItems) {
		this.orderItems = orderItems;
	}
	 
	 
}
