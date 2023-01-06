package com.scottlogic.matcher.controller;

import com.scottlogic.matcher.controller.dto.OrderDto;
import com.scottlogic.matcher.controller.dto.TradeDto;
import com.scottlogic.matcher.service.Matcher;
import com.scottlogic.matcher.models.Order;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Matcher matcher;

    public OrderController(Matcher matcher) {
        this.matcher = matcher;
    }

    @GetMapping(value = "/buy")
    public ResponseEntity<List<OrderDto>> getBuyOrders() {
        List<OrderDto> orders = matcher.getBuyOrders().stream().map(OrderDto::create).toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(value = "/sell")
    public ResponseEntity<List<OrderDto>> getSellOrders() {
        List<OrderDto> orders = matcher.getSellOrders().stream().map(OrderDto::create).toList();
        return ResponseEntity.ok(orders);
    }

    @PostMapping(value = "")
    public ResponseEntity<List<TradeDto>> placeOrder(@Valid @RequestBody OrderDto orderDto) {
        Order order = orderDto.toModel();
        List<TradeDto> trades = matcher.receiveOrder(order).stream().map(TradeDto::create).toList();
        return ResponseEntity.ok(trades);
    }
}
