package org.polina.practice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @KafkaListener(topics = "new_orders", groupId = "payment-group", concurrency = "3")
    public void handleNewOrder(Order order, Acknowledgment acknowledgment) {
        log.info("Получено уведомление об оплате заказа: {}", order);
        if (simulatePayment()) {
            order.setStatus(Status.PAID);
            log.info("Оплата успешно выполнена для заказа: {}", order.getId());
            kafkaTemplate.send("payed_orders", order.getId().toString(), order);
            log.info("Информация об успешной оплате отправлена в Kafka: {}", order);
            acknowledgment.acknowledge();
        } else {
            log.error("Оплата не удалась для заказа: {}", order.getId());
        }
    }
    private boolean simulatePayment() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
