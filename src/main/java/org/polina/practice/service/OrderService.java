package org.polina.practice.service;

import org.polina.practice.entity.Order;

public interface OrderService {
    Order createOrder(Long userId, Order order);
    Order updateOrder(Long userId, Order order);
    Order getOrderById(Long id);
    void deleteOrderById(Long orderId);

}
