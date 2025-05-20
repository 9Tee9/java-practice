package org.polina.practice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.polina.practice.model.Order;
import org.polina.practice.model.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final KafkaTemplate<String, Order> kafkaTemplate;


    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody @Valid Order order) {
        try {
            log.info("Получен новый заказ: {}", order);

            order.setId(UUID.randomUUID());
            order.setStatus(Status.CREATED);

            kafkaTemplate.send("new_orders", order.getId().toString(), order);

            log.info("Заказ отправлен в Kafka. ID: {}", order.getId());
            return ResponseEntity.ok("Заказ создан и отправлен на обработку.");
        } catch (Exception e) {
            log.error("Ошибка при создании заказа: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании заказа.");
        }
    }
}
