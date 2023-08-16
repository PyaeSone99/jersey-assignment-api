package com.product.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.Response;

import com.product.dto.OrderItemRequest;
import com.product.dto.OrderItemResponse;
import com.product.dto.OrderRequest;
import com.product.dto.OrderResponse;
import com.product.dto.ProductResponse;
import com.product.util.ConnectionDatasource;

public class OrderService {
	
//	Creation Order Start
	public static Response create(OrderRequest orderRequest) {
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into [order](order_id,customer_name,customer_phone_number,address,total_price)"
					+ " values (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
			
			int min = 1;
			int max = 1000;
			Random random = new Random();
			int order_id = random.nextInt(max-min + 1)+min;
			statement.setInt(1, order_id);
			statement.setString(2, orderRequest.getCustomerName());
			statement.setString(3, orderRequest.getCustomerPhoneNumber());
			statement.setString(4, orderRequest.getAddress());
			List<OrderItemRequest> orderItem = orderRequest.getOrderItems();
			float total_price = 0;
			for(OrderItemRequest itemRequest : orderItem) {
				double productPrice = getProductPrice(itemRequest.getProductId());
				total_price += itemRequest.getQuantity() * productPrice;
			}
			
			statement.setFloat(5, total_price);
			statement.executeUpdate();
			
			try(ResultSet generatedKey = statement.getGeneratedKeys()){
				if(generatedKey.next()) {
					int id = generatedKey.getInt(1);
					for(OrderItemRequest itemRequest : orderItem) {
						insertOrderItem(id,itemRequest);
					}
				}
			}
			return Response.status(Response.Status.CREATED).entity("Order created Successfully").build();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void insertOrderItem(int order_id,OrderItemRequest itemRequest) {
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("insert into orderItem(order_id,product_id,quantity)"
					+ "values (?,?,?)")){
			
			statement.setInt(1, order_id);
			statement.setInt(2, itemRequest.getProductId());
			statement.setInt(3, itemRequest.getQuantity());
			statement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static double getProductPrice(int productId) {
		double price = 0.0;
		try (Connection connection = ConnectionDatasource.getConnection();
				PreparedStatement statement = connection.prepareStatement("select price from product where id = ?")) {

			statement.setInt(1, productId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					price = resultSet.getDouble("price");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}
//	Creation Order end

//	Finding Orders Start
	
	public static List<OrderResponse> findAll() {
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("select id, order_id, customer_name, customer_phone_number, address, total_price from [order]")){
			
			ResultSet result = statement.executeQuery();
			List<OrderResponse> list = new ArrayList<OrderResponse>();
			while(result.next()) {
				OrderResponse orderResponse = new OrderResponse();
				orderResponse.setId(result.getInt("id"));
				orderResponse.setOrderId(result.getInt("order_id"));
				orderResponse.setCustomerName(result.getString("customer_name"));
				orderResponse.setCustomerPhone(result.getString("customer_phone_number"));
				orderResponse.setAddress(result.getString("address"));
				orderResponse.setTotalPrice(result.getFloat("total_price"));
				List<OrderItemResponse> orderItemResponses = getOrderItemsFromOrder(orderResponse.getId());
				orderResponse.setOrderItems(orderItemResponses);
				list.add(orderResponse);
			}
			return list;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<OrderItemResponse> getOrderItemsFromOrder(int id) {
		List<OrderItemResponse> orderItemslist = new ArrayList<OrderItemResponse>();
		try (Connection connection = ConnectionDatasource.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT p.name AS productName , oi.quantity, p.price AS productPrice "
						+ "FROM OrderItem oi INNER JOIN Product p ON oi.product_id = p.id WHERE oi.order_id = ?")) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				OrderItemResponse orderItemResponse = new OrderItemResponse();
				orderItemResponse.setProductName(resultSet.getString("productName"));
				orderItemResponse.setQuantity(resultSet.getInt("quantity"));
				orderItemResponse.setProductPrice(resultSet.getDouble("productPrice"));
				orderItemslist.add(orderItemResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderItemslist;
	}

	public static OrderResponse findById(int id) {
		
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from [order] where id = ?")){
			
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				OrderResponse orderResponse = new OrderResponse();
				orderResponse.setId(result.getInt("id"));
				orderResponse.setOrderId(result.getInt("order_id"));
				orderResponse.setCustomerName(result.getString("customer_name"));
				orderResponse.setCustomerPhone(result.getString("customer_phone_number"));
				orderResponse.setAddress(result.getString("address"));
				orderResponse.setTotalPrice(result.getFloat("total_price"));
				List<OrderItemResponse> orderItemResponses = getOrderItemsFromOrder(orderResponse.getId());
				orderResponse.setOrderItems(orderItemResponses);
				return orderResponse;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
// Finding Orders End
	
//	Updating Orders Start 
	public static Response update(int order_id,OrderRequest orderRequest) {
		
		try(Connection connection = ConnectionDatasource.getConnection();//update [order] set customer_name = 'john doe update' where id = 5;
			PreparedStatement statement = connection.prepareStatement("update [order] set customer_name=?, customer_phone_number=?, address=?, total_price=? where id=?")){
			
			statement.setString(1, orderRequest.getCustomerName());
			statement.setString(2, orderRequest.getCustomerPhoneNumber());
			statement.setString(3, orderRequest.getAddress());

			List<OrderItemRequest> updatedOrderItems = orderRequest.getOrderItems();
			
			for(OrderItemRequest itemRequest : updatedOrderItems) {
				deleteOldOrderItems(order_id);
				insertOrderItem(order_id,itemRequest);
			}
			
			float total_price = 0;
			for (OrderItemRequest itemRequest : updatedOrderItems) {
				double productPrice = getProductPrice(itemRequest.getProductId());
				total_price += itemRequest.getQuantity() * productPrice;
			}
			statement.setFloat(4, total_price);
			statement.setInt(5, order_id);
			statement.executeUpdate();
			

			return Response.status(Response.Status.CREATED).entity("Order Updated Successfully").build();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void deleteOldOrderItems(int id) {
		
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("delete from orderItem where order_id = ?")){
			
			statement.setInt(1, id);
			statement.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
//	Updating Orders End
	
//	Deleting order start 
	public static Response deleteById(int id) {
		
		try (Connection connection = ConnectionDatasource.getConnection();
			 PreparedStatement statement = connection.prepareStatement("delete from [order] where id = ?")){
			
			statement.setInt(1, id);
			deleteOldOrderItems(id);
			statement.executeUpdate();
			return Response.status(Response.Status.CREATED).entity("Order Deleted Successfully").build();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	Deleting Order End
}





























