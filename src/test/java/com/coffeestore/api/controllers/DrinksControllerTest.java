package com.coffeestore.api.controllers;

import com.coffeestore.api.models.Drink;
import com.coffeestore.api.repositories.DrinksRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class DrinksControllerTest {
    @Autowired
    private DrinksRepository drinksRepository;
    @Autowired
    private MockMvc mockMvc;

    private Drink drink1, drink2, drink3, drink4;

    @BeforeEach
    public void setUp() {
        drinksRepository.deleteAll();
        drink1 = drinksRepository.save(Drink.builder().name("Black Coffee").price(4.0).build());
        drink2 = drinksRepository.save(Drink.builder().name("Latte").price(5.0).build());
        drink3 = drinksRepository.save(Drink.builder().name("Mocha").price(6.0).build());
        drink4 = drinksRepository.save(Drink.builder().name("Tea").price(3.0).build());
    }

    @AfterEach
    public void tearDown() {
        drinksRepository.deleteAll();
    }

    @Test
    public void testGetAllDrinks() throws Exception {
        mockMvc.perform(get("/drinks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(4)))
            .andExpect(jsonPath("$.content[0].name", is("Black Coffee")))
            .andExpect(jsonPath("$.content[0].price", is(4.0)))
            .andExpect(jsonPath("$.content[1].name", is("Latte")))
            .andExpect(jsonPath("$.content[1].price", is(5.0)))
            .andExpect(jsonPath("$.content[2].name", is("Mocha")))
            .andExpect(jsonPath("$.content[2].price", is(6.0)))
            .andExpect(jsonPath("$.content[3].name", is("Tea")))
            .andExpect(jsonPath("$.content[3].price", is(3.0)));
    }

    @Test
    public void testGetOneDrink() throws Exception {
        mockMvc.perform(get("/drinks/{id}", drink2.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(drink2.getId().intValue())))
            .andExpect(jsonPath("$.name", is("Latte")))
            .andExpect(jsonPath("$.price", is(5.0)));
    }

    @Test
    public void testGetOneDrinkNotFound() throws Exception {
        drinksRepository.deleteById(drink3.getId());
        mockMvc.perform(get("/drinks/{id}", drink3.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateDrink() throws Exception {
        mockMvc.perform(
                post("/drinks")
                    .content("""
                        {
                            "name": "Cappuccino",
                            "price": 7
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Cappuccino")))
            .andExpect(jsonPath("$.price", is(7.0)));
    }

    @Test
    public void testCreateDrinkRequiredArgumentMissing() throws Exception {
        mockMvc.perform(
                post("/drinks")
                    .content("""
                        {
                            "price": 2
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateDrink() throws Exception {
        mockMvc.perform(
                patch("/drinks/{id}", drink4.getId())
                    .content("""
                        {
                            "name": "Bubble Tea"
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Bubble Tea")))
            .andExpect(jsonPath("$.price", is(3.0)));
    }

    @Test
    public void testUpdateDrinkNotFound() throws Exception {
        drinksRepository.deleteById(drink4.getId());
        mockMvc.perform(
                patch("/drinks/{id}", drink4.getId())
                    .content("""
                        {
                            "name": "Milk Tea"
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteDrink() throws Exception {
        mockMvc.perform(delete("/drinks/{id}", drink1.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteDrinkNotFound() throws Exception {
        drinksRepository.deleteById(drink2.getId());
        mockMvc.perform(delete("/drinks/{id}", drink2.getId()))
            .andExpect(status().isNotFound());
    }
}
