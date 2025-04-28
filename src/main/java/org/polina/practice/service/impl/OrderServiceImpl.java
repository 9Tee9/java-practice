package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;
import org.polina.practice.entity.User;
import org.polina.practice.exception.OrderNotFoundException;
import org.polina.practice.exception.ProductNotFoundException;
import org.polina.practice.exception.UserNotFoundException;
import org.polina.practice.repository.OrderRepository;
import org.polina.practice.repository.ProductRepository;
import org.polina.practice.repository.UserRepository;
import org.polina.practice.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat
                        .format("Пользователь с id {0} не найден!", userId)));

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus(order.getStatus());

        List<Product> products = order.getProducts().stream()
                .map(product -> productRepository.findById(product.getId())
                        .orElseThrow(() -> new ProductNotFoundException(MessageFormat
                                .format("Товар с id {0} найден!", product.getId()))))
                .collect(Collectors.toList());

        newOrder.setProducts(products);

        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newOrder.setTotalPrice(totalPrice);

        return orderRepository.save(newOrder);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order updatedOrder = orderRepository.findById(id).orElseThrow(()->
                new OrderNotFoundException(MessageFormat
                        .format("Товар с id {0} не найден!", id)));
        updatedOrder.setStatus(order.getStatus());
        return orderRepository.save(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()->
                        new OrderNotFoundException(MessageFormat
                                .format("Товар с id {0} не найден!", id)));
        User user = order.getUser();
        user.getOrders().remove(order);
        userRepository.save(user);
        orderRepository.deleteById(id);
    }
}