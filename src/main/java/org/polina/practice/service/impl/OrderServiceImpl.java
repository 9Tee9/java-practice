package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.UpsertOrderRequest;
import org.polina.practice.entity.Customer;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;
import org.polina.practice.entity.Status;
import org.polina.practice.exception.CustomerNotFoundException;
import org.polina.practice.exception.OrderNotFoundException;
import org.polina.practice.exception.ProductNotFoundException;
import org.polina.practice.repository.CustomerRepository;
import org.polina.practice.repository.OrderRepository;
import org.polina.practice.repository.ProductRepository;
import org.polina.practice.service.OrderService;
import org.polina.practice.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()->
                new OrderNotFoundException(MessageFormat
                        .format("Заказ с id {0} не найден!", id)));
    }

    @Override
    @Transactional
    //проверить еще раз
    public Order createOrder(Long customerId, Order order) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->
                new CustomerNotFoundException(MessageFormat
                        .format("Покупатель с id {0} не найден!", customerId)));
        Order newOrder = new Order();
        newOrder.setCustomer(customer);
        newOrder.setOrderStatus(Status.PAID);
        newOrder.setShippingAddress(order.getShippingAddress());

        List<Product> products = order.getProducts().stream()
                .map(product -> productRepository.findById(product.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException(MessageFormat
                                .format("Товар с id {0} найден!", product.getProductId()))))
                .collect(Collectors.toList());
        products.forEach(product -> productService.decreaseStock(product.getProductId()));
        newOrder.setProducts(products);

        BigDecimal totalPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newOrder.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, UpsertOrderRequest request) {
        Order updatedOrder = orderRepository.findById(id).orElseThrow(()->
                new OrderNotFoundException(MessageFormat
                        .format("Заказ с id {0} не найден!", id)));
        updatedOrder.setOrderStatus(request.getOrderStatus());
        return orderRepository.save(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()->
                new OrderNotFoundException(MessageFormat
                        .format("Заказ с id {0} не найден!", id)));
        orderRepository.deleteById(order.getOrderId());
    }
}
