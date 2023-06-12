package com.kafka.communication.services;

import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.kafka.communication.objects.Output;

@Service
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private final KafkaTemplate<String, Output> kafkaTemplate;
    private final Resilience4JCircuitBreakerFactory  circuitBreakerFactory;

    @Value("${kafka.topic}")
    private String topic;
    
    @Autowired
    public KafkaProducer(KafkaTemplate<String, Output> kafkaTemplate,
    		Resilience4JCircuitBreakerFactory  circuitBreakerFactory) {
        this.kafkaTemplate = kafkaTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }
    
    public void sendMessage(Output orderMessage){
    	   try {
    		   CircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafkaCircuitBreaker");
    	        circuitBreaker.run((Supplier<Void>) () -> {
    	            kafkaTemplate.send(topic, orderMessage)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            handleError(orderMessage, ex);
                        } else {
                        	LOGGER.info(orderMessage.toString());
                            handleSuccess(orderMessage);
                            LOGGER.info("Order message published successfully: {}", orderMessage);
                        }
                    });
    	            return null;
    	        }, (Function<Throwable, Void>) throwable -> {
    	            handleCircuitBreakerError(orderMessage, throwable);
    	            return null;
    	        });
    	    } catch (Exception e) {
    	        handleError(orderMessage, e);
    	    }
    }
    private void handleSuccess(Output orderMessage) {
        try {
        	orderMessage.setKafkaStatus("SUCCESS");
        } catch (RestClientException e) {
            LOGGER.error("Failed to update order status for order ID {}: {}", orderMessage.getOrderID(), e.getMessage());
        }
    }

    private void handleError(Output orderMessage, Throwable throwable) {
        try {
        	orderMessage.setKafkaStatus("FAILED");

        } catch (RestClientException e) {
            LOGGER.error("Failed to update order status for order ID {}: {}", orderMessage.getOrderID(), e.getMessage());
        }
        LOGGER.error("Failed to publish order message: {}", orderMessage, throwable);
    }

    private void handleCircuitBreakerError(Output orderMessage, Throwable throwable) {
        try {
        	orderMessage.setKafkaStatus("FAILED");

        } catch (RestClientException e) {
            LOGGER.error("Failed to update order status for order ID {}: {}", orderMessage.getOrderID(), e.getMessage());
        }
        LOGGER.error("Circuit breaker error occurred while publishing order message: {}", orderMessage, throwable);
    }
}
