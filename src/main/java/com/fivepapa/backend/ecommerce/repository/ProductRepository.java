package com.fivepapa.backend.ecommerce.repository;

import com.fivepapa.backend.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Spring Data JPA 會自動實作這些方法

    // 根據名稱搜尋商品（模糊查詢）
    List<Product> findByNameContaining(String name);

    // 查詢所有上架商品
    List<Product> findByActiveTrue();

    // 查詢價格區間的商品
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}