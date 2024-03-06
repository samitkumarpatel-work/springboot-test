package com.example.springboottest.repositories;

import com.example.springboottest.models.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long>{
}
