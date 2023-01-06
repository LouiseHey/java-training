package com.scottlogic.matcher.models;

import com.scottlogic.matcher.utility.IdGenerator;
import lombok.Getter;

@Getter
public class Order implements Comparable<Order> {
    private final String orderId;
    private final User user;
    private final int price;
    private int quantity;
    private final Action action;

    public Order(User user, int price, int quantity, Action action) {
        this(IdGenerator.generate(), user, price, quantity, action);
    }

    private Order(String orderId, User user, int price, int quantity, Action action) {
        this.orderId = orderId;
        this.user = user;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    public void removeQuantity(int removedQuantity) {
        this.quantity = this.quantity - removedQuantity;
    }

    public boolean isQuantityZero() {
        return quantity == 0;
    }

    @Override
    public int compareTo(Order o) {
        return o.getPrice() - this.getPrice();
    }
}
