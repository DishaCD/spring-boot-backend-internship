package com.example.foodordering.repository;

import com.example.foodordering.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> {
    Optional<MenuItem> findByName(String name);
}
