package com.coffeestore.api.services.impl;

import com.coffeestore.api.dto.BeverageAddRequestDto;
import com.coffeestore.api.dto.CartDto;
import com.coffeestore.api.models.Beverage;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.repositories.BeverageRepository;
import com.coffeestore.api.repositories.CartRepository;
import com.coffeestore.api.services.BeverageService;
import com.coffeestore.api.services.CartService;
import com.coffeestore.api.services.PriceAndDiscountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final BeverageService beverageService;
    private final PriceAndDiscountsService priceAndDiscountsService;
    private final BeverageRepository beverageRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItem addBeverageToCart(Long userId, BeverageAddRequestDto addRequest) {
        Beverage beverage = getOrCreateBeverage(addRequest);

        CartItem cartItem = CartItem.builder()
            .beverage(beverage)
            .userId(userId)
            .price(priceAndDiscountsService.calculateBeveragePrice(beverage))
            .build();

        return cartRepository.save(cartItem);
    }

    @Override
    public CartDto getCartContents(Long userId) {
        List<CartItem> cartItems = cartRepository.findAllByUserId(userId);
        Double totalPrice = priceAndDiscountsService.calculateTotalPriceForCartItems(cartItems);
        Double priceWithDiscount = priceAndDiscountsService.calculatePriceWithDiscountForCart(cartItems, totalPrice);
        return CartDto.builder()
            .cartItems(cartItems)
            .totalPrice(totalPrice)
            .priceWithDiscount(priceWithDiscount)
            .build();
    }

    @Override
    public boolean cartItemExists(Long id, Long userId) {
        return cartRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public void deleteCartItem(Long id, Long userId) {
        cartRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    private Beverage getOrCreateBeverage(BeverageAddRequestDto addRequest) {
        Long beverageHash = beverageService.calculateHash(addRequest.getDrinkId(), addRequest.getToppingIds());
        List<Beverage> beveragesWithMatchingHash = beverageRepository.findAllByHash(beverageHash);
        for (Beverage beverage : beveragesWithMatchingHash) {
            if (beverageService.addRequestMatchesExisting(addRequest, beverage)) {
                return beverage;
            }
        }
        return beverageService.createBeverage(addRequest, beverageHash);
    }
}
