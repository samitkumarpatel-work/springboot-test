package com.example.springboottest.orderservice;

import java.time.LocalDate;
import java.util.List;

public record Order(Long id, LocalDate placedAt, LocalDate expectedDeliveryDate, LocalDate deliveredDate, List<String> items) {

}
