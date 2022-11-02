package com.coffeestore.api.services;

import com.coffeestore.api.dto.DrinkCreateRequestDto;
import com.coffeestore.api.dto.DrinkUpdateRequestDto;
import com.coffeestore.api.models.Drink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service for fetching, creating, updating and deleting {@link Drink} objects
 */
public interface DrinksService {
    /**
     * Get all Drinks with standard pagination functionality
     *
     * @param pageable standard Spring {@link Pageable} object
     * @return Page with Drinks
     */
    Page<Drink> findAllDrinks(Pageable pageable);

    /**
     * Get one Drink by ID
     *
     * @param id An ID of the Drink
     * @return {@link Optional} object with one Drink
     */
    Optional<Drink> findOneDrink(long id);

    /**
     * Create Drink from {@link DrinkCreateRequestDto} request
     *
     * @param createRequest {@link DrinkCreateRequestDto} object containing field values for
     *                      new Drink
     * @return created Drink
     */
    Drink createDrink(DrinkCreateRequestDto createRequest);

    /**
     * Update Drink with {@link DrinkUpdateRequestDto} request
     *
     * @param id               An ID of the Drink
     * @param updateRequestDto {@link DrinkUpdateRequestDto} object containing new field values
     *                         for Drink being updated.
     * @return {@link java.util.Optional} object with updated Drink
     */
    Optional<Drink> updateDrink(long id, DrinkUpdateRequestDto updateRequestDto);

    /**
     * Delete Drink by its ID
     *
     * @param id An ID of the Drink to be deleted
     */
    void deleteDrink(long id);
}
