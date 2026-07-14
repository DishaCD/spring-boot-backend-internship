package com.example.foodordering.service;

import com.example.foodordering.dto.MenuItemDTO;
import com.example.foodordering.entity.MenuItem;
import com.example.foodordering.exception.ResourceNotFoundException;
import com.example.foodordering.repository.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuItem addMenuItem(MenuItemDTO dto) {
        MenuItem menuItem = MenuItem.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .availableQuantity(dto.getAvailableQuantity())
                .build();
        MenuItem saved = menuRepository.save(menuItem);
        logger.info("INFO  Menu item created successfully");
        return saved;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuRepository.findAll();
    }

    public MenuItem getMenuItemById(Long id) {
        logger.info("INFO  Fetching menu item details");
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    public MenuItem updateMenuItem(Long id, MenuItemDTO dto) {
        MenuItem item = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setPrice(dto.getPrice());
        item.setAvailableQuantity(dto.getAvailableQuantity());
        MenuItem updated = menuRepository.save(item);
        logger.info("INFO  Menu item updated successfully");
        return updated;
    }

    public void deleteMenuItem(Long id) {
        MenuItem item = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        menuRepository.delete(item);
        logger.info("INFO  Menu item deleted successfully");
    }
}
