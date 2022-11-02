package com.coffeestore.api.services.impl;

import com.coffeestore.api.dto.MostUsedToppingsReportDto;
import com.coffeestore.api.repositories.OrderHistoryRepository;
import com.coffeestore.api.services.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {
    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    public Page<MostUsedToppingsReportDto> findMostUsedToppings(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        return orderHistoryRepository.getMostUsedToppings(startDateTime, endDateTime, pageable);
    }
}
