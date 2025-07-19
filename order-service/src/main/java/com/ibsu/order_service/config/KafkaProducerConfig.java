package com.ibsu.order_service.config;

import com.ibsu.common.event.ItemsReservedEvent;
import com.ibsu.common.event.OrderCanceledEvent;
import com.ibsu.common.event.OrderConfirmedEvent;
import com.ibsu.order_service.dto.OrderResponseDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import com.ibsu.common.event.OrderPlacedEvent;
import org.springframework.kafka.support.serializer.JsonSerializer;
// other imports
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private <T> KafkaTemplate<String, T> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, OrderResponseDTO> orderResponseKafkaTemplate() {
        return kafkaTemplate();
    }

    @Bean
    public KafkaTemplate<String, OrderPlacedEvent> orderPlacedKafkaTemplate() {
        return kafkaTemplate();
    }

    @Bean
    public KafkaTemplate<String, ItemsReservedEvent> itemsReservedKafkaTemplate() {
        return kafkaTemplate();
    }

    @Bean
    public KafkaTemplate<String, OrderCanceledEvent> orderCancelledKafkaTemplate() {
        return kafkaTemplate();
    }

    @Bean
    public KafkaTemplate<String, OrderConfirmedEvent> orderConfirmedKafkaTemplate() {
        return kafkaTemplate();
    }
}


