package com.example.foodordering.controller;

import com.example.foodordering.dto.OrderRequestDTO;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<FoodOrder> placeOrder(@Valid @RequestBody OrderRequestDTO dto) {
        FoodOrder order = orderService.placeOrder(dto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodOrder> getOrderById(@PathVariable Long id) {
        FoodOrder order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<FoodOrder>> getAllOrders() {
        List<FoodOrder> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<FoodOrder> cancelOrder(@PathVariable Long id) {
        FoodOrder cancelled = orderService.cancelOrder(id);
        return ResponseEntity.ok(cancelled);
    }
}
