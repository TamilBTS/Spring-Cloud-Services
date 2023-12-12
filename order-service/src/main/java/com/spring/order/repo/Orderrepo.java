package com.spring.order.repo;

import com.spring.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Orderrepo extends JpaRepository<Order, String> {
}
