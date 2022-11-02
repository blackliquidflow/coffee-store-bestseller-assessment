package com.coffeestore.api.services.impl;

import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.services.PriceAndDiscountsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAndDiscountsServiceImpl implements PriceAndDiscountsService {
    //TODO - promotion values hardcoded to simplify implementation. Should be moved to separate database tables
    private final static double THRESHOLD_PROMOTION_DISCOUNT = 0.25;
    private final static int THRESHOLD_PROMOTION_LIMIT = 12;
    private final static int ONE_FOR_FREE_PROMOTION_LIMIT = 3;

    @Override
    public Double calculateTotalPriceForCartItems(List<CartItem> cartItems) {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getPrice();
        }
        return totalPrice;
    }

    @Override
    public Double calculatePriceWithDiscountForCart(List<CartItem> cartItems, Double totalPrice) {
        Double thresholdDiscountedPrice = getPriceWithThresholdDiscount(totalPrice);
        Double oneForFreeDiscountedPrice = getPriceWithOneForFreeDiscount(cartItems, totalPrice);
        return thresholdDiscountedPrice <= oneForFreeDiscountedPrice ?
            thresholdDiscountedPrice : oneForFreeDiscountedPrice;
    }

    private Double getPriceWithThresholdDiscount(Double totalPrice) {
        if (totalPrice == null) {
            return totalPrice;
        }

        if (totalPrice > THRESHOLD_PROMOTION_LIMIT) {
            return Math.round(totalPrice * (1 - THRESHOLD_PROMOTION_DISCOUNT) * 100.0) / 100.0;
        }
        return totalPrice;
    }

    private Double getPriceWithOneForFreeDiscount(List<CartItem> cartItems, Double totalPrice) {
        if (totalPrice == null) {
            return totalPrice;
        }

        if (cartItems != null && cartItems.size() >= ONE_FOR_FREE_PROMOTION_LIMIT) {
            Double minPrice = null;
            for (CartItem cartItem : cartItems) {
                if (minPrice == null || cartItem.getPrice() < minPrice) {
                    minPrice = cartItem.getPrice();
                }
            }
            totalPrice -= minPrice;
        }
        return totalPrice;
    }

    @Override
    public Double calculateBeveragePrice(Beverage beverage) {
        Double price = 0.0;
        if (beverage.getDrink() != null) {
            price += beverage.getDrink().getPrice();
        }
        for (Topping topping : beverage.getToppings()) {
            price += topping.getPrice();
        }
        return price;
    }
}
