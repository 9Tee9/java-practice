package org.polina.practice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.polina.practice.model.Order;
import org.polina.practice.model.Status;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderStatusService {

    @KafkaListener(topics = "payed_orders", groupId = "order-group")
    public void handlePayedOrder(ConsumerRecord<String, Order> record, Acknowledgment acknowledgment) {
        try {
            int partition = record.partition();
            Order order = record.value();
            System.out.println("Поток: " + Thread.currentThread().getName() +
                    ", Партиция: " + partition);
            log.info("Получено уведомление об оплате заказа: {}", order);
            order.setStatus(Status.PAID);
            log.info("Статус заказа успешно обновлен на PAID: {}", order);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обработке заказа: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "sent_orders", groupId = "order-group")
    public void handleSentOrder(ConsumerRecord<String, Order> record, Acknowledgment acknowledgment) {
        try {
            int partition = record.partition();
            Order order = record.value();
            System.out.println("Поток: " + Thread.currentThread().getName() +
                    ", Партиция: " + partition);
            log.info("Получено уведомление об отправке заказа: {}", order);
            order.setStatus(Status.SENT);
            log.info("Статус заказа успешно обновлен на SENT: {}", order);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обработке заказа: {}", e.getMessage(), e);
        }
    }
}