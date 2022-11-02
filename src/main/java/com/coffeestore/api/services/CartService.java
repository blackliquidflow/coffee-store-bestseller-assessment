package com.coffeestore.api.services;

import com.coffeestore.api.dto.BeverageAddRequestDto;
import com.coffeestore.api.dto.CartDto;
import com.coffeestore.api.models.CartItem;

/**
 * Service for managing Shopping Cart
 */
public interface CartService {
    /**
     * Get the contents of the Shopping Cart
     *
     * @param userId ID of the user who owns Shopping Cart
     * @return {@link CartDto} object representing the contents of the Shopping Cart
     * It contains the list of {@link CartItem} objects representing individual items
     * in the Cart as well as Total Price of all items in the Cart and Total Price With Applied Discounts
     */
    CartDto getCartContents(Long userId);

    /**
     * Add new item in the Shopping Cart
     *
     * @param userId     ID of the user who owns Shopping Cart
     * @param addRequest {@link BeverageAddRequestDto} object that contains the ID of the Drink
     *                   and ID's of the Toppings
     * @return {@link CartItem} object that contains the information about the item added
     * to Shopping Cart
     */
    CartItem addBeverageToCart(Long userId, BeverageAddRequestDto addRequest);

    /**
     * Check if the Cart Item exists for the given User
     *
     * @param id     ID of the Cart Item
     * @param userId ID of the user who owns Shopping Cart
     * @return True if Cart Item exists and false otherwise
     */
    boolean cartItemExists(Long id, Long userId);

    /**
     * Delete one item from Shopping Cart
     *
     * @param id     ID of the Cart Item
     * @param userId ID of the user who owns Shopping Cart
     */
    void deleteCartItem(Long id, Long userId);

    /**
     * Clear the contents of Shopping Cart by deleting all the Items that were present in it
     *
     * @param userId ID of the user who owns Shopping Cart
     */
    void clearCart(Long userId);
}
