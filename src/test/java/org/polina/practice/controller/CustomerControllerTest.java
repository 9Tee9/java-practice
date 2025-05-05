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
import org.polina.practice.dto.UpsertCustomerRequest;
import org.polina.practice.entity.Customer;
import org.polina.practice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Validator validator;

    private CustomerController customerController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService, objectMapper, validator);
        customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setFirstName("New");
        customer1.setLastName("Customer 1");
        customer1.setEmail("newCustomer1@rambler.ru");
        customer1.setContactNumber("89183416972");
        customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setFirstName("New");
        customer2.setLastName("Customer 2");
        customer2.setEmail("newCustomer2@rambler.ru");
        customer2.setContactNumber("89183416973");
    }

    @Test
    void whenGetAllCustomers_thenReturnJsonCustomers() throws Exception {
        List<Customer> customers = List.of(customer1, customer2);
        when(customerService.getAllCustomers()).thenReturn(customers);

        ResponseEntity<String> response = customerController.getAllCustomers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$[0].customerId")).longValue());
        assertEquals("New", jsonContext.read("$[0].firstName"));
        assertEquals("Customer 1", jsonContext.read("$[0].lastName"));
        assertEquals("newCustomer1@rambler.ru", jsonContext.read("$[0].email"));
        assertEquals("89183416972", jsonContext.read("$[0].contactNumber"));
        assertEquals(jsonContext.read("$[0].orders"), Collections.emptyList());

        assertEquals(2L, ((Number) jsonContext.read("$[1].customerId")).longValue());
        assertEquals("New", jsonContext.read("$[1].firstName"));
        assertEquals("Customer 2", jsonContext.read("$[1].lastName"));
        assertEquals("newCustomer2@rambler.ru", jsonContext.read("$[1].email"));
        assertEquals("89183416973", jsonContext.read("$[1].contactNumber"));
        assertEquals(jsonContext.read("$[1].orders"), Collections.emptyList());

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void whenGetCustomerId_thenReturnJsonCustomer() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customer1);

        ResponseEntity<String> response = customerController.getCustomerById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.customerId")).longValue());
        assertEquals("New", jsonContext.read("$.firstName"));
        assertEquals("Customer 1", jsonContext.read("$.lastName"));
        assertEquals("newCustomer1@rambler.ru", jsonContext.read("$.email"));
        assertEquals("89183416972", jsonContext.read("$.contactNumber"));
        assertEquals(jsonContext.read("$.orders"), Collections.emptyList());

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void whenCreateCustomer_thenReturnJsonCreatedCustomer() throws Exception {
        String customerJson = objectMapper.writeValueAsString(customer1);
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer1);

        ResponseEntity<String> response = customerController.createCustomer(customerJson);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.customerId")).longValue());
        assertEquals("New", jsonContext.read("$.firstName"));
        assertEquals("Customer 1", jsonContext.read("$.lastName"));
        assertEquals("newCustomer1@rambler.ru", jsonContext.read("$.email"));
        assertEquals("89183416972", jsonContext.read("$.contactNumber"));
        assertEquals(jsonContext.read("$.orders"), Collections.emptyList());

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    void whenUpdateCustomer_thenReturnJsonUpdatedCustomer() throws Exception {
        UpsertCustomerRequest request = new UpsertCustomerRequest();
        request.setContactNumber("89183416971");
        request.setEmail("updatedEmail@rambler.ru");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(1L);
        updatedCustomer.setFirstName("New");
        updatedCustomer.setLastName("Customer");
        updatedCustomer.setContactNumber("89183416971");
        updatedCustomer.setEmail("updatedEmail@rambler.ru");

        when(customerService.updateCustomer(eq(1L), any(UpsertCustomerRequest.class))).thenReturn(updatedCustomer);

        ResponseEntity<String> response = customerController.updateCustomer(1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();
        assertNotNull(jsonResponse);

        DocumentContext jsonContext = JsonPath.parse(jsonResponse);

        assertEquals(1L, ((Number) jsonContext.read("$.customerId")).longValue());
        assertEquals("New", jsonContext.read("$.firstName"));
        assertEquals("Customer", jsonContext.read("$.lastName"));
        assertEquals("updatedEmail@rambler.ru", jsonContext.read("$.email"));
        assertEquals("89183416971", jsonContext.read("$.contactNumber"));
        assertEquals(jsonContext.read("$.orders"), Collections.emptyList());

        verify(customerService, times(1)).updateCustomer(eq(1L), any(UpsertCustomerRequest.class));
    }

    @Test
    void whenDeleteCustomer_thenReturnNoContent() {
        doNothing().when(customerService).deleteCustomerById(1L);

        ResponseEntity<Void> response = customerController.deleteCustomer(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(customerService, times(1)).deleteCustomerById(1L);
    }
}
