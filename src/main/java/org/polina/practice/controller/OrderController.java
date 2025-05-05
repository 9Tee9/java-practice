package org.polina.practice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.UpsertOrderRequest;
import org.polina.practice.entity.Order;
import org.polina.practice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @GetMapping("/all")
    public ResponseEntity<String> getAllOrders() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(orderService.getAllOrders()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(orderService.getOrderById(id)));
    }
    @PostMapping("/{customerId}")
    public ResponseEntity<String> createOrder(@PathVariable Long customerId, @RequestBody String orderJson) throws JsonProcessingException {
        Order order = objectMapper.readValue(orderJson, Order.class);
        validator.validate(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper
                .writeValueAsString(orderService.createOrder(customerId, order)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody @Valid UpsertOrderRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(orderService.updateOrder(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}

