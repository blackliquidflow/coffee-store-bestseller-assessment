package com.coffeestore.api.services;

import com.coffeestore.api.dto.MostUsedToppingsReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Service for providing Reports Data based on Order history
 */
public interface ReportsService {
    /**
     * Get the Most Used Toppings for specified time period
     *
     * @param startDate Beginning of the time interval for search
     * @param endDate   End of the time interval for search
     * @param pageable  standard Spring {@link Pageable} object
     * @return {@link Page} containing {@link MostUsedToppingsReportDto} with aggregated data of Toppings usage numbers
     */
    Page<MostUsedToppingsReportDto> findMostUsedToppings(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
