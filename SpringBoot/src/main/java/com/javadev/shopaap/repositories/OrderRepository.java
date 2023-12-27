package com.javadev.shopaap.repositories;

import com.javadev.shopaap.entity.OrderEntity;
import com.javadev.shopaap.entity.ProductEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findByUserId(Long userId);
}
