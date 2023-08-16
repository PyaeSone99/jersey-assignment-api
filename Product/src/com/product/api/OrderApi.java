package com.product.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.product.dto.OrderRequest;
import com.product.dto.OrderResponse;
import com.product.services.OrderService;

@Path("/order")
public class OrderApi {
	
	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(OrderRequest orderRequest) {
		return OrderService.create(orderRequest);
	}
	
	@PUT
	@Path("update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id")int order_id,OrderRequest orderRequest) {
		return OrderService.update(order_id, orderRequest);
	}

	@GET
	@Path("findAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<OrderResponse> findAll() {
		return OrderService.findAll();
	}
	
	@GET
	@Path("details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderResponse findById(@PathParam("id")int id) {
		return OrderService.findById(id);
	}
	
	@DELETE
	@Path("deleteOrder/{id}")
	public Response delete(@PathParam("id")int id) {
		return OrderService.deleteById(id);
	}
}








































































