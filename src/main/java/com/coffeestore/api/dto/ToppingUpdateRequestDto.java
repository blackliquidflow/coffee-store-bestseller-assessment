package com.coffeestore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ToppingUpdateRequestDto {
    @Size(max = 50)
    private String name;
    private Double price;
}
