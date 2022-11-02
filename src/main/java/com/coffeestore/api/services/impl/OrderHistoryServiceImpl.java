package com.coffeestore.api.services.impl;

import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.models.Order;
import com.coffeestore.api.models.OrderHistoryLine;
import com.coffeestore.api.models.OrderLine;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.repositories.BeverageRepository;
import com.coffeestore.api.repositories.OrderHistoryRepository;
import com.coffeestore.api.services.OrderHistoryService;
import com.coffeestore.enums.IngredientType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private final BeverageRepository beverageRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    public List<OrderHistoryLine> addOrderHistoryRecords(Order order, List<OrderLine> orderLines) {
        List<Long> beverageIds = orderLines.stream().map(OrderLine::getBeverageId).toList();
        Map<Long, Beverage> beverageMap = beverageRepository.findAllById(beverageIds).stream()
            .collect(Collectors.toMap(Beverage::getId, beverage -> beverage));

        List<OrderHistoryLine> orderHistoryLines = new ArrayList<>();
        for (OrderLine orderLine : orderLines) {
            Beverage beverage = beverageMap.get(orderLine.getBeverageId());
            if (beverage.getDrink() != null) {
                Drink drink = beverage.getDrink();
                OrderHistoryLine drinkHistoryRecord =
                    constructOrderHistoryLine(order, orderLine, beverage.getId(), IngredientType.DRINK.toString(),
                        drink.getId(), drink.getName(), drink.getPrice());
                orderHistoryLines.add(drinkHistoryRecord);
            }

            for (Topping topping : beverage.getToppings()) {
                OrderHistoryLine toppingHistoryRecord =
                    constructOrderHistoryLine(order, orderLine, beverage.getId(), IngredientType.TOPPING.toString(),
                        topping.getId(), topping.getName(), topping.getPrice());
                orderHistoryLines.add(toppingHistoryRecord);
            }
        }

        return orderHistoryLines;
    }

    private OrderHistoryLine constructOrderHistoryLine(Order order,
                                                       OrderLine orderLine,
                                                       Long beverageId,
                                                       String ingredientType,
                                                       Long ingredientId,
                                                       String ingredientName,
                                                       Double price) {
        OrderHistoryLine orderHistoryLine = OrderHistoryLine.builder()
            .orderId(order.getId())
            .orderLineId(orderLine.getId())
            .orderDateTime(order.getCreationTime())
            .beverageId(beverageId)
            .ingredientType(ingredientType)
            .ingredientId(ingredientId)
            .ingredientName(ingredientName)
            .price(price)
            .build();
        return orderHistoryRepository.save(orderHistoryLine);
    }
}
