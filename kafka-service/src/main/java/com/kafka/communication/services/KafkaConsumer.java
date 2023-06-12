package com.kafka.communication.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kafka.communication.objects.Output;
import com.kafka.communication.utils.AppConstants;

@Service
public class KafkaConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("{inventory.service.url}")
	private String inventoryURL;
	
	public KafkaConsumer(RestTemplate restTemplate) {
		this.restTemplate=restTemplate;
	}
	
    @KafkaListener(topics = AppConstants.TOPIC_NAME,
                    groupId = AppConstants.GROUP_ID)
    public void consume(Output message){
    	if(message.getKafkaStatus()=="SUCCESS") {
    		restTemplate.postForObject(inventoryURL+"/update", message, Void.class);
            LOGGER.info(String.format("Message received -> %s", message));
    	}
    	else {
            LOGGER.info(String.format("Error receiving the message"));

    	}
    }
}
