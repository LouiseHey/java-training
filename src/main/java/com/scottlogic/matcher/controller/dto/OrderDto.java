package com.scottlogic.matcher.controller.dto;

import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private final String username;
    @NotNull
    @Min(value=0L, message="Price must be non negative.")
    private final Integer price;
    @NotNull
    @Min(value=0L, message="Price must be non negative.")
    private Integer quantity;
    @NotNull
    private final Action action;

    public OrderDto(String username, Integer price, Integer quantity, Action action) {
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    public static OrderDto create(Order order) {
        return new OrderDto(order.getUsername(), order.getPrice(), order.getQuantity(), order.getAction());
    }

    public Order toModel(String username) {
        return new Order(username, this.price, this.quantity, this.action);
    }
}
