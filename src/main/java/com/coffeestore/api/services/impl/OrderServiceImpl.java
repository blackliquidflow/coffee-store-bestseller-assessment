package com.coffeestore.api.services.impl;

import com.coffeestore.api.dto.CartDto;
import com.coffeestore.api.models.CartItem;
import com.coffeestore.api.models.Order;
import com.coffeestore.api.models.OrderLine;
import com.coffeestore.api.repositories.OrderLinesRepository;
import com.coffeestore.api.repositories.OrdersRepository;
import com.coffeestore.api.services.CartService;
import com.coffeestore.api.services.OrderHistoryService;
import com.coffeestore.api.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    private final OrdersRepository ordersRepository;
    private final OrderLinesRepository orderLinesRepository;
    private final OrderHistoryService orderHistoryService;

    @Override
    public Optional<Order> createNewOrder(Long userId) {
        CartDto cart = cartService.getCartContents(userId);
        if (cart.getCartItems().isEmpty()) {
            log.warn("No items is present in the Cart for user {}. Order will not be created.", userId);
            return Optional.empty();
        }

        Order order = Order.builder()
            .userId(userId)
            .totalPrice(cart.getTotalPrice())
            .totalPriceWithDiscount(cart.getPriceWithDiscount())
            .creationTime(LocalDateTime.now())
            .build();
        order = ordersRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        List<OrderLine> orderLines = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderLine orderLine = OrderLine.builder()
                .orderId(order.getId())
                .price(cartItem.getPrice())
                .beverageId(cartItem.getBeverage().getId())
                .build();
            orderLine = orderLinesRepository.save(orderLine);
            orderLines.add(orderLine);
        }

        orderHistoryService.addOrderHistoryRecords(order, orderLines);

        cartService.clearCart(userId);

        log.info("Order {} was successfully created", order);
        return Optional.of(order);
    }
}
