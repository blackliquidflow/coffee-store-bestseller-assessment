package com.coffeestore.api.controllers;

import com.coffeestore.api.models.Topping;
import com.coffeestore.api.repositories.ToppingsRepository;

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
public class ToppingsControllerTest {
    @Autowired
    private ToppingsRepository toppingsRepository;
    @Autowired
    private MockMvc mockMvc;

    private Topping topping1, topping2, topping3, topping4;

    @BeforeEach
    public void setUp() {
        toppingsRepository.deleteAll();
        topping1 = toppingsRepository.save(Topping.builder().name("Milk").price(2.0).build());
        topping2 = toppingsRepository.save(Topping.builder().name("Hazelnut syrup").price(3.0).build());
        topping3 = toppingsRepository.save(Topping.builder().name("Chocolate sauce").price(5.0).build());
        topping4 = toppingsRepository.save(Topping.builder().name("Lemon").price(2.0).build());
    }

    @AfterEach
    public void tearDown() {
        toppingsRepository.deleteAll();
    }

    @Test
    public void testGetAllToppings() throws Exception {
        mockMvc.perform(get("/toppings"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(4)))
            .andExpect(jsonPath("$.content[0].name", is("Milk")))
            .andExpect(jsonPath("$.content[0].price", is(2.0)))
            .andExpect(jsonPath("$.content[1].name", is("Hazelnut syrup")))
            .andExpect(jsonPath("$.content[1].price", is(3.0)))
            .andExpect(jsonPath("$.content[2].name", is("Chocolate sauce")))
            .andExpect(jsonPath("$.content[2].price", is(5.0)))
            .andExpect(jsonPath("$.content[3].name", is("Lemon")))
            .andExpect(jsonPath("$.content[3].price", is(2.0)));
    }

    @Test
    public void testGetOneTopping() throws Exception {
        mockMvc.perform(get("/toppings/{id}", topping2.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(topping2.getId().intValue())))
            .andExpect(jsonPath("$.name", is("Hazelnut syrup")))
            .andExpect(jsonPath("$.price", is(3.0)));
    }

    @Test
    public void testGetOneToppingNotFound() throws Exception {
        toppingsRepository.deleteById(topping3.getId());
        mockMvc.perform(get("/toppings/{id}", topping3.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTopping() throws Exception {
        mockMvc.perform(
                post("/toppings")
                    .content("""
                        {
                            "name": "Cream",
                            "price": 1.2
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Cream")))
            .andExpect(jsonPath("$.price", is(1.2)));
    }

    @Test
    public void testCreateToppingRequiredArgumentMissing() throws Exception {
        mockMvc.perform(
                post("/toppings")
                    .content("""
                        {
                            "price": 2
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateTopping() throws Exception {
        mockMvc.perform(
                patch("/toppings/{id}", topping4.getId())
                    .content("""
                        {
                            "name": "Lemon slice"
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Lemon slice")))
            .andExpect(jsonPath("$.price", is(2.0)));
    }

    @Test
    public void testUpdateToppingNotFound() throws Exception {
        toppingsRepository.deleteById(topping4.getId());
        mockMvc.perform(
                patch("/toppings/{id}", topping4.getId())
                    .content("""
                        {
                            "name": "Lemon slice"
                        }""")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTopping() throws Exception {
        mockMvc.perform(delete("/toppings/{id}", topping1.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteToppingNotFound() throws Exception {
        toppingsRepository.deleteById(topping2.getId());
        mockMvc.perform(delete("/toppings/{id}", topping2.getId()))
            .andExpect(status().isNotFound());
    }
}
