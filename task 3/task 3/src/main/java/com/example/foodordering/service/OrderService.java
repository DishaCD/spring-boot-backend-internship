package com.example.foodordering.service;

import com.example.foodordering.dto.OrderRequestDTO;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.exception.InsufficientStockException;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.MenuRepository;
import com.example.foodordering.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public FoodOrder placeOrder(OrderRequestDTO dto) {
        MenuItem item = menuRepository.findByName(dto.getItemName())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with name: " + dto.getItemName()));

        if (dto.getQuantity() > item.getAvailableQuantity()) {
            logger.warn("WARN  Attempt to order quantity greater than available stock");
            throw new InsufficientStockException("Insufficient quantity available for " + dto.getItemName());
        }

        logger.info("INFO  Calculating order total amount");
        double totalAmount = item.getPrice() * dto.getQuantity();

        item.setAvailableQuantity(item.getAvailableQuantity() - dto.getQuantity());
        menuRepository.save(item);

        FoodOrder order = FoodOrder.builder()
                .customerName(dto.getCustomerName())
                .itemName(dto.getItemName())
                .quantity(dto.getQuantity())
                .totalAmount(totalAmount)
                .orderStatus("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        FoodOrder saved = orderRepository.save(order);
        logger.info("INFO  Order placed successfully");
        return saved;
    }

    public FoodOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public List<FoodOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public FoodOrder cancelOrder(Long id) {
        FoodOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if ("CANCELLED".equals(order.getOrderStatus())) {
            return order;
        }

        MenuItem item = menuRepository.findByName(order.getItemName())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with name: " + order.getItemName()));

        item.setAvailableQuantity(item.getAvailableQuantity() + order.getQuantity());
        menuRepository.save(item);

        order.setOrderStatus("CANCELLED");
        FoodOrder updated = orderRepository.save(order);
        logger.info("INFO  Order cancelled successfully");
        return updated;
    }
}
