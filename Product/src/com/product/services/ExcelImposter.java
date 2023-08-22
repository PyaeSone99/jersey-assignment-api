package com.product.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.product.dto.OrderItemDto;
import com.product.dto.OrderItemRequest;
import com.product.dto.OrderRequest;

public class ExcelImposter {
	public static void parseExcelData(InputStream fileInputStream) {
        Map<String, List<OrderItemDto>> orderDataMap = new HashMap<String,List<OrderItemDto>>();

        try (Workbook workbook = WorkbookFactory.create(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                String customerName = row.getCell(0).getStringCellValue();
                String customerPhoneNumber = String.valueOf((long)row.getCell(1).getNumericCellValue());
                String address = row.getCell(2).getStringCellValue();
                int productId = (int) row.getCell(3).getNumericCellValue();
                int orderQuantity = (int) row.getCell(4).getNumericCellValue();

               
                OrderItemDto orderItem = new OrderItemDto(productId,orderQuantity);
                String orderKey = customerName + "-" + customerPhoneNumber + "-" + address;
                orderDataMap.computeIfAbsent(orderKey, k->new ArrayList<>()).add(orderItem);
            }
            
            for(Map.Entry<String, List<OrderItemDto>> entry : orderDataMap.entrySet()) {
            	String[] orderInfo = entry.getKey().split("-");
            	String customerName = orderInfo[0];
            	String customerPhoneNumber = orderInfo[1];
            	String address = orderInfo[2];
            	
            	List<OrderItemDto> orderItemDtos = entry.getValue();
                List<OrderItemRequest> orderItemRequests = new ArrayList<>();
                
                for (OrderItemDto dto : orderItemDtos) {
                    OrderItemRequest orderItemRequest = new OrderItemRequest(dto.getProductId(), dto.getQuantity());
                    orderItemRequests.add(orderItemRequest);
                }
            	OrderRequest ordReq = new OrderRequest(customerName,customerPhoneNumber,address,orderItemRequests);
            	OrderService.create(ordReq);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
}































