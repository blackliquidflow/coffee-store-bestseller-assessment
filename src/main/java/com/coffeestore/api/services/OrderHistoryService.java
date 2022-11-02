package com.coffeestore.api.services;

import com.coffeestore.api.models.Order;
import com.coffeestore.api.models.OrderHistoryLine;
import com.coffeestore.api.models.OrderLine;

import java.util.List;

/**
 * Service for managing Order History
 */
public interface OrderHistoryService {
    /**
     * Add new Order History records
     *
     * @param order      {@link Order} object with data of the Order created
     * @param orderLines list of {@link OrderLine} objects with data about the Beverages present in the Order
     * @return list of {@link OrderHistoryLine} records containing all history information about the Order
     */
    List<OrderHistoryLine> addOrderHistoryRecords(Order order, List<OrderLine> orderLines);
}
