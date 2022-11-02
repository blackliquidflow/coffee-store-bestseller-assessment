package com.coffeestore.api.controllers;

import com.coffeestore.api.dto.MostUsedToppingsReportDto;
import com.coffeestore.api.services.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST Controller for getting reports data based on Orders History
 */
@RequestMapping("/reports")
@RestController
@RequiredArgsConstructor
public class ReportsController {
    private final ReportsService reportsService;

    /**
     * Get the Most Used Toppings for specified time period
     *
     * @param startDate Beginning of the time interval for search. If not specified then (Today's Date - 3 months)
     *                  is used.
     * @param endDate   End of the time interval for search. If not specified then (Today's Date) is used.
     * @param pageable  standard Spring {@link Pageable} object
     * @return {@link Page} containing {@link MostUsedToppingsReportDto} with aggregated data of Toppings usage numbers
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<MostUsedToppingsReportDto> getMostUsedToppings(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable) {
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        return reportsService.findMostUsedToppings(startDate, endDate, pageable);
    }
}
