package com.product.dto;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "productResponse")
public class ProductResponse {
	private int id;
	private String code;
	private String name;
	private String description;
	private String filePath;
	private double price;
	private int quantity;
	public ProductResponse() {
		super();
	}
	public ProductResponse(int id,String code, String name, String description, String filePath, double price,
			int quantity) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.filePath = filePath;
		this.price = price;
		this.quantity = quantity;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	

	
	
}
