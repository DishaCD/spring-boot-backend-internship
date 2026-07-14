package com.example.foodordering.controller;

import com.example.foodordering.dto.MenuItemDTO;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAddMenuItem() throws Exception {
        MenuItemDTO dto = new MenuItemDTO("Veg Burger", "Fast Food", 120.0, 50);
        MenuItem item = new MenuItem(1L, "Veg Burger", "Fast Food", 120.0, 50);

        when(menuService.addMenuItem(any(MenuItemDTO.class))).thenReturn(item);

        mockMvc.perform(post("/api/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Veg Burger"));
    }

    @Test
    public void shouldReturnAllMenuItems() throws Exception {
        MenuItem item = new MenuItem(1L, "Veg Burger", "Fast Food", 120.0, 50);
        when(menuService.getAllMenuItems()).thenReturn(Collections.singletonList(item));

        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Veg Burger"));
    }

    @Test
    public void shouldReturnMenuItemById() throws Exception {
        MenuItem item = new MenuItem(1L, "Veg Burger", "Fast Food", 120.0, 50);
        when(menuService.getMenuItemById(1L)).thenReturn(item);

        mockMvc.perform(get("/api/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Veg Burger"));
    }

    @Test
    public void shouldUpdateMenuItem() throws Exception {
        MenuItemDTO dto = new MenuItemDTO("Updated Veg Burger", "Fast Food", 130.0, 40);
        MenuItem item = new MenuItem(1L, "Updated Veg Burger", "Fast Food", 130.0, 40);

        when(menuService.updateMenuItem(eq(1L), any(MenuItemDTO.class))).thenReturn(item);

        mockMvc.perform(put("/api/menu/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Veg Burger"));
    }

    @Test
    public void shouldDeleteMenuItem() throws Exception {
        doNothing().when(menuService).deleteMenuItem(1L);

        mockMvc.perform(delete("/api/menu/1"))
                .andExpect(status().isNoContent());

        verify(menuService, times(1)).deleteMenuItem(1L);
    }

    @Test
    public void shouldReturn404WhenMenuItemNotFound() throws Exception {
        when(menuService.getMenuItemById(1L)).thenThrow(new ResourceNotFoundException("Menu item not found with id: 1"));

        mockMvc.perform(get("/api/menu/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    public void shouldReturn400WhenInvalidMenuItemDTO() throws Exception {
        MenuItemDTO dto = new MenuItemDTO("", "", -10.0, -5); // Invalid input

        mockMvc.perform(post("/api/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    public void shouldReturn500WhenUnexpectedErrorOccurs() throws Exception {
        when(menuService.getAllMenuItems()).thenThrow(new RuntimeException("Unexpected db error"));

        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected db error"));
    }
}
