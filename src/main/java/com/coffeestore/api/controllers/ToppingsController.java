package com.coffeestore.api.controllers;

import com.coffeestore.api.dto.ToppingCreateRequestDto;
import com.coffeestore.api.dto.ToppingUpdateRequestDto;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.services.ToppingsService;
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
 * REST Controller for fetching, creating, updating and deleting {@link Topping} objects
 */
@RequestMapping("/toppings")
@RestController
@RequiredArgsConstructor
public class ToppingsController {
    private final ToppingsService toppingsService;

    /**
     * Get all Toppings with standard pagination functionality
     *
     * @param pageable standard Spring {@link Pageable} object
     * @return Page with Toppings
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<Topping> getAll(Pageable pageable) {
        return toppingsService.findAllToppings(pageable);
    }

    /**
     * Get one Topping by ID
     *
     * @param id An id of the Topping
     * @return one Topping
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Topping> getOne(@PathVariable long id) {
        Optional<Topping> toppingOptional = toppingsService.findOneTopping(id);
        return toppingOptional
            .map(topping -> new ResponseEntity<>(topping, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Create Topping from json request data
     *
     * @param createRequest json request data containing field values for new Topping
     * @return json response with newly created Topping
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Topping createOne(@Valid @RequestBody ToppingCreateRequestDto createRequest) {
        return toppingsService.createTopping(createRequest);
    }

    /**
     * Update Topping from json request data
     *
     * @param id   An ID of the Topping
     * @param body json request data containing field values for Topping being updated.
     *             Can contain only some of the Topping's fields. In that case only those fields will be updated
     * @return json response with updated Topping
     */
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Topping> updateOne(@PathVariable long id,
                                             @Valid @RequestBody ToppingUpdateRequestDto body) {
        Optional<Topping> toppingOptional = toppingsService.updateTopping(id, body);
        return toppingOptional
            .map(topping -> new ResponseEntity<>(topping, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Delete Topping by its ID
     *
     * @param id An ID of the Topping to be deleted
     * @return If no Topping with provided ID exist, HTTP status 404 will be returned. If Topping was successfully
     * deleted, then HTTP status 204 returned.
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable long id) {
        if (toppingsService.findOneTopping(id).isPresent()) {
            toppingsService.deleteTopping(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
