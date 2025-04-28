package org.polina.practice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;
import org.polina.practice.entity.User;
import org.polina.practice.service.OrderService;
import org.polina.practice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long userId,
                                             @RequestBody @Valid Order order) {
        User user = userService.getUserById(userId);
        Order createdOrder = orderService.createOrder(user.getId(), order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long userId,
                                             @RequestBody @Valid Order order) {
        User user = userService.getUserById(userId);
        Order updatedOrder = orderService.updateOrder(user.getId(), order);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
    }
    @PostMapping("/{orderId}")
    public ResponseEntity<Order> addProductToOrder(@PathVariable Long orderId,
                                                   @RequestBody @Valid Product product) {
        Order updatedOrder = orderService.addProductToOrder(orderId, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
    }
    @DeleteMapping("{orderId}")
    public ResponseEntity<Order> removeProductFromOrder(@PathVariable Long orderId,
                                                        @RequestBody @Valid Product product) {
        Order updatedOrder = orderService.removeProductFromOrder(orderId, product);
        return ResponseEntity.ok(updatedOrder);
    }
    @GetMapping("/{id}")
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
