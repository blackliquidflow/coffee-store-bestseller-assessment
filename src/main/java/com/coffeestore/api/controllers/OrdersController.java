package com.coffeestore.api.controllers;

import com.coffeestore.api.models.Order;
import com.coffeestore.api.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * REST Controller for placing Orders
 */
@RequestMapping("/order")
@RestController
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;

    /**
     * Place an order for all the Beverages currently present in the Shopping Cart. If order is created successfully,
     * {@link com.coffeestore.api.models.OrderHistoryLine} is also created for each ingredient of each beverage that
     * exists in the order. After {@link Order} is created, Shopping Cart becomes empty and HTTP status 201 is returned
     * to user. If no items is present in Shopping Cart when trying to create an order, HTTP status 400 is returned.
     *
     * @param userId ID of the user who owns Shopping Cart
     * @return response containing created Order or empty response if no Order was created
     */
    @Transactional
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam long userId) {
        Optional<Order> orderOptional = orderService.createNewOrder(userId);
        return orderOptional
            .map(order -> new ResponseEntity<>(order, HttpStatus.CREATED))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
