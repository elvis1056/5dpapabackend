package com.fivepapa.backend.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;  // 商品名稱

    @Column(columnDefinition = "TEXT")
    private String description;  // 商品描述

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;  // 商品價格

    @Column(nullable = false)
    private Integer stock;  // 庫存數量

    @Column(name = "image_url", length = 500)
    private String imageUrl;  // 商品圖片網址

    // 商品分類（ManyToOne）- 一個產品屬於一個分類
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private Boolean active = true;  // 是否上架

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 建立時間

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 更新時間

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
