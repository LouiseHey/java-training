package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.Order;
import java.util.List;

public interface OrderService {
    Order saveNewOrder(Order order);

    void saveOrders(List<Order> orders);

    List<Order> getBuyOrders();

    List<Order> getSellOrders();
}
