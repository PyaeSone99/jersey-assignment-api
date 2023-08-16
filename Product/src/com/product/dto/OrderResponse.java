package com.product.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderResponse")
public class OrderResponse {

	private int id;
	private int orderId;
	private String customerName;
	private String customerPhone;
	private String address;
	private double totalPrice;
	private List<OrderItemResponse> orderItems;
	public OrderResponse() {
		super();
	}
	public OrderResponse(int id, int orderId, String customerName, String customerPhone, String address,
			double totalPrice, List<OrderItemResponse> orderItems) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.customerName = customerName;
		this.customerPhone = customerPhone;
		this.address = address;
		this.totalPrice = totalPrice;
		this.orderItems = orderItems;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<OrderItemResponse> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemResponse> orderItems) {
		this.orderItems = orderItems;
	}
	
	

}
