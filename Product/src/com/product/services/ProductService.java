package com.product.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.dto.UpdateProduct;
import com.product.util.ConnectionDatasource;


public class ProductService {
	
	private static String filePath = "D:/eclipse 2020 workspace/Product/WebContent/uploadImage/";
	
	public static ProductResponse create(ProductRequest productRequest)throws IOException {
		
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into product values(?,?,?,?,?,?,?)")){
			String mainFilePath = filePath + productRequest.getContentDisposition().getFileName();
			Files.copy(productRequest.getFilePath(), Paths.get(mainFilePath),StandardCopyOption.REPLACE_EXISTING);
			
			
			statement.setString(1, productRequest.getCode());
			statement.setString(2,productRequest.getDescription());
			statement.setString(3, mainFilePath);
			statement.setString(4,productRequest.getContentDisposition().getFileName());
			statement.setString(5, productRequest.getName());
			statement.setDouble(6, productRequest.getPrice());
			statement.setInt(7, productRequest.getQuantity());
			int result =  statement.executeUpdate();
			if (result > 0) {
				ProductResponse response = new ProductResponse();
				response.setCode(productRequest.getCode());
				response.setName(productRequest.getName());
				response.setDescription(productRequest.getDescription());
				response.setFilePath(mainFilePath);
				response.setPrice(productRequest.getPrice());
				response.setQuantity(productRequest.getQuantity());
				return response;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<ProductResponse> findAll() {
		try(Connection connection = ConnectionDatasource.getConnection();
				PreparedStatement statement = connection.prepareStatement("select * from product")){
				ResultSet result = statement.executeQuery();
				List<ProductResponse> list = new ArrayList<ProductResponse>();
				while(result.next()) {
					ProductResponse response = new ProductResponse();
					response.setId(result.getInt("id"));
					response.setCode(result.getString("code"));
					response.setName(result.getString("name"));
					response.setDescription(result.getString("description"));
					response.setFilePath(result.getString("filepath"));
					response.setPrice(result.getDouble("price"));
					response.setQuantity(result.getInt("quantity"));
					list.add(response);
				}
				return list;
			}catch (Exception e) {
				// TODO: handle exception
			}
			return null;
	}
	
	public static ProductResponse findById(int id) {
		
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from product where id = ?")){
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				return new ProductResponse(
						result.getInt("id"),
						result.getString("code"),
						result.getString("name"),
						result.getString("description"),
						result.getString("filepath"),
						result.getDouble("price"),
						result.getInt("quantity")
						);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ProductResponse findByName(String name) {
		
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from product where name = ?")){
			statement.setString(1, name);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				return new ProductResponse(
						result.getInt("id"),
						result.getString("code"),
						result.getString("name"),
						result.getString("description"),
						result.getString("filepath"),
						result.getDouble("price"),
						result.getInt("quantity")
						);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ProductResponse updateWithImage(int id,ProductRequest productRequest)throws IOException {

		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement(""
					+ "update product set code = ? ,name = ? ,description = ? ,filepath = ? ,imagename = ? ,price = ? ,quantity = ? "
					+ "where id = ?",
					ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE)){
			
			String mainFilePath = filePath + productRequest.getContentDisposition().getFileName();
			Files.copy(productRequest.getFilePath(), Paths.get(mainFilePath),StandardCopyOption.REPLACE_EXISTING);
			
			statement.setString(1, productRequest.getCode());
			statement.setString(3,productRequest.getDescription());
			statement.setString(4, mainFilePath);
			statement.setString(5,productRequest.getContentDisposition().getFileName());
			statement.setString(2, productRequest.getName());
			statement.setDouble(6, productRequest.getPrice());
			statement.setInt(7, productRequest.getQuantity());
			statement.setInt(8, id);
			int result = statement.executeUpdate();
			
			if(result > 0) {
				ProductResponse response = new ProductResponse();
				response.setCode(productRequest.getCode());
				response.setName(productRequest.getName());
				response.setDescription(productRequest.getDescription());
				response.setFilePath(mainFilePath);
				response.setPrice(productRequest.getPrice());
				response.setQuantity(productRequest.getQuantity());
				return response;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ProductResponse updateWithOutImage(int id,UpdateProduct updateProduct) {
		
		try(Connection connection = ConnectionDatasource.getConnection();
				PreparedStatement statement = connection.prepareStatement(""
						+ "update product set code = ? ,name = ? ,description = ? ,price = ? ,quantity = ? "
						+ "where id = ?",
						ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE)){
				
				
				statement.setString(1, updateProduct.getCode());
				statement.setString(2, updateProduct.getName());
				statement.setString(3,updateProduct.getDescription());
				statement.setDouble(4, updateProduct.getPrice());
				statement.setInt(5, updateProduct.getQuantity());
				statement.setInt(6, id);
				int result = statement.executeUpdate();
				
				if(result > 0) {
					ProductResponse response = new ProductResponse();
					response.setCode(updateProduct.getCode());
					response.setName(updateProduct.getName());
					response.setDescription(updateProduct.getDescription());
					response.setPrice(updateProduct.getPrice());
					response.setQuantity(updateProduct.getQuantity());
					return response;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
	}

	public static Response getImage(String imageName)throws IOException {
		File imageFile = new File(filePath + imageName);
		if(!imageFile.exists() || !imageFile.isFile()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		FileInputStream fileInputStream = new FileInputStream(imageFile);
		long contentLegth = imageFile.length();
		return Response.ok(fileInputStream).header("Content-Length", String.valueOf(contentLegth))
				.build();
	}
	
	public static Response deleteById(int id) {
		
		try (Connection connection = ConnectionDatasource.getConnection();
			 PreparedStatement statement = connection.prepareStatement("delete from product where id = ?")){
			
			statement.setInt(1, id);
			statement.executeUpdate();
			return Response.status(Response.Status.CREATED).entity("Order Deleted Successfully").build();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}























































