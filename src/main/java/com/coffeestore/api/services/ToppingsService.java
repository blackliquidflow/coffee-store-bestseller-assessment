package com.coffeestore.api.services;

import com.coffeestore.api.dto.ToppingCreateRequestDto;
import com.coffeestore.api.dto.ToppingUpdateRequestDto;
import com.coffeestore.api.models.Topping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service for fetching, creating, updating and deleting {@link Topping} objects
 */
public interface ToppingsService {
    /**
     * Get all Toppings with standard pagination functionality
     *
     * @param pageable standard Spring {@link Pageable} object
     * @return Page with Toppings
     */
    Page<Topping> findAllToppings(Pageable pageable);

    /**
     * Get one Topping by ID
     *
     * @param id An ID of the Topping
     * @return {@link Optional} object with one Topping
     */
    Optional<Topping> findOneTopping(long id);

    /**
     * Create Topping from {@link ToppingCreateRequestDto} request
     *
     * @param createRequest {@link ToppingCreateRequestDto} object containing field values for
     *                      new Topping
     * @return created Topping
     */
    Topping createTopping(ToppingCreateRequestDto createRequest);

    /**
     * Update Topping with {@link ToppingUpdateRequestDto} request
     *
     * @param id               An ID of the Topping
     * @param updateRequestDto {@link ToppingUpdateRequestDto} object containing new field
     *                         values for Topping being updated.
     * @return {@link java.util.Optional} object with updated Topping
     */
    Optional<Topping> updateTopping(long id, ToppingUpdateRequestDto updateRequestDto);

    /**
     * Delete Topping by its ID
     *
     * @param id An ID of the Topping to be deleted
     */
    void deleteTopping(long id);
}
