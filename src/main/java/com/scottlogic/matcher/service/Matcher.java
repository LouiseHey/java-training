package com.scottlogic.matcher.service;

import com.scottlogic.matcher.models.MatchedOrdersAndTrades;
import com.scottlogic.matcher.models.Order;
import com.scottlogic.matcher.models.Trade;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class Matcher {

    private final OrderService orderService;
    private final TradeService tradeService;

    public Matcher(OrderService orderService, TradeService tradeService) {
        this.orderService = orderService;
        this.tradeService = tradeService;
    }

    public List<Trade> receiveOrder(Order order) {
        order = orderService.saveNewOrder(order);

        MatchedOrdersAndTrades matched = switch (order.getAction()) {
            case BUY -> matchBuyOrder(order);
            case SELL -> matchSellOrder(order);
        };

        orderService.saveOrders(matched.getOrders());
        return tradeService.saveNewTrades(matched.getTrades());
    }

    private MatchedOrdersAndTrades matchBuyOrder(Order buyOrder) {
        List<Order> sellOrders = orderService.getSellOrders();
        MatchedOrdersAndTrades matched = new MatchedOrdersAndTrades();

        for (Order sellOrder : sellOrders) {
            if (buyOrder.getQuantity() != 0 && buyOrder.getPrice() >= sellOrder.getPrice()) {
                Trade trade = new Trade(buyOrder, sellOrder, sellOrder.getPrice());

                buyOrder.removeQuantity(trade.getQuantity());
                sellOrder.removeQuantity(trade.getQuantity());

                matched.addOrder(sellOrder);
                matched.addTrade(trade);
            } else {
                break;
            }
        }

        matched.addOrder(buyOrder);
        return matched;
    }

    private MatchedOrdersAndTrades matchSellOrder(Order sellOrder) {
        List<Order> buyOrders = orderService.getBuyOrders();
        MatchedOrdersAndTrades matched = new MatchedOrdersAndTrades();

        for (Order buyOrder : buyOrders) {
            if (sellOrder.getQuantity() != 0 && sellOrder.getPrice() <= buyOrder.getPrice()) {
                Trade trade = new Trade(buyOrder, sellOrder, buyOrder.getPrice());

                buyOrder.removeQuantity(trade.getQuantity());
                sellOrder.removeQuantity(trade.getQuantity());

                matched.addOrder(buyOrder);
                matched.addTrade(trade);
            } else {
                break;
            }
        }

        matched.addOrder(sellOrder);
        return matched;
    }
}
