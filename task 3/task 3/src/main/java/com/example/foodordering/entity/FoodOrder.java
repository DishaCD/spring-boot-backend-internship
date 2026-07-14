package com.example.foodordering.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String itemName;
    private Integer quantity;
    private Double totalAmount;
    private String orderStatus; // PLACED, CANCELLED
    private LocalDateTime orderDate;

    public FoodOrder() {
    }

    public FoodOrder(Long id, String customerName, String itemName, Integer quantity, Double totalAmount, String orderStatus, LocalDateTime orderDate) {
        this.id = id;
        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    // Builder pattern
    public static FoodOrderBuilder builder() {
        return new FoodOrderBuilder();
    }

    public static class FoodOrderBuilder {
        private Long id;
        private String customerName;
        private String itemName;
        private Integer quantity;
        private Double totalAmount;
        private String orderStatus;
        private LocalDateTime orderDate;

        public FoodOrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FoodOrderBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public FoodOrderBuilder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public FoodOrderBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public FoodOrderBuilder totalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public FoodOrderBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public FoodOrderBuilder orderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public FoodOrder build() {
            return new FoodOrder(id, customerName, itemName, quantity, totalAmount, orderStatus, orderDate);
        }
    }
}
