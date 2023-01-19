package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import com.scottlogic.matcher.models.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.scottlogic.matcher.service.database.OrderDbService;
import com.scottlogic.matcher.service.database.TradeDbService;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class MatcherTest {
    private static Matcher matcher;
    private static OrderDbService orderService;
    private static TradeDbService tradeService;

    @BeforeAll
    static void beforeAll() {
        orderService = Mockito.mock(OrderDbService.class);
        tradeService = Mockito.mock(TradeDbService.class);

        when(orderService.saveNewOrder(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(tradeService.saveNewTrades(anyList())).thenAnswer(i -> i.getArguments()[0]);

        matcher = new Matcher(orderService, tradeService);
    }

    @Test
    void givenEmptySellOrdersList_WhenReceiveBuyOrder_OrderAddedToBuyOrders() {
        Order order = new Order("userId", 10, 20, 20, Action.BUY);

        when(orderService.getSellOrders()).thenReturn(List.of());

        List<Trade> trades = matcher.receiveOrder(order);
        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenSellOrdersListMatchesExactly_WhenReceiveBuyOrder_TradeCompletedAndOrderListsEmpty() {
        Order sellOrder = new Order("userId", 10, 20, 20, Action.SELL);
        when(orderService.getSellOrders()).thenReturn(List.of(sellOrder));

        Order buyOrder = new Order("userId", 10, 20, 20, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenBuyOrderMatchesPartially_WhenReceiveBuyOrder_TradeCompleted() {
        Order sellOrder = new Order("userId", 10, 10, 10, Action.SELL);
        when(orderService.getSellOrders()).thenReturn(List.of(sellOrder));


        Order buyOrder = new Order("userId", 15, 20, 20, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenSellOrderMatchesPartially_WhenReceiveBuyOrder_TradeCompleted() {
        Order sellOrder = new Order("userId", 10, 20, 20, Action.SELL);
        when(orderService.getSellOrders()).thenReturn(List.of(sellOrder));

        Order buyOrder = new Order("userId", 15, 5, 5, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(5, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenNoMatchWithSellOrders_WhenReceiveBuyOrder_NoTradesMade() {
        Order sellOrder = new Order("userId", 10, 20, 20, Action.SELL);
        when(orderService.getSellOrders()).thenReturn(List.of(sellOrder));

        String expectedUser = "userId";
        int expectedPrice = 5;
        int expectedQuantity = 5;

        Order buyOrder = new Order(expectedUser, expectedPrice, expectedQuantity, expectedQuantity, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenMatchesMultipleSellOrders_WhenReceiveBuyOrder_TradesCompleted() {
        Order sellOrder = new Order("userId", 10, 20, 20, Action.SELL);
        Order sellOrder2 = new Order("userId", 15, 10, 10, Action.SELL);
        when(orderService.getSellOrders()).thenReturn(List.of(sellOrder, sellOrder2));

        Order buyOrder = new Order("userId", 20, 25, 25, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
        Assertions.assertEquals(5, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }

    @Test
    void givenPartiallyMatchesMultipleSellOrders_WhenReceiveBuyOrder_TradesCompleted() {
        Order sellOrder = new Order("userId", 10, 10, 10, Action.SELL);
        Order sellOrder2 = new Order("userId", 15, 10, 10, Action.SELL);
        when(orderService.getSellOrders()).thenReturn(List.of(sellOrder, sellOrder2));

        Order buyOrder = new Order("userId", 20, 25, 25, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
        Assertions.assertEquals(10, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }


    @Test
    void givenEmptyBuyOrdersList_WhenReceiveSellOrder_OrderAddedToBuyOrders() {
        Order order = new Order("userId", 10, 20, 20, Action.SELL);

        when(orderService.getBuyOrders()).thenReturn(List.of());

        List<Trade> trades = matcher.receiveOrder(order);
        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenBuyOrdersListMatchesExactly_WhenReceiveSellOrder_TradeCompletedAndOrderListsEmpty() {
        Order buyOrder = new Order("userId", 10, 20, 20, Action.BUY);
        when(orderService.getBuyOrders()).thenReturn(List.of(buyOrder));

        Order sellOrder = new Order("userId", 10, 20, 20, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenSellOrderMatchesPartially_WhenReceiveSellOrder_TradeCompleted() {
        Order buyOrder = new Order("userId", 15, 10, 10, Action.BUY);
        when(orderService.getBuyOrders()).thenReturn(List.of(buyOrder));

        Order sellOrder = new Order("userId", 10, 20, 20, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(15, trades.get(0).getPrice());
    }

    @Test
    void givenBuyOrderMatchesPartially_WhenReceiveSellOrder_TradeCompleted() {
        Order buyOrder = new Order("userId", 15, 20, 20, Action.BUY);
        when(orderService.getBuyOrders()).thenReturn(List.of(buyOrder));

        Order sellOrder = new Order("userId", 10, 5, 5, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(5, trades.get(0).getQuantity());
        Assertions.assertEquals(15, trades.get(0).getPrice());
    }

    @Test
    void givenNoMatchWithBuyOrders_WhenReceiveSellOrder_NoTradesMade() {
        Order buyOrder = new Order("userId", 5, 20, 20, Action.BUY);
        when(orderService.getBuyOrders()).thenReturn(List.of(buyOrder));

        Order sellOrder = new Order("userId", 10, 5, 5, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenMatchesMultipleBuyOrders_WhenReceiveSellOrder_TradesCompleted() {
        Order buyOrder = new Order("userId", 20, 20, 20, Action.BUY);
        Order buyOrder2 = new Order("userId", 15, 10, 10, Action.BUY);
        when(orderService.getBuyOrders()).thenReturn(List.of(buyOrder, buyOrder2));

        Order sellOrder = new Order("userId", 10, 25, 25, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(20, trades.get(0).getPrice());
        Assertions.assertEquals(5, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }

    @Test
    void givenPartiallyMatchesMultipleBuyOrders_WhenReceiveSellOrder_TradesCompleted() {
        Order buyOrder = new Order("userId", 20, 10, 10, Action.BUY);
        Order buyOrder2 = new Order("userId", 15, 10, 10, Action.BUY);
        when(orderService.getBuyOrders()).thenReturn(List.of(buyOrder, buyOrder2));

        Order sellOrder = new Order("userId", 10, 25, 25, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(20, trades.get(0).getPrice());
        Assertions.assertEquals(10, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }
}
