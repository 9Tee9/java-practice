package org.polina.practice.service;

import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;

public interface OrderService {
    Order createOrder(Long userId, Order order);
    Order updateOrder(Long userId, Order order);
    Order getOrderById(Long id);
    void deleteOrderById(Long orderId);
    Order addProductToOrder(Long orderId, Product product);
    Order removeProductFromOrder(Long orderId, Product product);
}
