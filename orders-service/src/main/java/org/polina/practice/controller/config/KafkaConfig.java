package org.polina.practice.controller.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topics.partitions}")
    private int partitions;

    @Value("${spring.kafka.topics.replication-factor}")
    private short replicationFactor;

    @Bean
    public NewTopic newOrdersTopic() {
        return TopicBuilder.name("new_orders")
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
    }
}
