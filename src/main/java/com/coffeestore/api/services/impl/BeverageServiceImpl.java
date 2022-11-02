package com.coffeestore.api.services.impl;

import com.coffeestore.api.dto.BeverageAddRequestDto;
import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.repositories.BeverageRepository;
import com.coffeestore.api.repositories.DrinksRepository;
import com.coffeestore.api.repositories.ToppingsRepository;
import com.coffeestore.api.services.BeverageService;
import com.coffeestore.exceptions.RestApiInvalidRequestDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BeverageServiceImpl implements BeverageService {
    private final BeverageRepository beverageRepository;
    private final ToppingsRepository toppingsRepository;
    private final DrinksRepository drinksRepository;

    @Override
    public Long calculateHash(Long drinkId, List<Long> toppingIds) {
        //TODO - should be replaced with more complex hash function with better hash values distribution
        Long hash = null;
        if (drinkId != null) {
            hash = drinkId * 59;
        }
        if (toppingIds != null) {
            hash = (hash == null ? 0 : hash);
            for (Long toppingId : toppingIds) {
                hash += (10000 + toppingId * 59);
            }
        }
        return hash == null ? null : hash % Long.MAX_VALUE;
    }

    @Override
    public boolean addRequestMatchesExisting(BeverageAddRequestDto addRequest, Beverage beverage) {
        if ((addRequest.getDrinkId() == null && beverage.getDrink() != null)
            || (addRequest.getDrinkId() != null && beverage.getDrink() == null)
            || (!addRequest.getDrinkId().equals(beverage.getDrink().getId()))
        ) {
            return false;
        }

        List<Long> beverageToppings = beverage.getToppings()
            .stream()
            .map(Topping::getId)
            .sorted()
            .toList();

        List<Long> requestToppings = addRequest.getToppingIds();
        Collections.sort(requestToppings);
        return beverageToppings.equals(requestToppings);
    }

    @Override
    public Beverage createBeverage(BeverageAddRequestDto addRequest, Long beverageHash) {
        Beverage beverage = new Beverage();
        beverage.setHash(beverageHash);

        if (addRequest.getDrinkId() != null) {
            drinksRepository
                .findById(addRequest.getDrinkId())
                .ifPresent(beverage::setDrink);
        }
        List<Long> requestToppings = addRequest.getToppingIds();
        List<Topping> toppings = requestToppings != null ? toppingsRepository.findAllById(requestToppings) : null;

        if (beverage.getDrink() == null && (toppings == null || toppings.isEmpty())) {
            log.warn("{} request for adding Beverage won't be processed as it doesn't have any valid ingredient data", addRequest);
            throw new RestApiInvalidRequestDataException(
                String.format("Request data %s doesn't contain any valid ingredients", addRequest));
        }

        Beverage savedBeverage = beverageRepository.save(beverage);
        log.info("New beverage {} was created", beverage);
        savedBeverage.setToppings(toppings);
        return savedBeverage;
    }
}
