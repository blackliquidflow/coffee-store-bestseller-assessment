package com.coffeestore.api.controllers;

import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.repositories.BeverageRepository;
import com.coffeestore.api.repositories.BeverageToppingsRepository;
import com.coffeestore.api.repositories.CartRepository;
import com.coffeestore.api.repositories.DrinksRepository;

import com.coffeestore.api.repositories.ToppingsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {
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
    private MockMvc mockMvc;

    private Drink drink1, drink2, drink3, drink4;
    private Topping topping1, topping2, topping3, topping4;
    private Beverage beverage1, beverage2;
    private CartItem cartItem1, cartItem2;

    @BeforeEach
    public void setUpBeforeEach() {
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

        cartItem1 = cartRepository.save(CartItem.builder().userId(1L).price(11.0).beverage(beverage1).build());
        cartItem2 = cartRepository.save(CartItem.builder().userId(1L).price(5.0).beverage(beverage2).build());
    }

    @AfterEach
    public void tearDown() {
        cartRepository.deleteAll();
        beverageToppingsRepository.deleteAll();
        beverageRepository.deleteAll();
        drinksRepository.deleteAll();
        toppingsRepository.deleteAll();
    }

    @Test
    public void testGetAllCartItems() throws Exception {
        mockMvc.perform(get("/cart")
                .param("userId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalPrice", is(16.0)))
            .andExpect(jsonPath("$.priceWithDiscount", is(12.0)))
            .andExpect(jsonPath("$.cartItems[0].userId", is(1)))
            .andExpect(jsonPath("$.cartItems[0].price", is(11.0)))
            .andExpect(jsonPath("$.cartItems[0].beverage.hash", is(123)))
            .andExpect(jsonPath("$.cartItems[0].beverage.drink.name", is("Black Coffee")))
            .andExpect(jsonPath("$.cartItems[0].beverage.drink.price", is(4.0)))
            .andExpect(jsonPath("$.cartItems[0].beverage.toppings[0].name", is("Milk")))
            .andExpect(jsonPath("$.cartItems[0].beverage.toppings[0].price", is(2.0)))
            .andExpect(jsonPath("$.cartItems[0].beverage.toppings[1].name", is("Chocolate sauce")))
            .andExpect(jsonPath("$.cartItems[0].beverage.toppings[1].price", is(5.0)))
            .andExpect(jsonPath("$.cartItems[1].userId", is(1)))
            .andExpect(jsonPath("$.cartItems[1].price", is(5.0)))
            .andExpect(jsonPath("$.cartItems[1].beverage.hash", is(234)))
            .andExpect(jsonPath("$.cartItems[1].beverage.drink.name", is("Tea")))
            .andExpect(jsonPath("$.cartItems[1].beverage.drink.price", is(3.0)))
            .andExpect(jsonPath("$.cartItems[1].beverage.toppings[0].name", is("Lemon")))
            .andExpect(jsonPath("$.cartItems[1].beverage.toppings[0].price", is(2.0)));
    }

    @Test
    public void testAddCartItem() throws Exception {
        mockMvc.perform(post("/cart")
                .param("userId", "1")
                .content("{\n" +
                    "\"drinkId\": " + drink3.getId() + ",\n" +
                    "\"toppingIds\": [" + topping2.getId() + "," + topping3.getId() + "]\n" +
                    "}")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId", is(1)))
            .andExpect(jsonPath("$.price", is(14.0)))
            .andExpect(jsonPath("$.beverage.drink.name", is("Mocha")))
            .andExpect(jsonPath("$.beverage.drink.price", is(6.0)))
            .andExpect(jsonPath("$.beverage.toppings[0].name", is("Hazelnut syrup")))
            .andExpect(jsonPath("$.beverage.toppings[0].price", is(3.0)))
            .andExpect(jsonPath("$.beverage.toppings[1].name", is("Chocolate sauce")))
            .andExpect(jsonPath("$.beverage.toppings[1].price", is(5.0)));
    }

    @Test
    public void testAddCartItemEmptyRequest() throws Exception {
        mockMvc.perform(post("/cart")
                .param("userId", "1")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/cart/" + cartItem1.getId())
                .param("userId", "1")
            )
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCartItemNotFound() throws Exception {
        cartRepository.deleteById(cartItem2.getId());
        mockMvc.perform(delete("/cart/" + cartItem2.getId())
                .param("userId", "1")
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void testClearCart() throws Exception {
        mockMvc.perform(delete("/cart")
                .param("userId", "1")
            )
            .andExpect(status().isNoContent());
    }
}
