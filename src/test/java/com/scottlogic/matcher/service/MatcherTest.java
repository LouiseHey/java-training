package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.User;
import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import com.scottlogic.matcher.models.Trade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class MatcherTest {
    Matcher matcher;

    @BeforeEach
    public void beforeEach() {
        matcher = new Matcher();
    }

    @Test
    void givenEmptySellOrdersList_WhenReceiveBuyOrder_OrderAddedToBuyOrders() {
        String expectedUser = "userId";
        int expectedPrice = 10;
        int expectedQuantity = 20;

        Order order = new Order(expectedUser, expectedPrice, expectedQuantity, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(order);

        List<Order> buyOrders = matcher.getBuyOrders();
        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(expectedQuantity, buyOrders.get(0).getQuantity());

        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenBuyOrderInList_WhenReceiveBuyOrder_BuyOrderSortedByPriceDescending() {
        Order buyOrder = new Order("userId", 20, 20, Action.BUY);
        matcher.receiveOrder(buyOrder);

        Order buyOrder1 = new Order("userId", 30, 20, Action.BUY);
        matcher.receiveOrder(buyOrder1);

        Order buyOrder2 = new Order("userId", 25, 20, Action.BUY);
        matcher.receiveOrder(buyOrder2);

        List<Order> buyOrders = matcher.getBuyOrders();

        Assertions.assertEquals(3, buyOrders.size());
        Assertions.assertEquals(30, buyOrders.get(0).getPrice());
        Assertions.assertEquals(25, buyOrders.get(1).getPrice());
        Assertions.assertEquals(20, buyOrders.get(2).getPrice());
    }

    @Test
    void givenSellOrdersListMatchesExactly_WhenReceiveBuyOrder_TradeCompletedAndOrderListsEmpty() {
        Order sellOrder = new Order("userId", 10, 20, Action.SELL);
        matcher.receiveOrder(sellOrder);

        Order buyOrder = new Order("userId", 10, 20, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(0, buyOrders.size());
        Assertions.assertEquals(0, sellOrders.size());

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenBuyOrderMatchesPartially_WhenReceiveBuyOrder_TradeCompleted() {
        Order sellOrder = new Order("userId", 10, 10, Action.SELL);
        matcher.receiveOrder(sellOrder);

        Order buyOrder = new Order("userId", 15, 20, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(0, sellOrders.size());
        Assertions.assertEquals(10, buyOrders.get(0).getQuantity());

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenSellOrderMatchesPartially_WhenReceiveBuyOrder_TradeCompleted() {
        Order sellOrder = new Order("userId", 10, 20, Action.SELL);
        matcher.receiveOrder(sellOrder);

        Order buyOrder = new Order("userId", 15, 5, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(0, buyOrders.size());
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(15, sellOrders.get(0).getQuantity());

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(5, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenNoMatchWithSellOrders_WhenReceiveBuyOrder_OrderAdded() {
        Order sellOrder = new Order("userId", 10, 20, Action.SELL);
        matcher.receiveOrder(sellOrder);

        String expectedUser = "userId";
        int expectedPrice = 5;
        int expectedQuantity = 5;

        Order buyOrder = new Order(expectedUser, expectedPrice, expectedQuantity, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(expectedQuantity, buyOrders.get(0).getQuantity());

        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenMatchesMultipleSellOrders_WhenReceiveBuyOrder_TradesCompleted() {
        Order sellOrder = new Order("userId", 10, 20, Action.SELL);
        matcher.receiveOrder(sellOrder);

        Order sellOrder2 = new Order("userId", 15, 10, Action.SELL);
        matcher.receiveOrder(sellOrder2);

        Order buyOrder = new Order("userId", 20, 25, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(0, buyOrders.size());
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(5, sellOrders.get(0).getQuantity());

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
        Assertions.assertEquals(5, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }

    @Test
    void givenPartiallyMatchesMultipleSellOrders_WhenReceiveBuyOrder_TradesCompleted() {
        Order sellOrder = new Order("userId", 10, 10, Action.SELL);
        matcher.receiveOrder(sellOrder);

        Order sellOrder2 = new Order("userId", 15, 10, Action.SELL);
        matcher.receiveOrder(sellOrder2);

        Order buyOrder = new Order("userId", 20, 25, Action.BUY);
        List<Trade> trades = matcher.receiveOrder(buyOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(0, sellOrders.size());
        Assertions.assertEquals(5, buyOrders.get(0).getQuantity());

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
        Assertions.assertEquals(10, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }


    @Test
    void givenEmptyBuyOrdersList_WhenReceiveSellOrder_OrderAddedToBuyOrders() {
        String expectedUser = "userId";
        int expectedPrice = 10;
        int expectedQuantity = 20;

        Order order = new Order(expectedUser, expectedPrice, expectedQuantity, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(order);

        List<Order> sellOrders = matcher.getSellOrders();
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(expectedQuantity, sellOrders.get(0).getQuantity());

        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenSellOrderInList_WhenReceiveSellOrder_SellOrderSortedByPriceAscending() {
        Order sellOrder = new Order("userId", 30, 20, Action.SELL);
        matcher.receiveOrder(sellOrder);

        Order sellOrder1 = new Order("userId", 20, 20, Action.SELL);
        matcher.receiveOrder(sellOrder1);

        Order sellOrder2 = new Order("userId", 25, 20, Action.SELL);
        matcher.receiveOrder(sellOrder2);

        List<Order> buyOrders = matcher.getSellOrders();

        Assertions.assertEquals(3, buyOrders.size());
        Assertions.assertEquals(20, buyOrders.get(0).getPrice());
        Assertions.assertEquals(25, buyOrders.get(1).getPrice());
        Assertions.assertEquals(30, buyOrders.get(2).getPrice());
    }

    @Test
    void givenBuyOrdersListMatchesExactly_WhenReceiveSellOrder_TradeCompletedAndOrderListsEmpty() {
        Order buyOrder = new Order("userId", 10, 20, Action.BUY);
        matcher.receiveOrder(buyOrder);

        Order sellOrder = new Order("userId", 10, 20, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(0, buyOrders.size());
        Assertions.assertEquals(0, sellOrders.size());

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(10, trades.get(0).getPrice());
    }

    @Test
    void givenSellOrderMatchesPartially_WhenReceiveSellOrder_TradeCompleted() {
        Order buyOrder = new Order("userId", 15, 10, Action.BUY);
        matcher.receiveOrder(buyOrder);

        Order sellOrder = new Order("userId", 10, 20, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(0, buyOrders.size());
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(10, sellOrders.get(0).getQuantity());

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(15, trades.get(0).getPrice());
    }

    @Test
    void givenBuyOrderMatchesPartially_WhenReceiveSellOrder_TradeCompleted() {
        Order buyOrder = new Order("userId", 15, 20, Action.BUY);
        matcher.receiveOrder(buyOrder);

        Order sellOrder = new Order("userId", 10, 5, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(0, sellOrders.size());
        Assertions.assertEquals(15, buyOrders.get(0).getQuantity());

        Assertions.assertEquals(1, trades.size());
        Assertions.assertEquals(5, trades.get(0).getQuantity());
        Assertions.assertEquals(15, trades.get(0).getPrice());
    }

    @Test
    void givenNoMatchWithBuyOrders_WhenReceiveSellOrder_OrderAdded() {
        Order buyOrder = new Order("userId", 5, 20, Action.BUY);
        matcher.receiveOrder(buyOrder);

        String expectedUser = "userId";
        int expectedPrice = 10;
        int expectedQuantity = 5;

        Order sellOrder = new Order(expectedUser, expectedPrice, expectedQuantity, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(expectedQuantity, sellOrders.get(0).getQuantity());

        Assertions.assertEquals(0, trades.size());
    }

    @Test
    void givenMatchesMultipleBuyOrders_WhenReceiveSellOrder_TradesCompleted() {
        Order buyOrder = new Order("userId", 20, 20, Action.BUY);
        matcher.receiveOrder(buyOrder);

        Order buyOrder2 = new Order("userId", 15, 10, Action.BUY);
        matcher.receiveOrder(buyOrder2);

        Order sellOrder = new Order("userId", 10, 25, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(1, buyOrders.size());
        Assertions.assertEquals(0, sellOrders.size());
        Assertions.assertEquals(5, buyOrders.get(0).getQuantity());

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(20, trades.get(0).getQuantity());
        Assertions.assertEquals(20, trades.get(0).getPrice());
        Assertions.assertEquals(5, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }

    @Test
    void givenPartiallyMatchesMultipleBuyOrders_WhenReceiveSellOrder_TradesCompleted() {
        Order buyOrder = new Order("userId", 20, 10, Action.BUY);
        matcher.receiveOrder(buyOrder);

        Order buyOrder2 = new Order("userId", 15, 10, Action.BUY);
        matcher.receiveOrder(buyOrder2);

        Order sellOrder = new Order("userId", 10, 25, Action.SELL);
        List<Trade> trades = matcher.receiveOrder(sellOrder);

        List<Order> buyOrders = matcher.getBuyOrders();
        List<Order> sellOrders = matcher.getSellOrders();

        Assertions.assertEquals(0, buyOrders.size());
        Assertions.assertEquals(1, sellOrders.size());
        Assertions.assertEquals(5, sellOrders.get(0).getQuantity());

        Assertions.assertEquals(2, trades.size());
        Assertions.assertEquals(10, trades.get(0).getQuantity());
        Assertions.assertEquals(20, trades.get(0).getPrice());
        Assertions.assertEquals(10, trades.get(1).getQuantity());
        Assertions.assertEquals(15, trades.get(1).getPrice());
    }
}
