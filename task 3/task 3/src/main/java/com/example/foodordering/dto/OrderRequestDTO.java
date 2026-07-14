package com.example.foodordering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequestDTO {

    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;

    @NotBlank(message = "Item name cannot be blank")
    private String itemName;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(String customerName, String itemName, Integer quantity) {
        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
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

    // Builder pattern
    public static OrderRequestDTOBuilder builder() {
        return new OrderRequestDTOBuilder();
    }

    public static class OrderRequestDTOBuilder {
        private String customerName;
        private String itemName;
        private Integer quantity;

        public OrderRequestDTOBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public OrderRequestDTOBuilder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public OrderRequestDTOBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderRequestDTO build() {
            return new OrderRequestDTO(customerName, itemName, quantity);
        }
    }
}
