package com.coffeestore.api.dto;

import com.coffeestore.api.models.CartItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartDto {
    private List<CartItem> cartItems;
    private Double totalPrice;
    private Double priceWithDiscount;
}
