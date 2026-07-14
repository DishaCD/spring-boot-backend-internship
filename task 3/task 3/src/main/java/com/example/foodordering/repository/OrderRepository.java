package com.example.foodordering.repository;

import com.example.foodordering.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<FoodOrder, Long> {
}
