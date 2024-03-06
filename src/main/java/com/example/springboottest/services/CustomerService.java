package com.example.springboottest.services;

import com.example.springboottest.models.Customer;
import com.example.springboottest.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    final CustomerRepository customerRepository;
    final OrderService orderService;

    public Mono<Customer> fetchCustomer(Long id) {
        return customerRepository.findById(id)
                .flatMap(customer -> orderService.fetchCustomerOrder(id)
                        .map(order -> {
                            customer.setOrder(order);
                            return customer;
                        })
                );
    }
}
