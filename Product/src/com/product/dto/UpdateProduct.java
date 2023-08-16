package com.product.dto;

import java.io.InputStream;
import java.util.Optional;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.core.header.FormDataContentDisposition;

@XmlRootElement(name = "updateProduct")
public class UpdateProduct {
	private String code;
	private String name;
	private String description;
//	private Optional<InputStream> filePath;
//	private Optional<FormDataContentDisposition> contentDisposition;
	private double price;
	private int quantity;
	
	public UpdateProduct() {
		super();
	}

	public UpdateProduct(String code, String name, String description, double price, int quantity) {
		super();
		this.code = code;
		this.name = name;
		this.description = description;
//		this.filePath = filePath;
//		this.contentDisposition = contentDisposition;
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

//	public Optional<InputStream> getFilePath() {
//		return filePath;
//	}
//
//	public void setFilePath(Optional<InputStream> filePath) {
//		this.filePath = filePath;
//	}
//
//	public Optional<FormDataContentDisposition> getContentDisposition() {
//		return contentDisposition;
//	}
//
//	public void setContentDisposition(Optional<FormDataContentDisposition> contentDisposition) {
//		this.contentDisposition = contentDisposition;
//	}

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
	
	
	
	
}
