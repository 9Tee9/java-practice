package org.polina.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Product;
import org.polina.practice.entity.Status;
import org.polina.practice.entity.User;
import org.polina.practice.service.OrderService;
import org.polina.practice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void whenCreateOrder_thenReturnCreatedOrder() throws Exception {
        Long userId = 1L;
        Order requestOrder = new Order();
        requestOrder.setStatus(Status.ONTHEWAY);
        List<Order> orders = new ArrayList<>();
        orders.add(requestOrder);
        List<Product> products = Arrays.asList(
                new Product(1L, "Laptop", new BigDecimal("1000.00"), orders),
                new Product(6L, "Table", new BigDecimal("2000.00"), orders)
        );
        requestOrder.setProducts(products);

        User user = new User();
        user.setId(userId);

        Order createdOrder = new Order();
        createdOrder.setId(1L);
        createdOrder.setStatus(Status.ONTHEWAY);
        createdOrder.setTotalPrice(new BigDecimal("3000.00"));
        createdOrder.setProducts(products);

        when(userService.getUserById(userId)).thenReturn(user);
        when(orderService.createOrder(userId, requestOrder)).thenReturn(createdOrder);

        mockMvc.perform(post("/api/v1/order/userId/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ONTHEWAY"))
                .andExpect(jsonPath("$.totalPrice").value("3000.0"))
                .andExpect(jsonPath("$.products[0].id").value(1))
                .andExpect(jsonPath("$.products[0].name").value("Laptop"))
                .andExpect(jsonPath("$.products[0].price").value("1000.0"))
                .andExpect(jsonPath("$.products[1].id").value(6))
                .andExpect(jsonPath("$.products[1].name").value("Table"))
                .andExpect(jsonPath("$.products[1].price").value("2000.0"));

        verify(userService, times(1)).getUserById(userId);
        verify(orderService, times(1)).createOrder(userId, requestOrder);
    }

    @Test
    void whenUpdateOrder_thenReturnUpdatedOrder() throws Exception {
        Long orderId = 1L;
        Order requestOrder = new Order();
        requestOrder.setStatus(Status.DELIVERED);

        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus(Status.DELIVERED);
        updatedOrder.setTotalPrice(new BigDecimal("3000.00"));

        when(orderService.updateOrder(orderId, requestOrder)).thenReturn(updatedOrder);

        mockMvc.perform(put("/api/v1/order/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("DELIVERED"))
                .andExpect(jsonPath("$.totalPrice").value("3000.0"));

        verify(orderService, times(1)).updateOrder(orderId, requestOrder);
    }

    @Test
    void whenGetOrderById_thenReturnOrder() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Status.PAID);
        order.setTotalPrice(new BigDecimal("3000.00"));

        when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/api/v1/order/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.totalPrice").value("3000.0"));

        verify(orderService, times(1)).getOrderById(orderId);
    }

    @Test
    void whenDeleteOrderById_thenReturnNoContent() throws Exception {
        Long orderId = 1L;

        doNothing().when(orderService).deleteOrderById(orderId);

        mockMvc.perform(delete("/api/v1/order/{id}", orderId))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrderById(orderId);
    }

}
