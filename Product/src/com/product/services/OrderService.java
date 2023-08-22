package com.product.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.Response;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.product.dto.OrderItemRequest;
import com.product.dto.OrderItemResponse;
import com.product.dto.OrderRequest;
import com.product.dto.OrderResponse;
import com.product.dto.ProductResponse;
import com.product.util.ConnectionDatasource;
import com.sun.jersey.core.header.FormDataContentDisposition;



public class OrderService {
	
//	Creation Order Start
	public static void create(OrderRequest orderRequest) {
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
//			return Response.status(Response.Status.CREATED).entity("Order created Successfully").build();
		}catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
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
	
	public static List<OrderResponse> findAll(int startIndex, int limit, String searchValue) {
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = buildSearchStatement(connection, startIndex, limit, searchValue)){
			
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
	
	 public static int getTotalOrderCount(String searchValue) {
	        try (Connection connection = ConnectionDatasource.getConnection();
	             PreparedStatement statement = buildCountStatement(connection, searchValue);
	             ResultSet result = statement.executeQuery()) {
	            if (result.next()) {
	                return result.getInt(1);
	            }
	            return 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
	
	 private static PreparedStatement buildCountStatement(Connection connection, String searchValue) throws SQLException {
		    String baseQuery = "SELECT COUNT(*) FROM [order]";
		    String searchCondition = "";

		    if (searchValue != null && !searchValue.isEmpty()) {
		        searchCondition = " WHERE customer_name LIKE ? OR customer_phone_number LIKE ? OR address LIKE ?";
		    }

		    String finalQuery = baseQuery + searchCondition;

		    PreparedStatement statement = connection.prepareStatement(finalQuery);
		    int paramIndex = 1;

		    if (searchValue != null && !searchValue.isEmpty()) {
		        statement.setString(paramIndex++, "%" + searchValue + "%"); // For customer name search
		        statement.setString(paramIndex++, "%" + searchValue + "%"); // For customer phone search
		        statement.setString(paramIndex++, "%" + searchValue + "%"); // For customer address search
		    }

		    return statement;
		}

	 
	 private static PreparedStatement buildSearchStatement(Connection connection, int startIndex, int limit, String searchValue) throws SQLException {
	        String baseQuery = "SELECT id, order_id, customer_name, customer_phone_number, address, total_price FROM [order]";
	        String searchCondition = "";

	        if (searchValue != null && !searchValue.isEmpty()) {
	            searchCondition = " WHERE customer_name LIKE ? OR customer_phone_number LIKE ? OR address LIKE ?";
	        }

	        String finalQuery = baseQuery + searchCondition + " ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

	        PreparedStatement statement = connection.prepareStatement(finalQuery);
	        int paramIndex = 1;

	        if (searchValue != null && !searchValue.isEmpty()) {
	            statement.setString(paramIndex++, "%" + searchValue + "%"); // For customer name search
	            statement.setString(paramIndex++, "%" + searchValue + "%"); // For customer phone search
	            statement.setString(paramIndex++, "%" + searchValue + "%");
	        }

	        statement.setInt(paramIndex++, startIndex);
	        statement.setInt(paramIndex++, limit);

	        return statement;
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
	public static void update(int order_id,OrderRequest orderRequest) {
		
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement("update [order] set customer_name=?, customer_phone_number=?, address=?, total_price=? where id=?")){
			
			statement.setString(1, orderRequest.getCustomerName());
			statement.setString(2, orderRequest.getCustomerPhoneNumber());
			statement.setString(3, orderRequest.getAddress());

			List<OrderItemRequest> updatedOrderItems = orderRequest.getOrderItems();
			deleteOldOrderItems(order_id);
			for(OrderItemRequest itemRequest : updatedOrderItems) {
				
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
			

//			return Response.status(Response.Status.CREATED).entity("Order Updated Successfully").build();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
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
	public static void deleteById(int id) {
		
		try (Connection connection = ConnectionDatasource.getConnection();
			 PreparedStatement statement = connection.prepareStatement("delete from [order] where id = ?")){
			
			statement.setInt(1, id);
			deleteOldOrderItems(id);
			statement.executeUpdate();
//			return Response.status(Response.Status.CREATED).entity("Order Deleted Successfully").build();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
	}
	
//	Deleting Order End
	
//	Excel Export Start
	
	public static Response exportToExcel() {
		try(Connection connection = ConnectionDatasource.getConnection();
			PreparedStatement statement = connection.prepareStatement(
			"SELECT o.*, oi.product_id, oi.quantity, p.name AS product_name " +
            "FROM [Order] o " +
            "JOIN OrderItem oi ON o.id = oi.order_id " +
            "JOIN Product p ON oi.product_id = p.id")){
			
			ResultSet resultSet = statement.executeQuery();
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Order Data");
			int rowNum = 0;
			int currentOrderId = -1;
			Row headerRow = sheet.createRow(rowNum++);
			headerRow.createCell(0).setCellValue("Order ID");
			headerRow.createCell(1).setCellValue("Customer Name");
			headerRow.createCell(2).setCellValue("Customer Phone Number");
			headerRow.createCell(3).setCellValue("Address");
			headerRow.createCell(4).setCellValue("Total Price");
			headerRow.createCell(5).setCellValue("Product ID");
			headerRow.createCell(6).setCellValue("Product Name");
			headerRow.createCell(7).setCellValue("Quantity");

			while (resultSet.next()) {
				Row orderRow = sheet.createRow(rowNum++);
				orderRow.createCell(0).setCellValue(resultSet.getString("order_id"));
				orderRow.createCell(1).setCellValue(resultSet.getString("customer_name"));
				orderRow.createCell(2).setCellValue(resultSet.getString("customer_phone_number"));
				orderRow.createCell(3).setCellValue(resultSet.getString("address"));
				orderRow.createCell(4).setCellValue(resultSet.getDouble("total_price"));
				orderRow.createCell(5).setCellValue(resultSet.getInt("product_id"));
				orderRow.createCell(6).setCellValue(resultSet.getString("product_name"));
				orderRow.createCell(7).setCellValue(resultSet.getInt("quantity"));

			}
			File excelFile = File.createTempFile("order_data", ".xlsx");
			try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
				workbook.write(outputStream);
			}
			Response.ResponseBuilder response = Response.ok(excelFile);
			response.header("Content-Disposition", "attachment; filename=order_data.xlsx");
			return response.build();

		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
//	Excel Export End
	
//	Excel Import Start
	
}





























