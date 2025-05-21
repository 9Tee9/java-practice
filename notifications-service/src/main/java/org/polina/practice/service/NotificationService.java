package org.polina.practice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.polina.practice.model.Order;
import org.polina.practice.model.Status;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "sent_orders", groupId = "notification-group")
    public void handleSentOrder(ConsumerRecord<String, Order> record, Acknowledgment acknowledgment) {
        try {
            int partition = record.partition();
            Order order = record.value();
            System.out.println("Поток: " + Thread.currentThread().getName() +
                    ", Партиция: " + partition);
            log.info("Получен заказ для отправки уведомления: {}", order);
            sendNotification(order);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомления: {}", e.getMessage(), e);
        }
    }

    private void sendNotification(Order order) {
        if (order.getStatus() == Status.SENT) {
            log.info("Отправляем уведомление пользователю {} о доставке заказа: {}", order.getUserId(), order.getId());
            simulateNotificationSending(order);
        } else {
            log.warn("Заказ не имеет статус SENT, уведомление не отправлено: {}", order);
        }
    }


    private void simulateNotificationSending(Order order) {
        log.info("Уведомление успешно отправлено пользователю {}: Заказ {} доставлен.", order.getUserId(), order.getId());
    }
}
