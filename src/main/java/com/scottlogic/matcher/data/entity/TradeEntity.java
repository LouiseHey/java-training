package com.scottlogic.matcher.data.entity;

import com.scottlogic.matcher.models.Trade;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Trade")
public class TradeEntity {
    @Id
    private String id;
    private String buyOrderId;
    private String sellOrderId;
    private String buyUser;
    private String sellUser;
    private int price;
    private int quantity;

    public TradeEntity(String buyOrderId, String sellOrderId, String buyUser,
                       String sellUser, int price, int quantity) {
        this(null, buyOrderId, sellOrderId, buyUser, sellUser, price, quantity);
    }

    public TradeEntity(String id, String buyOrderId, String sellOrderId, String buyUser,
                       String sellUser, int price, int quantity) {
        this.id = id;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyUser = buyUser;
        this.sellUser = sellUser;
        this.price = price;
        this.quantity = quantity;
    }

    public TradeEntity() {}

    public Trade toModel() {
        return new Trade(this.id, this.buyOrderId, this.sellOrderId, this.buyUser, this.sellUser,
            this.price, this.quantity);
    }

    public static TradeEntity create(Trade trade) {
        return new TradeEntity(trade.getTradeId(), trade.getBuyOrderId(), trade.getSellOrderId(), trade.getBuyUser(),
            trade.getSellUser(), trade.getPrice(), trade.getQuantity());
    }
}
