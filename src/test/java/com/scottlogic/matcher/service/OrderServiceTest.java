package com.scottlogic.matcher.service;

import com.scottlogic.matcher.data.OrderRepository;
import com.scottlogic.matcher.data.entity.OrderEntity;
import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderServiceTest {
    private static OrderService orderService;
    private static OrderRepository orderRepository;

    @BeforeAll
    static void beforeAll() {
        orderRepository = Mockito.mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    void whenSaveNewOrder_orderReturned() {
        Order order = new Order("userId", 10, 20, 20, Action.BUY);

        when(orderRepository.insert(any(OrderEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        Order savedOrder = orderService.saveNewOrder(order);
        Assertions.assertEquals(order.getUsername(), savedOrder.getUsername());
        Assertions.assertEquals(order.getPrice(), savedOrder.getPrice());
        Assertions.assertEquals(order.getQuantity(), savedOrder.getQuantity());
        Assertions.assertEquals(order.getAction(), savedOrder.getAction());
    }

    @Test
    void whenGetBuyOrders_buyOrdersReturnedAndSortedAndFiltered() {
        OrderEntity order1 = new OrderEntity("orderId1", "userId", 100, 20, 20, Action.BUY);
        OrderEntity order2 = new OrderEntity("orderId2", "userId", 10, 20, 20, Action.BUY);
        OrderEntity order3 = new OrderEntity("orderId3", "userId", 50, 20, 20, Action.BUY);
        OrderEntity order4 = new OrderEntity("orderId4", "userId", 20, 0, 20, Action.BUY);

        when(orderRepository.findByAction(Action.BUY)).thenReturn(List.of(order1, order2, order3, order4));


        List<Order> orders = orderService.getBuyOrders();
        Assertions.assertEquals(3, orders.size());

        Assertions.assertEquals(100, orders.get(0).getPrice());
        Assertions.assertEquals(50, orders.get(1).getPrice());
        Assertions.assertEquals(10, orders.get(2).getPrice());
    }

    @Test
    void whenGetSellOrders_sellOrdersReturnedAndSortedAndFiltered() {
        OrderEntity order1 = new OrderEntity("orderId1", "userId", 100, 20, 20, Action.SELL);
        OrderEntity order2 = new OrderEntity("orderId2", "userId", 10, 20, 20, Action.SELL);
        OrderEntity order3 = new OrderEntity("orderId3", "userId", 50, 20, 20, Action.SELL);
        OrderEntity order4 = new OrderEntity("orderId4", "userId", 20, 0, 20, Action.SELL);

        when(orderRepository.findByAction(Action.SELL)).thenReturn(List.of(order1, order2, order3, order4));


        List<Order> orders = orderService.getSellOrders();
        Assertions.assertEquals(3, orders.size());

        Assertions.assertEquals(10, orders.get(0).getPrice());
        Assertions.assertEquals(50, orders.get(1).getPrice());
        Assertions.assertEquals(100, orders.get(2).getPrice());
    }
}
