package com.coffeestore.api.repositories;

import com.coffeestore.api.models.BeverageTopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageToppingsRepository extends JpaRepository<BeverageTopping, Long> {
}
