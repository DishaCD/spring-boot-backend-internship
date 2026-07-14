package com.example.foodordering.controller;

import com.example.foodordering.dto.OrderRequestDTO;
import com.example.foodordering.entity.FoodOrder;
import com.example.foodordering.exception.InsufficientStockException;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldPlaceOrder() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO("Harshitha", "Veg Burger", 2);
        FoodOrder order = new FoodOrder(1L, "Harshitha", "Veg Burger", 2, 240.0, "PLACED", LocalDateTime.now());

        when(orderService.placeOrder(any(OrderRequestDTO.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalAmount").value(240.0))
                .andExpect(jsonPath("$.orderStatus").value("PLACED"));
    }

    @Test
    public void shouldReturnOrderById() throws Exception {
        FoodOrder order = new FoodOrder(1L, "Harshitha", "Veg Burger", 2, 240.0, "PLACED", LocalDateTime.now());
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Harshitha"));
    }

    @Test
    public void shouldReturnAllOrders() throws Exception {
        FoodOrder order = new FoodOrder(1L, "Harshitha", "Veg Burger", 2, 240.0, "PLACED", LocalDateTime.now());
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(order));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Harshitha"));
    }

    @Test
    public void shouldCancelOrder() throws Exception {
        FoodOrder order = new FoodOrder(1L, "Harshitha", "Veg Burger", 2, 240.0, "CANCELLED", LocalDateTime.now());
        when(orderService.cancelOrder(1L)).thenReturn(order);

        mockMvc.perform(post("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("CANCELLED"));
    }

    @Test
    public void shouldReturn404WhenOrderNotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenThrow(new ResourceNotFoundException("Order not found with id: 1"));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    public void shouldReturn400WhenInsufficientStock() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO("Harshitha", "Veg Burger", 100);
        when(orderService.placeOrder(any(OrderRequestDTO.class))).thenThrow(new InsufficientStockException("Insufficient stock"));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    public void shouldReturn400WhenInvalidOrderDTO() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO("", "", -1); // Invalid values

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}
