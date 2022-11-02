package com.coffeestore.api.services;

import com.coffeestore.api.dto.BeverageAddRequestDto;
import com.coffeestore.api.models.Beverage;

import java.util.List;

/**
 * Service for managing {@link Beverage} objects representing Drink with Toppings
 */
public interface BeverageService {
    /**
     * Hash of the Beverage that is used to quickly find Beverage in the database
     *
     * @param drinkId    ID of the {@link com.coffeestore.api.models.Drink} object
     * @param toppingIds list of IDs of the {@link com.coffeestore.api.models.Topping} objects
     * @return Hash value
     */
    Long calculateHash(Long drinkId, List<Long> toppingIds);

    /**
     * Executes check if the Beverage being created is equal to {@link Beverage} object
     * that already exists. If two Beverages have the same Hash it gives a good chance that these two
     * Beverages are exactly the same. But additional validation is required in order to check it on ingredients level.
     *
     * @param addRequest {@link BeverageAddRequestDto} object representing new Beverage being
     *                   created
     * @param beverage   {@link Beverage} object representing already existing Beverage
     * @return true if the contents of two beverages is equal and false otherwise
     */
    boolean addRequestMatchesExisting(BeverageAddRequestDto addRequest, Beverage beverage);

    /**
     * Create new Beverage from {@link BeverageAddRequestDto} object
     *
     * @param addRequest   {@link BeverageAddRequestDto} object representing Beverage being
     *                     created
     * @param beverageHash Hash of the Beverage being created
     * @return created {@link Beverage}
     */
    Beverage createBeverage(BeverageAddRequestDto addRequest, Long beverageHash);
}
