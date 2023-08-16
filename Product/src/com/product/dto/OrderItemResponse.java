package com.product.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderItemResponse")
public class OrderItemResponse {
	 private String productName;
	    private int quantity;
	    private double productPrice;
		public OrderItemResponse() {
			super();
		}
		public OrderItemResponse(String productName, int quantity, double productPrice) {
			super();
			this.productName = productName;
			this.quantity = quantity;
			this.productPrice = productPrice;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public double getProductPrice() {
			return productPrice;
		}
		public void setProductPrice(double productPrice) {
			this.productPrice = productPrice;
		}

	    
}
