package com.example.outsourcingproject.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcingproject.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
