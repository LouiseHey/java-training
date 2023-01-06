package com.scottlogic.matcher.controller.dto;

import com.scottlogic.matcher.models.User;
import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private final String accountId;
    @Min(value=0L, message="Price must be non negative.")
    private final int price;
    @Min(value=0L, message="Price must be non negative.")
    private int quantity;
    private final Action action;

    public OrderDto(String accountId, int price, int quantity, Action action) {
        this.accountId = accountId;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    public static OrderDto create(Order order) {
        return new OrderDto(order.getUser().getUserId(), order.getPrice(), order.getQuantity(), order.getAction());
    }

    public Order toModel() {
        return new Order(new User(this.accountId), this.price, this.quantity, this.action);
    }
}
