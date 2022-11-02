package com.coffeestore.api.controllers;

import com.coffeestore.api.dto.BeverageAddRequestDto;
import com.coffeestore.api.dto.CartDto;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * REST Controller for managing Shopping Cart
 */
@RequestMapping("/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * Get the contents of the Shopping Cart
     *
     * @param userId ID of the user who owns Shopping Cart
     * @return {@link CartDto} object representing the contents of the Shopping Cart
     * It contains the list of {@link CartItem} objects representing individual items
     * in the Cart as well as Total Price of all items in the Cart and Total Price With Applied Discounts
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CartDto getAllCartItems(@RequestParam long userId) {
        return cartService.getCartContents(userId);
    }

    /**
     * Add new item in the Shopping Cart
     *
     * @param userId     ID of the user who owns Shopping Cart
     * @param addRequest {@link BeverageAddRequestDto} object that contains the ID of the Drink
     *                   and ID's of the Toppings
     * @return {@link CartItem} object that contains the information about the item added
     * to Shopping Cart
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CartItem addItemToCart(@RequestParam long userId,
                                  @RequestBody BeverageAddRequestDto addRequest) {
        return cartService.addBeverageToCart(userId, addRequest);
    }

    /**
     * Delete one item from Shopping Cart
     *
     * @param id     ID of the Cart Item
     * @param userId ID of the user who owns Shopping Cart
     * @return HTTP status 204 if Item was deleted from the Cart and Http status 404 if Item was not found
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemFromCart(@PathVariable long id,
                                                   @RequestParam long userId) {
        if (cartService.cartItemExists(id, userId)) {
            cartService.deleteCartItem(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Clear the contents of Shopping Cart by deleting all the Items that were present in it
     *
     * @param userId ID of the user who owns Shopping Cart
     */
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void clearCart(@RequestParam long userId) {
        cartService.clearCart(userId);
    }
}
