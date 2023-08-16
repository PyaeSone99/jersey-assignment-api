package com.product.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
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
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.dto.UpdateProduct;
import com.product.services.OrderService;
import com.product.services.ProductService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/product")
public class ProductApi {	
	
	@POST
	@Path("create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ProductResponse create(@FormDataParam("code")String code,
							@FormDataParam("name")String name,
							@FormDataParam("description")String description,
							@FormDataParam("image")InputStream file,
							@FormDataParam("image")FormDataContentDisposition contentDisposition,
							@FormDataParam("price")double price,
							@FormDataParam("quantity")int quantity)throws IOException {
		ProductRequest request = new ProductRequest(code,name,description,file,contentDisposition,price,quantity);
		
		return ProductService.create(request);
	}
	
	@PUT
	@Path("update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ProductResponse update(@PathParam("id")int id,
							@FormDataParam("code")String code,
							@FormDataParam("name")String name,
							@FormDataParam("description")String description,
							@FormDataParam("image")InputStream file,
							@FormDataParam("image")FormDataContentDisposition contentDisposition,
							@FormDataParam("price")double price,
							@FormDataParam("quantity")int quantity)throws IOException {
		if (file == null) {
			UpdateProduct updateProduct = new UpdateProduct(code,name,description,price,quantity);
			System.out.println("here");
			System.out.println(file);
			return ProductService.updateWithOutImage(id,updateProduct);
		}else {
			ProductRequest request = new ProductRequest(code,name,description,file,contentDisposition,price,quantity);
			System.out.println("there");
			System.out.println(file);
			return ProductService.updateWithImage(id,request);
		}
	}
	
	@GET
	@Path("findAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductResponse> findAll() {
		return ProductService.findAll();
	}
	
	@GET
	@Path("/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductResponse findById(@PathParam("id")int id) {
		return ProductService.findById(id);
	}
	
	@GET
	@Path("/withProductName/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductResponse findByName(@PathParam("name")String name) {
		return ProductService.findByName(name);
	}
	
	@GET
	@Path("/image/{imageName}")
	@Produces("image/jpeg")
	public Response getImage(@PathParam("imageName")String imageName)throws IOException {
		return ProductService.getImage(imageName);
	}
	
	@DELETE
	@Path("deleteProduct/{id}")
	public Response delete(@PathParam("id")int id) {
		return ProductService.deleteById(id);
	}
}









































