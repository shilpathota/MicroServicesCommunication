package com.kafka.communication.services;

import org.springframework.stereotype.Service;

import com.kafka.communication.Objects.MessageStatus;
import com.kafka.communication.Objects.Output;

@Service
public class InventoryService {
	
	public void updateInventory(Output message) {
		if(message.getStatus()==MessageStatus.SUCCESS) {
			if(message.getAction()=="ADD") {
				System.out.println(getInventory(message.getProductID())+message.getQuantity());
			}
			else if(message.getAction()=="REMOVE") {
				System.out.println(getInventory(message.getProductID())-message.getQuantity());
			}
		}
	}

	private int getInventory(String productID) {
		//query database and get the inventory
		return 0;
	}
}
