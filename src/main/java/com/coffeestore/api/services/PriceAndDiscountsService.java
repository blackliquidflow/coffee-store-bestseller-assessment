package com.coffeestore.api.services;

import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.CartItem;

import java.util.List;

/**
 * Service for calculating prices and discounts
 */
public interface PriceAndDiscountsService {
    /**
     * Calculate total price for all {@link CartItem} objects passed as a parameter
     *
     * @param cartItems list of {@link CartItem} objects
     * @return aggregated Total Price of all Items in Shopping Cart
     */
    Double calculateTotalPriceForCartItems(List<CartItem> cartItems);

    /**
     * Calculate total price with discount for all {@link CartItem} objects passed as a parameter
     *
     * @param cartItems  list of {@link CartItem} objects
     * @param totalPrice total price of {@link CartItem} objects
     * @return aggregated Total Price with applied Discount of all Items in Shopping Cart
     */
    Double calculatePriceWithDiscountForCart(List<CartItem> cartItems, Double totalPrice);

    /**
     * Calculate price of {@link Beverage} object
     *
     * @param beverage {@link Beverage} object representing Drink with Toppings
     * @return aggregated Total Price of {@link Beverage}
     */
    Double calculateBeveragePrice(Beverage beverage);
}
