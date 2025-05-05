package org.polina.practice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.polina.practice.dto.UpsertOrderRequest;
import org.polina.practice.entity.Customer;
import org.polina.practice.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders() throws JsonProcessingException;
    Order getOrderById(Long id) throws JsonProcessingException;
    Order createOrder(Long customerId, Order order);
    Order updateOrder(Long id, UpsertOrderRequest request);
    void deleteOrderById(Long id);
}
