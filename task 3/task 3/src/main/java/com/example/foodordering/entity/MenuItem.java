package com.example.foodordering.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private Double price;
    private Integer availableQuantity;

    public MenuItem() {
    }

    public MenuItem(Long id, String name, String category, Double price, Integer availableQuantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public static MenuItemBuilder builder() {
        return new MenuItemBuilder();
    }

    public static class MenuItemBuilder {
        private Long id;
        private String name;
        private String category;
        private Double price;
        private Integer availableQuantity;

        public MenuItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuItemBuilder category(String category) {
            this.category = category;
            return this;
        }

        public MenuItemBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public MenuItemBuilder availableQuantity(Integer availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public MenuItem build() {
            return new MenuItem(id, name, category, price, availableQuantity);
        }
    }
}
