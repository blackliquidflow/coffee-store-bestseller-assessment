package com.coffeestore.api.repositories;

import com.coffeestore.api.dto.MostUsedToppingsReportDto;
import com.coffeestore.api.models.OrderHistoryLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryLine, Long> {
    @Query("select new com.coffeestore.api.dto.MostUsedToppingsReportDto( " +
        "count(*) as numberOfUsages, " +
        "ohl.ingredientId as toppingId, " +
        "ohl.ingredientName as toppingName) " +
        "from OrderHistoryLine ohl " +
        "where ohl.ingredientType = 'TOPPING' and ohl.orderDateTime >= ?1 and ohl.orderDateTime < ?2 " +
        "group by ohl.ingredientId, ohl.ingredientName " +
        "order by numberOfUsages desc")
    Page<MostUsedToppingsReportDto> getMostUsedToppings(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
