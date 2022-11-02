package com.coffeestore.api.repositories;

import com.coffeestore.api.models.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLinesRepository extends JpaRepository<OrderLine, Long> {
}
