package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;
import org.polina.practice.entity.Status;
import org.polina.practice.exception.OrderNotFoundException;
import org.polina.practice.exception.ProductNotFoundException;
import org.polina.practice.repository.OrderRepository;
import org.polina.practice.repository.ProductRepository;
import org.polina.practice.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException(MessageFormat
                        .format("Заказ с ID {0} не найден!", orderId)));
    }

    @Override
    @Transactional
    public Order createOrder(Long userId, Order order) {
        order.setStatus(Status.PAID);
        order.setTotalPrice(BigDecimal.ZERO);
        return orderRepository.save(order);
    }
    @Override
    @Transactional
    public Order updateOrder(Long userId, Order order) {
        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow(()->
                new OrderNotFoundException(MessageFormat.format("Товар с id {0} не найден!",
                        order.getId())));
        updatedOrder.setStatus(order.getStatus());
        return orderRepository.save(updatedOrder);
    }

    public Order addProductToOrder(Long orderId, Product product) {
        Order currentOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        MessageFormat.format("Заказ с ID {0} не найден!", orderId)));

        Product currentProduct = productRepository.findByName(product.getName())
                .orElseThrow(() -> new ProductNotFoundException(
                        MessageFormat.format("Продукт с названием {0} не найден!", product.getName())));

        currentOrder.getProducts().add(currentProduct);
        currentOrder.setTotalPrice(currentOrder.getTotalPrice().add(currentProduct.getPrice()));
        return orderRepository.save(currentOrder);
    }

    public Order removeProductFromOrder(Long orderId, Product product) {
        Order currentOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        MessageFormat.format("Заказ с ID {0} не найден!", orderId)));

        Product currentProduct = productRepository.findByName(product.getName())
                .orElseThrow(() -> new ProductNotFoundException(
                        MessageFormat.format("Продукт с названием {0} не найден!", product.getName())));

        currentOrder.getProducts().remove(currentProduct);
        currentOrder.setTotalPrice(currentOrder.getTotalPrice().subtract(currentProduct.getPrice()));
        return orderRepository.save(currentOrder);

    }


    @Override
    @Transactional
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}