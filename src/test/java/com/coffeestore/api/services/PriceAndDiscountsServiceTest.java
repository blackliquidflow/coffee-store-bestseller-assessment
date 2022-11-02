package com.coffeestore.api.services;

import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.models.Drink;
import com.coffeestore.api.models.Topping;
import com.coffeestore.api.services.impl.PriceAndDiscountsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceAndDiscountsServiceTest {
    private PriceAndDiscountsService priceAndDiscountsService;

    private Drink drink1, drink2, drink3, drink4;
    private Topping topping1, topping2, topping3, topping4;
    private Beverage beverage1, beverage2, beverage3, beverage4;
    private CartItem cartItem1, cartItem2, cartItem3, cartItem4;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        priceAndDiscountsService = new PriceAndDiscountsServiceImpl();

        drink1 = Drink.builder().name("Black Coffee").price(4.0).build();
        drink2 = Drink.builder().name("Latte").price(5.0).build();
        drink3 = Drink.builder().name("Mocha").price(6.0).build();
        drink4 = Drink.builder().name("Tea").price(3.0).build();

        topping1 = Topping.builder().name("Milk").price(2.0).build();
        topping2 = Topping.builder().name("Hazelnut syrup").price(3.0).build();
        topping3 = Topping.builder().name("Chocolate sauce").price(5.0).build();
        topping4 = Topping.builder().name("Lemon").price(2.0).build();

        beverage1 = Beverage.builder().hash(123L).drink(drink1).toppings(List.of(topping1, topping3)).build();
        beverage2 = Beverage.builder().hash(234L).drink(drink4).toppings(List.of(topping4)).build();
        beverage3 = Beverage.builder().hash(345L).drink(drink2).toppings(List.of(topping1, topping2)).build();
        beverage4 = Beverage.builder().hash(234L).drink(drink3).toppings(List.of(topping2, topping3)).build();

        cartItem1 = CartItem.builder().userId(1L).price(11.0).beverage(beverage1).build();
        cartItem2 = CartItem.builder().userId(1L).price(5.0).beverage(beverage2).build();
        cartItem3 = CartItem.builder().userId(1L).price(10.0).beverage(beverage3).build();
        cartItem4 = CartItem.builder().userId(1L).price(14.0).beverage(beverage4).build();
    }

    @Test
    public void testCalculateTotalPriceForCartItems() {
        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        assertEquals(16.0, priceAndDiscountsService.calculateTotalPriceForCartItems(cartItems));
    }

    @Test
    public void testCalculateTotalPriceForCartItemsEmptyCart() {
        List<CartItem> cartItems = Collections.emptyList();
        assertEquals(0, priceAndDiscountsService.calculateTotalPriceForCartItems(cartItems));
    }

    @Test
    public void testCalculatePriceWithDiscountForCartCheapestForFree() {
        List<CartItem> cartItems = List.of(cartItem1, cartItem3, cartItem4);
        assertEquals(25.0, priceAndDiscountsService.calculatePriceWithDiscountForCart(cartItems, 35.0));
    }

    @Test
    public void testCalculatePriceWithDiscountForCart25Percent() {
        List<CartItem> cartItems = List.of(cartItem1, cartItem2, cartItem3);
        assertEquals(19.5, priceAndDiscountsService.calculatePriceWithDiscountForCart(cartItems, 26.0));
    }

    @Test
    public void testCalculateBeveragePrice() {
        assertEquals(11.0, priceAndDiscountsService.calculateBeveragePrice(beverage1));
    }
}
