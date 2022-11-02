package com.coffeestore.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class BeverageAddRequestDto {
    private Long drinkId;
    private List<Long> toppingIds;
}
