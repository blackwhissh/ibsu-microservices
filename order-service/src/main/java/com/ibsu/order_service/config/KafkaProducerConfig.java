package com.ibsu.order_service.config;

import com.ibsu.common.event.ItemsRemovedEvent;
import com.ibsu.order_service.dto.OrderResponseDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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

    @Bean
    public ProducerFactory<String, OrderResponseDTO> orderResponseProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderResponseDTO> orderResponseKafkaTemplate() {
        return new KafkaTemplate<>(orderResponseProducerFactory());
    }

    @Bean
    public ProducerFactory<String, OrderPlacedEvent> orderPlacedProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderPlacedEvent> orderPlacedKafkaTemplate() {
        return new KafkaTemplate<>(orderPlacedProducerFactory());
    }

    @Bean
    public ProducerFactory<String, ItemsRemovedEvent> itemsRemovedEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ItemsRemovedEvent> itemsRemovedEventKafkaTemplate() {
        return new KafkaTemplate<>(itemsRemovedEventProducerFactory());
    }
}

