package com.coffeestore.api.controllers;

import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.repositories.BeverageRepository;
import com.coffeestore.api.repositories.BeverageToppingsRepository;
import com.coffeestore.api.repositories.CartRepository;
import com.coffeestore.api.repositories.DrinksRepository;

import com.coffeestore.api.repositories.OrderHistoryRepository;
import com.coffeestore.api.repositories.OrderLinesRepository;
import com.coffeestore.api.repositories.OrdersRepository;
import com.coffeestore.api.repositories.ToppingsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class OrdersControllerTest {
    @Autowired
    private DrinksRepository drinksRepository;
    @Autowired
    private ToppingsRepository toppingsRepository;
    @Autowired
    private BeverageRepository beverageRepository;
    @Autowired
    private BeverageToppingsRepository beverageToppingsRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderLinesRepository orderLinesRepository;
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    @Autowired
    private MockMvc mockMvc;

    private Drink drink1, drink2, drink3, drink4;
    private Topping topping1, topping2, topping3, topping4;
    private Beverage beverage1, beverage2;

    @BeforeEach
    public void setUp() {
        drinksRepository.deleteAll();
        toppingsRepository.deleteAll();

        drink1 = drinksRepository.save(Drink.builder().name("Black Coffee").price(4.0).build());
        drink2 = drinksRepository.save(Drink.builder().name("Latte").price(5.0).build());
        drink3 = drinksRepository.save(Drink.builder().name("Mocha").price(6.0).build());
        drink4 = drinksRepository.save(Drink.builder().name("Tea").price(3.0).build());

        topping1 = toppingsRepository.save(Topping.builder().name("Milk").price(2.0).build());
        topping2 = toppingsRepository.save(Topping.builder().name("Hazelnut syrup").price(3.0).build());
        topping3 = toppingsRepository.save(Topping.builder().name("Chocolate sauce").price(5.0).build());
        topping4 = toppingsRepository.save(Topping.builder().name("Lemon").price(2.0).build());

        beverage1 = beverageRepository.save(Beverage.builder().hash(123L).drink(drink1).toppings(List.of(topping1, topping3)).build());
        beverage2 = beverageRepository.save(Beverage.builder().hash(234L).drink(drink4).toppings(List.of(topping4)).build());

        cartRepository.save(CartItem.builder().userId(1L).price(11.0).beverage(beverage1).build());
        cartRepository.save(CartItem.builder().userId(1L).price(5.0).beverage(beverage2).build());
    }

    @AfterEach
    public void tearDown() {
        orderHistoryRepository.deleteAll();
        ordersRepository.deleteAll();
        orderLinesRepository.deleteAll();
        cartRepository.deleteAll();
        beverageToppingsRepository.deleteAll();
        beverageRepository.deleteAll();
        drinksRepository.deleteAll();
        toppingsRepository.deleteAll();
    }

    @Test
    public void testPlaceOrder() throws Exception {
        mockMvc.perform(post("/order")
                .param("userId", "1")
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId", is(1)))
            .andExpect(jsonPath("$.totalPrice", is(16.0)))
            .andExpect(jsonPath("$.totalPriceWithDiscount", is(12.0)));
    }

    @Test
    public void testPlaceOrderNoItemsInTheCart() throws Exception {
        cartRepository.deleteAll();
        mockMvc.perform(post("/order")
                .param("userId", "1")
            )
            .andExpect(status().isBadRequest());
    }
}
