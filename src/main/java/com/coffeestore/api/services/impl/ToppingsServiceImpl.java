package com.coffeestore.api.services.impl;

import com.coffeestore.api.dto.ToppingCreateRequestDto;
import com.coffeestore.api.dto.ToppingUpdateRequestDto;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.repositories.ToppingsRepository;
import com.coffeestore.api.services.ToppingsService;
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
public class ToppingsServiceImpl implements ToppingsService {
    private final ToppingsRepository toppingsRepository;

    @Override
    public Page<Topping> findAllToppings(Pageable pageable) {
        return toppingsRepository.findAll(pageable);
    }

    @Override
    public Optional<Topping> findOneTopping(long id) {
        return toppingsRepository.findById(id);
    }

    @Override
    public Topping createTopping(ToppingCreateRequestDto createRequest) {
        if (toppingsRepository.existsByName(createRequest.getName())) {
            throw new RestApiConstraintViolationException(
                String.format("Topping called %s already exists", createRequest.getName()));
        }
        Topping topping = Topping.builder()
            .name(createRequest.getName())
            .price(createRequest.getPrice())
            .build();
        topping = toppingsRepository.save(topping);
        log.info("New topping {} was successfully created", topping);
        return topping;
    }

    @Override
    public Optional<Topping> updateTopping(long id, ToppingUpdateRequestDto updateRequestDto) {
        if (toppingsRepository.existsByNameAndIdNot(updateRequestDto.getName(), id)) {
            throw new RestApiConstraintViolationException(
                String.format("Name %s already used for another topping", updateRequestDto.getName()));
        }
        return toppingsRepository.findById(id).map(topping -> {
            if (updateRequestDto.getName() != null) {
                topping.setName(updateRequestDto.getName());
            }
            if (updateRequestDto.getPrice() != null) {
                topping.setPrice(updateRequestDto.getPrice());
            }
            Topping updatedTopping = toppingsRepository.save(topping);
            log.info("Topping {} was successfully updated", updatedTopping);
            return updatedTopping;
        });
    }

    @Override
    public void deleteTopping(long id) {
        toppingsRepository.deleteById(id);
    }
}
