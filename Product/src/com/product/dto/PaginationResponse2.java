package com.product.dto;

import java.util.List;

public class PaginationResponse2<T> {
	private List<T> items;
	private int totalCount;
	private int totalPages;

	public PaginationResponse2(List<T> items, int totalCount, int totalPages) {
		this.items = items;
		this.totalCount = totalCount;
		this.totalPages = totalPages;
	}

	public PaginationResponse2() {
		super();
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
	    
}
