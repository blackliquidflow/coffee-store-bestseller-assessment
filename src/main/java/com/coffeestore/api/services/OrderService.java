package com.coffeestore.api.services;

import com.coffeestore.api.models.Order;

import java.util.Optional;

public interface OrderService {
    /**
     * Create an order for all the Beverages currently present in the Shopping Cart. If Shopping Cart is empty,
     * order will not be created.
     *
     * @param userId ID of the user who owns Shopping Cart
     * @return {@link Optional} object that contains Order if the one was created
     */
    Optional<Order> createNewOrder(Long userId);
}
