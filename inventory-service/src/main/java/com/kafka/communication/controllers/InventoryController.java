package com.kafka.communication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kafka.communication.Objects.Output;
import com.kafka.communication.services.InventoryService;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@PostMapping("/update")
	public ResponseEntity<String> updateInventory(@RequestBody Output message) {
		inventoryService.updateInventory(message);
		return ResponseEntity.ok("Inventory is updated");
	}
}
