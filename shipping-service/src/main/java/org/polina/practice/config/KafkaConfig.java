package org.polina.practice.config;

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
    public NewTopic sentOrdersTopic() {
        return TopicBuilder.name("sent_orders")
                .partitions(partitions)
                .replicas(replicationFactor)
                .build();
    }
}
