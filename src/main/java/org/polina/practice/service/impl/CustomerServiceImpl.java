package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.UpsertCustomerRequest;
import org.polina.practice.entity.Customer;
import org.polina.practice.exception.CustomerNotFoundException;
import org.polina.practice.repository.CustomerRepository;
import org.polina.practice.service.CustomerService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(()->
                new CustomerNotFoundException(MessageFormat
                        .format("Покупатель с id {0} не найден!", id)));
    }

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer updateCustomer(Long id, UpsertCustomerRequest request) {
        Customer updatedCustomer = customerRepository.findById(id).orElseThrow(()->
                new CustomerNotFoundException(MessageFormat
                        .format("Покупатель с id {0} не найден!", id)));
        if (request.getContactNumber() != null) {
            updatedCustomer.setContactNumber(request.getContactNumber());
        }
        if (request.getEmail() != null) {
            updatedCustomer.setEmail(request.getEmail());
        }
        return customerRepository.save(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()->
                new CustomerNotFoundException(MessageFormat
                        .format("Покупатель с id {0} не найден!", id)));
        customerRepository.deleteById(customer.getCustomerId());
    }
}
