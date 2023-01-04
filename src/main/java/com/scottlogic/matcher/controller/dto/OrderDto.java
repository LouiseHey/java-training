package com.scottlogic.matcher.controller.dto;

import com.scottlogic.matcher.models.Account;
import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private final String accountId;
    private final int price;
    private int quantity;
    private final Action action;

    public OrderDto(String accountId, int price, int quantity, Action action) {
        this.accountId = accountId;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    public static OrderDto create(Order order) {
        return new OrderDto(order.getAccount().getAccountId(), order.getPrice(), order.getQuantity(), order.getAction());
    }

    public Order toModel() {
        return new Order(new Account(this.accountId), this.price, this.quantity, this.action);
    }
}