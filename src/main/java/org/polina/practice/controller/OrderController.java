package org.polina.practice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.User;
import org.polina.practice.service.OrderService;
import org.polina.practice.service.UserService;
import org.polina.practice.views.Views;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/userId/{userId}")
    @JsonView(Views.OrderSummary.class)
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody @Valid Order order) {
        User user = userService.getUserById(userId);
        Order createdOrder = orderService.createOrder(user.getId(), order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    @JsonView(Views.OrderSummary.class)
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                             @RequestBody @Valid Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
    }

    @GetMapping("/{id}")
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
