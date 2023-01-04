package com.scottlogic.matcher.controller.dto;

import com.scottlogic.matcher.models.Trade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeDto {
    private final String tradeId;
    private final String buyOrderId;
    private final String sellOrderId;
    private final int price;
    private final int quantity;

    public TradeDto(String tradeId, String buyOrderId, String sellOrderId, int price, int quantity) {
        this.tradeId = tradeId;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    public static TradeDto create(Trade trade) {
        return new TradeDto(trade.getTradeId(), trade.getBuyOrderId(), trade.getSellOrderId(), trade.getPrice(), trade.getQuantity());
    }
}
