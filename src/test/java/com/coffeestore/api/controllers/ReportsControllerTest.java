package com.coffeestore.api.controllers;

import com.coffeestore.api.models.OrderHistoryLine;
import com.coffeestore.api.repositories.OrderHistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ReportsControllerTest {
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(1L).orderLineId(1L).orderDateTime(LocalDateTime.of(2022, 10, 20, 10, 0)).beverageId(1L).ingredientType("drink").ingredientId(1L).ingredientName("Black Coffee").price(4.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(1L).orderLineId(2L).orderDateTime(LocalDateTime.of(2022, 10, 20, 10, 0)).beverageId(1L).ingredientType("topping").ingredientId(1L).ingredientName("Milk").price(2.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(2L).orderLineId(1L).orderDateTime(LocalDateTime.of(2022, 10, 22, 15, 20)).beverageId(2L).ingredientType("drink").ingredientId(3L).ingredientName("Mocha").price(6.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(2L).orderLineId(2L).orderDateTime(LocalDateTime.of(2022, 10, 22, 15, 20)).beverageId(2L).ingredientType("topping").ingredientId(2L).ingredientName("Hazelnut syrup").price(3.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(3L).orderLineId(1L).orderDateTime(LocalDateTime.of(2022, 10, 25, 7, 30)).beverageId(3L).ingredientType("drink").ingredientId(4L).ingredientName("Tea").price(3.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(3L).orderLineId(2L).orderDateTime(LocalDateTime.of(2022, 10, 25, 7, 30)).beverageId(3L).ingredientType("topping").ingredientId(4L).ingredientName("Lemon").price(2.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(3L).orderLineId(3L).orderDateTime(LocalDateTime.of(2022, 10, 25, 7, 30)).beverageId(4L).ingredientType("drink").ingredientId(3L).ingredientName("Mocha").price(6.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(4L).orderLineId(1L).orderDateTime(LocalDateTime.of(2022, 10, 28, 9, 56)).beverageId(4L).ingredientType("topping").ingredientId(2L).ingredientName("Hazelnut syrup").price(3.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(4L).orderLineId(2L).orderDateTime(LocalDateTime.of(2022, 10, 28, 9, 56)).beverageId(4L).ingredientType("topping").ingredientId(1L).ingredientName("Milk").price(2.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(5L).orderLineId(1L).orderDateTime(LocalDateTime.of(2022, 10, 30, 11, 12)).beverageId(1L).ingredientType("drink").ingredientId(1L).ingredientName("Black Coffee").price(4.0).build());
        orderHistoryRepository.save(OrderHistoryLine.builder().orderId(5L).orderLineId(2L).orderDateTime(LocalDateTime.of(2022, 10, 30, 11, 12)).beverageId(1L).ingredientType("topping").ingredientId(1L).ingredientName("Milk").price(2.0).build());
    }

    @AfterEach
    public void tearDown() {
        orderHistoryRepository.deleteAll();
    }

    @Test
    public void testGetMostUsedToppingsDefaultTimeLimit() throws Exception {
        mockMvc.perform(get("/reports"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(3)))
            .andExpect(jsonPath("$.content[0].numberOfUsages", is(3)))
            .andExpect(jsonPath("$.content[0].toppingId", is(1)))
            .andExpect(jsonPath("$.content[0].toppingName", is("Milk")))
            .andExpect(jsonPath("$.content[1].numberOfUsages", is(2)))
            .andExpect(jsonPath("$.content[1].toppingId", is(2)))
            .andExpect(jsonPath("$.content[1].toppingName", is("Hazelnut syrup")))
            .andExpect(jsonPath("$.content[2].numberOfUsages", is(1)))
            .andExpect(jsonPath("$.content[2].toppingId", is(4)))
            .andExpect(jsonPath("$.content[2].toppingName", is("Lemon")));
    }

    @Test
    public void testGetMostUsedToppings() throws Exception {
        mockMvc.perform(get("/reports")
                .param("startDate", "2022-10-22")
                .param("endDate", "2022-10-28"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(3)))
            .andExpect(jsonPath("$.content[0].numberOfUsages", is(2)))
            .andExpect(jsonPath("$.content[0].toppingId", is(2)))
            .andExpect(jsonPath("$.content[0].toppingName", is("Hazelnut syrup")))
            .andExpect(jsonPath("$.content[1].numberOfUsages", is(1)))
            .andExpect(jsonPath("$.content[1].toppingId", is(1)))
            .andExpect(jsonPath("$.content[1].toppingName", is("Milk")))
            .andExpect(jsonPath("$.content[2].numberOfUsages", is(1)))
            .andExpect(jsonPath("$.content[2].toppingId", is(4)))
            .andExpect(jsonPath("$.content[2].toppingName", is("Lemon")));
    }
}
