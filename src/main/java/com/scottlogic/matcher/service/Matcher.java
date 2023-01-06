package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.Order;
import com.scottlogic.matcher.models.Trade;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Getter
public class Matcher {

    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();

    public List<Trade> receiveOrder(Order order) {
        return switch (order.getAction()) {
            case BUY -> matchBuyOrder(order);
            case SELL ->  matchSellOrder(order);
        };
    }

    private List<Trade> matchBuyOrder(Order buyOrder) {
        List<Trade> newTrades = new ArrayList<>();

        for (Order sellOrder : sellOrders) {
            if (buyOrder.getQuantity() == 0) {
                break;
            }

            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                Trade trade = new Trade(buyOrder, sellOrder, sellOrder.getPrice());
                newTrades.add(trade);

                sellOrder.removeQuantity(trade.getQuantity());
                buyOrder.removeQuantity(trade.getQuantity());
            } else {
                break;
            }
        }

        if (buyOrder.getQuantity() != 0) {
            buyOrders.add(buyOrder);
        }

        sortOrderLists();
        return newTrades;
    }

    private List<Trade> matchSellOrder(Order sellOrder) {
        List<Trade> newTrades = new ArrayList<>();

        for (Order buyOrder : buyOrders) {
            if (sellOrder.getQuantity() == 0) {
                break;
            }

            if (sellOrder.getPrice() <= buyOrder.getPrice()) {
                Trade trade = new Trade(buyOrder, sellOrder, buyOrder.getPrice());
                newTrades.add(trade);

                buyOrder.removeQuantity(trade.getQuantity());
                sellOrder.removeQuantity(trade.getQuantity());
            } else {
                break;
            }
        }

        if (sellOrder.getQuantity() != 0) {
            sellOrders.add(sellOrder);
        }

        sortOrderLists();
        return newTrades;
    }

    private void sortOrderLists() {
        buyOrders.removeIf(Order::isQuantityZero);
        buyOrders.sort(Comparator.naturalOrder());

        sellOrders.removeIf(Order::isQuantityZero);
        sellOrders.sort(Comparator.naturalOrder());
        Collections.reverse(sellOrders);
    }
}
