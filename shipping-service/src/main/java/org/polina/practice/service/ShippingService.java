package org.polina.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.polina.practice.model.Order;
import org.polina.practice.model.Status;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @KafkaListener(topics = "payed_orders", groupId = "shipping-group", concurrency = "5")
    public void processShipping(Order order, Acknowledgment acknowledgment) {
        if (simulateShippingSuccess()) {
            order.setStatus(Status.SENT);
            log.info("Заказ успешно отправлен: {}", order.getId());
            kafkaTemplate.send("sent_orders", order.getId().toString(), order);
            log.info("Информация об успешной отгрузке отправлена в Kafka: {}", order);
            acknowledgment.acknowledge();
        } else {
            log.error("Отправка не удалась для заказа: {}", order.getId());
        }
    }

    private boolean simulateShippingSuccess() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
