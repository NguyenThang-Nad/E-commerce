package com.javadev.shopaap.repositories;

import com.javadev.shopaap.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity,Long> {
    List<ProductImageEntity> findByProductId(Long productId);
}
