package com.product.dto;

import java.util.List;

public class PaginationResponse {
	private List<ProductResponse> items;
    private int totalCount;
    private int totalPages;

    public PaginationResponse(List<ProductResponse> items, int totalCount, int totalPages) {
        this.items = items;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }

	public PaginationResponse() {
		super();
	}

	public List<ProductResponse> getItems() {
		return items;
	}

	public void setItems(List<ProductResponse> items) {
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
