package com.scottlogic.matcher.controller;

import com.scottlogic.matcher.controller.dto.OrderDto;
import com.scottlogic.matcher.controller.dto.TradeDto;
import com.scottlogic.matcher.service.Matcher;
import com.scottlogic.matcher.models.Order;
import com.scottlogic.matcher.service.OrderService;
import com.scottlogic.matcher.utility.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Matcher matcher;
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    public OrderController(Matcher matcher, OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.matcher = matcher;
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/buy")
    public ResponseEntity<List<OrderDto>> getBuyOrders() {
        List<OrderDto> orders = orderService.getBuyOrders().stream().map(OrderDto::create).toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(value = "/sell")
    public ResponseEntity<List<OrderDto>> getSellOrders() {
        List<OrderDto> orders = orderService.getSellOrders().stream().map(OrderDto::create).toList();
        return ResponseEntity.ok(orders);
    }

    @PostMapping(value = "")
    public ResponseEntity<List<TradeDto>> placeOrder(HttpServletRequest req, @Valid @RequestBody OrderDto orderDto) {
        String username = jwtTokenProvider.getUsername(req);

        Order order = orderDto.toModel(username);
        List<TradeDto> trades = matcher.receiveOrder(order).stream().map(TradeDto::create).toList();
        return ResponseEntity.ok(trades);
    }
}
