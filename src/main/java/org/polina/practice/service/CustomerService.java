package org.polina.practice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.polina.practice.dto.UpsertCustomerRequest;
import org.polina.practice.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers() throws JsonProcessingException;
    Customer getCustomerById(Long id) throws JsonProcessingException;
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Long id, UpsertCustomerRequest request);
    void deleteCustomerById(Long id);
}
