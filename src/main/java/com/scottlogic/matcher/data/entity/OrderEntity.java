package com.scottlogic.matcher.data.entity;

import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Order")
public class OrderEntity {
    @Id
    private String id;
    private String username;
    private int price;
    private int quantity;
    private int initialQuantity;
    private Action action;

    public OrderEntity(String id, String username, int price, int quantity, int initialQuantity, Action action) {
        this.id = id;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.initialQuantity = initialQuantity;
        this.action = action;
    }

    public OrderEntity() {}

    public Order toModel() {
        return new Order(this.id, this.username, this.price, this.quantity, this.initialQuantity, this.action);
    }

    public static OrderEntity create(Order order) {
        return new OrderEntity(order.getOrderId(), order.getUsername(), order.getPrice(), order.getQuantity(), order.getInitialQuantity(), order.getAction());
    }
}
