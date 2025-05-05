package org.polina.practice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.UpsertCustomerRequest;
import org.polina.practice.entity.Customer;
import org.polina.practice.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @GetMapping("/all")
    public ResponseEntity<String> getAllCustomers() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(customerService.getAllCustomers()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getCustomerById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(customerService.getCustomerById(id)));
    }
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody String customerJson) throws JsonProcessingException {
        Customer customer = objectMapper.readValue(customerJson, Customer.class);
        validator.validate(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(objectMapper.writeValueAsString(customerService.createCustomer(customer)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody @Valid UpsertCustomerRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(customerService.updateCustomer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }
}
