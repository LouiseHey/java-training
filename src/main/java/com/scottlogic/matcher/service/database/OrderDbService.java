package com.scottlogic.matcher.service.database;

import com.scottlogic.matcher.data.OrderRepository;
import com.scottlogic.matcher.data.entity.OrderEntity;
import com.scottlogic.matcher.models.Action;
import com.scottlogic.matcher.models.Order;
import com.scottlogic.matcher.service.OrderService;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class OrderDbService implements OrderService {
    private final OrderRepository orderRepository;

    public OrderDbService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveNewOrder(Order order) {
        return orderRepository.insert(OrderEntity.create(order)).toModel();
    }

    public void saveOrders(List<Order> orders) {
        orderRepository.saveAll(orders.stream().map(OrderEntity::create).toList());
    }

    public List<Order> getBuyOrders() {
        return getOrders(Action.BUY)
            .stream()
            .sorted(Comparator.naturalOrder())
            .toList();
    }

    public List<Order> getSellOrders() {
        return getOrders(Action.SELL)
            .stream()
            .sorted(Comparator.reverseOrder())
            .toList();
    }

    private List<Order> getOrders(Action action) {
        return orderRepository.findByAction(action)
            .stream()
            .map(OrderEntity::toModel)
            .filter(o -> !o.isQuantityZero())
            .toList();
    }
}
