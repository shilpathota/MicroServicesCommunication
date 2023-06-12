package com.kafka.communication.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import com.kafka.communication.objects.Output;
import com.kafka.communication.utils.AppConstants;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic inventoryTopic() {
        return TopicBuilder.name(AppConstants.TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public ProducerFactory<String, Output> kafkaProducerFactory() {
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        //by default, it is set to 1 which means it waits for the leader replica to receive the message
        //if set to 0, it does not wait for the acknowledgement
        producerConfigs.put(ProducerConfig.ACKS_CONFIG, "all"); // Set acknowledgment to "all"

        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }
    
    @Bean
    public KafkaTemplate<String, Output> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
    
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
