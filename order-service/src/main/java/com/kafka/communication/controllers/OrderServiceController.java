package com.kafka.communication.controllers;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.kafka.communication.Objects.Output;
import com.kafka.communication.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderServiceController {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceController.class);

	@Autowired
	private OrderService orderService;
    private final RestTemplate restTemplate;
	
	@Value("${kafka.service.url}")
	private String kafkaURL;
	
	public OrderServiceController(@Autowired RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	 @PostMapping("/createOrder")
	public ResponseEntity<String> placeOrder(@RequestBody Output message){
		 logger.info(message.toString());
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 restTemplate.postForObject(kafkaURL+"/publish", new HttpEntity<>(message, headers), Void.class);
		return ResponseEntity.ok("order placed successfully");
	}
	
	@PutMapping("/cancelOrder")
	public ResponseEntity<String> cancelOrder(@RequestParam String OrderID ){
		Output output = orderService.getOrder(OrderID);
		output.setAction("REMOVE");
		output.setStatus("SUCCESS");
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForObject(kafkaURL+"/publish", new HttpEntity<>(output, headers), Void.class);
		return ResponseEntity.ok("order cancelled successfully");

	}
	
	@GetMapping("/getOrder")
	public ResponseEntity<Output> getOrder(@RequestParam String OrderID){
		return ResponseEntity.ok(orderService.getOrder(OrderID));
	}
}
