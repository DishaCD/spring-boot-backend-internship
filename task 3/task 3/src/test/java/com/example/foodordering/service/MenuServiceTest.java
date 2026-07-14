package com.example.foodordering.service;

import com.example.foodordering.dto.MenuItemDTO;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    public void shouldAddMenuItemSuccessfully() {
        // Arrange
        MenuItemDTO dto = MenuItemDTO.builder()
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        MenuItem savedItem = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        when(menuRepository.save(any(MenuItem.class))).thenReturn(savedItem);

        // Act
        MenuItem result = menuService.addMenuItem(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Veg Burger", result.getName());
        verify(menuRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    public void shouldReturnMenuItemById() {
        // Arrange
        MenuItem item = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        when(menuRepository.findById(eq(1L))).thenReturn(Optional.of(item));

        // Act
        MenuItem result = menuService.getMenuItemById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Veg Burger", result.getName());
        verify(menuRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void shouldThrowExceptionWhenMenuItemNotFound() {
        // Arrange
        when(menuRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            menuService.getMenuItemById(1L);
        });

        verify(menuRepository, times(1)).findById(eq(1L));
        verify(menuRepository, never()).save(any(MenuItem.class));
    }

    @Test
    public void shouldUpdateMenuItemSuccessfully() {
        // Arrange
        MenuItem existingItem = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        MenuItemDTO dto = MenuItemDTO.builder()
                .name("Updated Veg Burger")
                .category("Fast Food")
                .price(130.0)
                .availableQuantity(40)
                .build();

        MenuItem updatedItem = MenuItem.builder()
                .id(1L)
                .name("Updated Veg Burger")
                .category("Fast Food")
                .price(130.0)
                .availableQuantity(40)
                .build();

        when(menuRepository.findById(eq(1L))).thenReturn(Optional.of(existingItem));
        when(menuRepository.save(any(MenuItem.class))).thenReturn(updatedItem);

        // Act
        MenuItem result = menuService.updateMenuItem(1L, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Veg Burger", result.getName());
        assertEquals(130.0, result.getPrice());
        assertEquals(40, result.getAvailableQuantity());
        verify(menuRepository, times(1)).findById(eq(1L));
        verify(menuRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    public void shouldDeleteMenuItemSuccessfully() {
        // Arrange
        MenuItem existingItem = MenuItem.builder()
                .id(1L)
                .name("Veg Burger")
                .category("Fast Food")
                .price(120.0)
                .availableQuantity(50)
                .build();

        when(menuRepository.findById(eq(1L))).thenReturn(Optional.of(existingItem));
        doNothing().when(menuRepository).delete(any(MenuItem.class));

        // Act
        menuService.deleteMenuItem(1L);

        // Assert
        verify(menuRepository, times(1)).findById(eq(1L));
        verify(menuRepository, times(1)).delete(any(MenuItem.class));
    }
}
