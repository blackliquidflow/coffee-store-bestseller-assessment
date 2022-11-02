package com.coffeestore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MostUsedToppingsReportDto {
    private long numberOfUsages;
    private Long toppingId;
    private String toppingName;
}
