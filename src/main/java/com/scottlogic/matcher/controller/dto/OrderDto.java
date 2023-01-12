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
    private Integer initialQuantity;
    @NotNull
    private final Action action;

    public OrderDto(String username, Integer price, Integer quantity, Integer initialQuantity, Action action) {
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.initialQuantity = initialQuantity;
        this.action = action;
    }

    public static OrderDto create(Order order) {
        return new OrderDto(order.getUsername(), order.getPrice(), order.getQuantity(), order.getInitialQuantity(), order.getAction());
    }

    public Order toModel(String username) {
        if (this.initialQuantity == null) {
            this.initialQuantity = this.quantity;
        }
        return new Order(username, this.price, this.quantity, this.initialQuantity, this.action);
    }
}
