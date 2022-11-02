package com.coffeestore.api.repositories;

import com.coffeestore.api.models.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinksRepository extends JpaRepository<Drink, Long> {
    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByName(String name);
}
