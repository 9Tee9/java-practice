package org.polina.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.dto.UpsertOrderRequest;
import org.polina.practice.entity.Order;
import org.polina.practice.entity.Status;
import org.polina.practice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @Mock
    private Validator validator;

    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        orderController = new OrderController(orderService, objectMapper, validator);
    }
    @Test
    void whenGetAllOrders_thenReturnJsonOrders() throws Exception {
        Order order1 = createOrder(1L, "Address 1", BigDecimal.valueOf(100.0), Status.PAID);
        Order order2 = createOrder(2L, "Address 2", BigDecimal.valueOf(200.0), Status.DELIVERED);

        List<Order> orders = List.of(order1, order2);
        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<String> response = orderController.getAllOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$[0].orderId")).longValue());
        assertEquals("Address 1", jsonContext.read("$[0].shippingAddress"));
        assertEquals(100.0, jsonContext.read("$[0].totalPrice"));
        assertEquals("PAID", jsonContext.read("$[0].orderStatus"));

        assertEquals(2L, ((Number) jsonContext.read("$[1].orderId")).longValue());
        assertEquals("Address 2", jsonContext.read("$[1].shippingAddress"));
        assertEquals(200.0, jsonContext.read("$[1].totalPrice"));
        assertEquals("DELIVERED", jsonContext.read("$[1].orderStatus"));

        verify(orderService, times(1)).getAllOrders();
    }

    private Order createOrder(Long orderId, String shippingAddress, BigDecimal totalPrice, Status orderStatus) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setShippingAddress(shippingAddress);
        order.setTotalPrice(totalPrice);
        order.setOrderStatus(orderStatus);
        return order;
    }
    @Test
    void whenGetOrderById_thenReturnJsonOrder() throws Exception {
        Order order = createOrder(1L, "Address 1", BigDecimal.valueOf(100.0), Status.ONTHEWAY);
        when(orderService.getOrderById(1L)).thenReturn(order);

        ResponseEntity<String> response = orderController.getOrderById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.orderId")).longValue());
        assertEquals("Address 1", jsonContext.read("$.shippingAddress"));
        assertEquals(100.0, jsonContext.read("$.totalPrice"));
        assertEquals("ONTHEWAY", jsonContext.read("$.orderStatus"));

        verify(orderService, times(1)).getOrderById(1L);
    }
    @Test
    void whenCreateOrder_thenReturnJsonCreatedOrder() throws Exception {
        Order createdOrder = createOrder(1L, "Address 1", BigDecimal.valueOf(100.0), Status.PAID);

        String orderJson = objectMapper.writeValueAsString(createdOrder);
        when(orderService.createOrder(eq(1L), any(Order.class))).thenReturn(createdOrder);

        ResponseEntity<String> response = orderController.createOrder(1L, orderJson);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.orderId")).longValue());
        assertEquals("Address 1", jsonContext.read("$.shippingAddress"));
        assertEquals(100.0, jsonContext.read("$.totalPrice"));
        assertEquals("PAID", jsonContext.read("$.orderStatus"));

        verify(orderService, times(1)).createOrder(eq(1L), any(Order.class));
    }
    @Test
    void whenUpdateOrder_thenReturnJsonUpdatedOrder() throws Exception {
        UpsertOrderRequest request = new UpsertOrderRequest();
        request.setOrderStatus(Status.ONTHEWAY);

        Order updatedOrder = createOrder(1L, "Address 1", BigDecimal.valueOf(100.0), Status.ONTHEWAY);
        when(orderService.updateOrder(eq(1L), any(UpsertOrderRequest.class))).thenReturn(updatedOrder);

        ResponseEntity<String> response = orderController.updateOrder(1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.orderId")).longValue());
        assertEquals("Address 1", jsonContext.read("$.shippingAddress"));
        assertEquals(100.0, jsonContext.read("$.totalPrice"));
        assertEquals("ONTHEWAY", jsonContext.read("$.orderStatus"));

        verify(orderService, times(1)).updateOrder(eq(1L), any(UpsertOrderRequest.class));
    }
    @Test
    void whenDeleteOrder_thenReturnNoContent() {
        doNothing().when(orderService).deleteOrderById(1L);

        ResponseEntity<Void> response = orderController.deleteOrder(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(orderService, times(1)).deleteOrderById(1L);
    }
}
