package com.example.foodordering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class MenuItemDTO {

    @NotBlank(message = "Item name cannot be blank")
    private String name;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be positive or zero")
    private Double price;

    @NotNull(message = "Available quantity cannot be null")
    @PositiveOrZero(message = "Available quantity must be positive or zero")
    private Integer availableQuantity;

    public MenuItemDTO() {
    }

    public MenuItemDTO(String name, String category, Double price, Integer availableQuantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    // Builder pattern
    public static MenuItemDTOBuilder builder() {
        return new MenuItemDTOBuilder();
    }

    public static class MenuItemDTOBuilder {
        private String name;
        private String category;
        private Double price;
        private Integer availableQuantity;

        public MenuItemDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuItemDTOBuilder category(String category) {
            this.category = category;
            return this;
        }

        public MenuItemDTOBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public MenuItemDTOBuilder availableQuantity(Integer availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public MenuItemDTO build() {
            return new MenuItemDTO(name, category, price, availableQuantity);
        }
    }
}
