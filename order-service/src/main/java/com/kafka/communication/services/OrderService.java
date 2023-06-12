package com.kafka.communication.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.kafka.communication.Objects.Output;

@Service
public class OrderService {
	
	public Output getOrder(String orderID) {
		Output output = new Output();
		output.setOrderID(orderID);
		output.setProductID("PRODUCT1");
		output.setQuantity(1);
		output.setStatus("success");
		output.setComments("placing the order");
		output.setAction("ADD");
		output.setTimeStamp(new Date());
		
		return output;
	}
}
