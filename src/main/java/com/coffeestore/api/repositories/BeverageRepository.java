package com.coffeestore.api.repositories;

import com.coffeestore.api.models.Beverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeverageRepository extends JpaRepository<Beverage, Long> {
    List<Beverage> findAllByHash(Long hash);
}
