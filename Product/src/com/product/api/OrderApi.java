package com.product.api;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.product.dto.OrderRequest;
import com.product.dto.OrderResponse;
import com.product.dto.PaginationResponse;
import com.product.dto.PaginationResponse2;
import com.product.services.ExcelImposter;
import com.product.services.OrderService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/order")
public class OrderApi {
	
	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void create(OrderRequest orderRequest) {
		 OrderService.create(orderRequest);
	}
	
	@PUT
	@Path("update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void update(@PathParam("id")int order_id,OrderRequest orderRequest) {
		 OrderService.update(order_id, orderRequest);
	}

	@GET
	@Path("findAll")
	@Produces(MediaType.APPLICATION_JSON)
	public PaginationResponse2<OrderResponse> findAll(@QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("search") String searchValue) {
		
		 int startIndex = (page) * limit;
	        List<OrderResponse> orders = OrderService.findAll(startIndex, limit, searchValue);

	        int totalOrderCount = OrderService.getTotalOrderCount(searchValue);
	        int totalPages = (int) Math.ceil((double) totalOrderCount / limit);

	        PaginationResponse2<OrderResponse> response = new PaginationResponse2<>(orders, totalOrderCount, totalPages);
		return response;
	}
	
	@GET
	@Path("details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderResponse findById(@PathParam("id")int id) {
		return OrderService.findById(id);
	}
	
	@DELETE
	@Path("deleteOrder/{id}")
	public void delete(@PathParam("id")int id) {
		 OrderService.deleteById(id);
	}
	
	
	@GET
	@Path("/export")
	@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public Response exportToExcel() {
		return OrderService.exportToExcel();
	}
	
    @POST
    @Path("/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importExcel(@FormDataParam("file") InputStream fileInputStream) {

    	ExcelImposter.parseExcelData(fileInputStream);

        return Response.ok("Import successful").build();
    }
    
    
}








































































