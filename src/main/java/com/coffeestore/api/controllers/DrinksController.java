package com.coffeestore.api.controllers;

import com.coffeestore.api.dto.DrinkCreateRequestDto;
import com.coffeestore.api.dto.DrinkUpdateRequestDto;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.services.DrinksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST Controller for fetching, creating, updating and deleting {@link Drink} objects
 */
@RequestMapping("/drinks")
@RestController
@RequiredArgsConstructor
public class DrinksController {
    private final DrinksService drinksService;

    /**
     * Get all Drinks with standard pagination functionality
     *
     * @param pageable standard Spring {@link Pageable} object
     * @return Page with Drinks
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<Drink> getAll(Pageable pageable) {
        return drinksService.findAllDrinks(pageable);
    }

    /**
     * Get one Drink by ID
     *
     * @param id An id of the Drink
     * @return one Drink
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Drink> getOne(@PathVariable long id) {
        Optional<Drink> drinkOptional = drinksService.findOneDrink(id);
        return drinkOptional
            .map(drink -> new ResponseEntity<>(drink, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Create Drink from json request data
     *
     * @param createRequest json request data containing field values for new Drink
     * @return json response with newly created Drink
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Drink createOne(@Valid @RequestBody DrinkCreateRequestDto createRequest) {
        return drinksService.createDrink(createRequest);
    }

    /**
     * Update Drink from json request data
     *
     * @param id An ID of the Drink
     * @param body json request data containing field values for Drink being updated.
     *             Can contain only some of the Drink's fields. In that case only those fields will be updated
     * @return json response with updated Drink
     */
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Drink> updateOne(@PathVariable long id,
                                           @Valid @RequestBody DrinkUpdateRequestDto body) {
        Optional<Drink> drinkOptional = drinksService.updateDrink(id, body);
        return drinkOptional
            .map(drink -> new ResponseEntity<>(drink, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete Drink by its ID
     *
     * @param id An ID of the Drink to be deleted
     * @return If no Drink with provided ID exist, HTTP status 404 will be returned. If Drink was successfully
     * deleted, then HTTP status 204 returned.
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable long id) {
        if (drinksService.findOneDrink(id).isPresent()) {
            drinksService.deleteDrink(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
