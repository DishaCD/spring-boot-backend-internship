package com.example.foodordering.service;

import com.example.foodordering.dto.OrderRequestDTO;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.exception.InsufficientStockException;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.MenuRepository;
import com.example.foodordering.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void shouldPlaceOrderSuccessfully() {
        // Arrange
        OrderRequestDTO dto = OrderRequestDTO.builder()
                .customerName("Harshitha")
                .itemName("Veg Burger")
                .quantity(2)
                .build();

        MenuItem item = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        FoodOrder savedOrder = FoodOrder.builder()
                .id(1L)
                .customerName("Harshitha")
                .itemName("Veg Burger")
                .quantity(2)
                .totalAmount(240.0)
                .orderStatus("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        when(menuRepository.findByName(eq("Veg Burger"))).thenReturn(Optional.of(item));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(item);
        when(orderRepository.save(any(FoodOrder.class))).thenReturn(savedOrder);

        // Act
        FoodOrder result = orderService.placeOrder(dto);

        // Assert
        assertNotNull(result);
        assertEquals(240.0, result.getTotalAmount());
        assertEquals(48, item.getAvailableQuantity()); // 50 - 2
        assertEquals("PLACED", result.getOrderStatus());
        verify(menuRepository, times(1)).findByName(eq("Veg Burger"));
        verify(menuRepository, times(1)).save(any(MenuItem.class));
        verify(orderRepository, times(1)).save(any(FoodOrder.class));
    }

    @Test
    public void shouldThrowExceptionWhenInsufficientQuantity() {
        // Arrange
        OrderRequestDTO dto = OrderRequestDTO.builder()
                .customerName("Harshitha")
                .itemName("Veg Burger")
                .quantity(60) // Exceeds available stock (50)
                .build();

        MenuItem item = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        when(menuRepository.findByName(eq("Veg Burger"))).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> {
            orderService.placeOrder(dto);
        });

        verify(menuRepository, times(1)).findByName(eq("Veg Burger"));
        verify(menuRepository, never()).save(any(MenuItem.class));
        verify(orderRepository, never()).save(any(FoodOrder.class));
    }

    @Test
    public void shouldReturnOrderById() {
        // Arrange
        FoodOrder order = FoodOrder.builder()
                .id(1L)
                .customerName("Harshitha")
                .itemName("Veg Burger")
                .quantity(2)
                .totalAmount(240.0)
                .orderStatus("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        when(orderRepository.findById(eq(1L))).thenReturn(Optional.of(order));

        // Act
        FoodOrder result = orderService.getOrderById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Harshitha", result.getCustomerName());
        verify(orderRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void shouldCancelOrderSuccessfully() {
        // Arrange
        FoodOrder order = FoodOrder.builder()
                .id(1L)
                .customerName("Harshitha")
                .itemName("Veg Burger")
                .quantity(2)
                .totalAmount(240.0)
                .orderStatus("PLACED")
                .orderDate(LocalDateTime.now())
                .build();

        MenuItem item = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(48)
                .build();

        FoodOrder cancelledOrder = FoodOrder.builder()
                .id(1L)
                .customerName("Harshitha")
                .itemName("Veg Burger")
                .quantity(2)
                .totalAmount(240.0)
                .orderStatus("CANCELLED")
                .orderDate(order.getOrderDate())
                .build();

        when(orderRepository.findById(eq(1L))).thenReturn(Optional.of(order));
        when(menuRepository.findByName(eq("Veg Burger"))).thenReturn(Optional.of(item));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(item);
        when(orderRepository.save(any(FoodOrder.class))).thenReturn(cancelledOrder);

        // Act
        FoodOrder result = orderService.cancelOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals("CANCELLED", result.getOrderStatus());
        assertEquals(50, item.getAvailableQuantity()); // 48 + 2 restored
        verify(orderRepository, times(1)).findById(eq(1L));
        verify(menuRepository, times(1)).findByName(eq("Veg Burger"));
        verify(menuRepository, times(1)).save(any(MenuItem.class));
        verify(orderRepository, times(1)).save(any(FoodOrder.class));
    }

    @Test
    public void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(1L);
        });

        verify(orderRepository, times(1)).findById(eq(1L));
    }
}
