package com.coffeestore.api.services.impl;

import com.coffeestore.api.dto.DrinkCreateRequestDto;
import com.coffeestore.api.dto.DrinkUpdateRequestDto;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.repositories.DrinksRepository;
import com.coffeestore.api.services.DrinksService;
import com.coffeestore.exceptions.RestApiConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class DrinksServiceImpl implements DrinksService {
    private final DrinksRepository drinksRepository;

    @Override
    public Page<Drink> findAllDrinks(Pageable pageable) {
        return drinksRepository.findAll(pageable);
    }

    @Override
    public Optional<Drink> findOneDrink(long id) {
        return drinksRepository.findById(id);
    }

    @Override
    public Drink createDrink(DrinkCreateRequestDto createRequest) {
        if (drinksRepository.existsByName(createRequest.getName())) {
            throw new RestApiConstraintViolationException(
                String.format("Drink called %s already exists", createRequest.getName()));
        }
        Drink drink = Drink.builder()
            .name(createRequest.getName())
            .price(createRequest.getPrice())
            .build();
        drink = drinksRepository.save(drink);
        log.info("New drink {} was successfully created", drink);
        return drink;
    }

    @Override
    public Optional<Drink> updateDrink(long id, DrinkUpdateRequestDto updateRequestDto) {
        if (drinksRepository.existsByNameAndIdNot(updateRequestDto.getName(), id)) {
            throw new RestApiConstraintViolationException(
                String.format("Name %s already used for another drink", updateRequestDto.getName()));
        }
        return drinksRepository.findById(id).map(drink -> {
            if (updateRequestDto.getName() != null) {
                drink.setName(updateRequestDto.getName());
            }
            if (updateRequestDto.getPrice() != null) {
                drink.setPrice(updateRequestDto.getPrice());
            }
            Drink updatedDrink = drinksRepository.save(drink);
            log.info("Drink {} was successfully updated", updatedDrink);
            return updatedDrink;
        });
    }

    @Override
    public void deleteDrink(long id) {
        drinksRepository.deleteById(id);
    }
}
